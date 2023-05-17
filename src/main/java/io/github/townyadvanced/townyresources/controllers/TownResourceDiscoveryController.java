package io.github.townyadvanced.townyresources.controllers;

import com.gmail.goosius.siegewar.TownOccupationController;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyEconomyHandler;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.Translatable;
import io.github.townyadvanced.townyresources.TownyResources;
import io.github.townyadvanced.townyresources.metadata.SurveyPlotMetaDataController;
import io.github.townyadvanced.townyresources.metadata.TownyResourcesGovernmentMetaDataController;
import io.github.townyadvanced.townyresources.objects.ResourceOfferCategory;
import io.github.townyadvanced.townyresources.settings.TownyResourcesSettings;
import io.github.townyadvanced.townyresources.util.SurveyPlotUtil;
import io.github.townyadvanced.townyresources.util.TownyResourcesMessagingUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.block.Biome;

public class TownResourceDiscoveryController {
    /**
     * Discover a new resource for a town
     * 
     * After discovery, recalculates town production
     * After discovery, recalculates nation production (if the town has an owner nation)
     * 
     * @param resident the resident who did the survey
     * @param town the town
     * @param surveyLevel the level of the survey
     * @param surveyCost the cost of the survey
     * @param alreadyDiscoveredMaterials list of the town's already-discovered materials
     * @throws TownyException 
     */
    public static void discoverNewResource(Resident resident,
                                            Town town,
                                            int surveyLevel,
                                            double surveyCost,
                                            List<String> alreadyDiscoveredMaterials) throws TownyException{

        //Ensure the resource at this level has not already been discovered
        List<String> discoveredResources = TownyResourcesGovernmentMetaDataController.getDiscoveredAsList(town);
        if(surveyLevel <= discoveredResources.size()) {
            throw new TownyException(Translatable.of("townyresources.msg_err_level_x_resource_already_discovered", surveyLevel));
        }

        //Ensure the player can afford this survey
        if (TownyEconomyHandler.isActive()) {
            if(!resident.getAccount().canPayFromHoldings(surveyCost)) {
			    throw new TownyException(Translatable.of("townyresources.msg_err_survey_too_expensive",
                    TownyEconomyHandler.getFormattedBalance(surveyCost), resident.getAccount().getHoldingFormattedBalance()));
            }

            //Pay for the survey
            resident.getAccount().withdraw(surveyCost, "Cost of resources survey.");
        }

        //Calculate a new category and material for discovery
        List<String> discoveredMaterials = new ArrayList<>(alreadyDiscoveredMaterials);
        //The survey command can only be run from within a town, townBlock should not be null.
        TownBlock townBlock = TownyAPI.getInstance().getTownBlock(resident.getPlayer());
        ResourceOfferCategory winningCategory = calculateWinningCategory(discoveredMaterials, townBlock);
        String winningMaterial = calculateWinningMaterial(winningCategory);
        //Discover the resource
        discoveredMaterials.add(winningMaterial);

        //Save to Town
        TownyResourcesGovernmentMetaDataController.setDiscovered(town, discoveredMaterials);
        town.save();

		// Save to TownBlock
		if (TownyResourcesSettings.areSurveyPlotsEnabled())
			SurveyPlotMetaDataController.setDiscovered(townBlock, winningMaterial);

        //Recalculate Town Production
        TownResourceProductionController.recalculateProductionForOneTown(town);

        //Recalculate Nation Production
        if(TownyResources.getPlugin().isSiegeWarInstalled() && TownOccupationController.isTownOccupied(town)) {
            TownResourceProductionController.recalculateProductionForOneNation(TownOccupationController.getTownOccupier(town));
        } else if (town.hasNation()) {
            TownResourceProductionController.recalculateProductionForOneNation(town.getNation());
        }

         //Send global message
        int levelOfNewResource = discoveredMaterials.size();
        double productivityModifierNormalized;
		if (TownyResourcesSettings.isNonDynamicAmountMaterial(winningMaterial))
			productivityModifierNormalized = 1.0;
		else
			productivityModifierNormalized = (double) TownyResourcesSettings.getProductionPercentagesPerResourceLevel().get(levelOfNewResource - 1) / 100;
        int preTaxProduction = (int)((winningCategory.getBaseAmountItems() * productivityModifierNormalized) + 0.5); 
        String categoryName = TownyResourcesMessagingUtil.formatOfferCategoryNameForDisplay(winningCategory);
        String materialName = TownyResourcesMessagingUtil.formatMaterialNameForDisplay(winningMaterial);
        TownyResourcesMessagingUtil.sendGlobalMessage(Translatable.of("townyresources.discovery.success", resident.getName(), categoryName, town.getName(), preTaxProduction, materialName));
    }

	private static ResourceOfferCategory calculateWinningCategory(List<String> alreadyDiscoveredMaterials) throws TownyException {
		return calculateWinningCategory(alreadyDiscoveredMaterials, null);
	}

    private static ResourceOfferCategory calculateWinningCategory(List<String> alreadyDiscoveredMaterials, TownBlock townBlock) throws TownyException{
    	Biome biome = TownyResourcesSettings.areSurveyPlotsEnabled() && townBlock != null ? SurveyPlotUtil.getSurveyPlotBiome(townBlock) : null;
    		
 		/*
 		 * Generate a list of candidate categories
 		 * This list will be comprised of all resource offer categories, except those of already discovered materials
 		 */
 		List<ResourceOfferCategory> candidateCategories = new ArrayList<>();
        CATEGORY_LOOP:
 		for(ResourceOfferCategory category: TownResourceOffersController.getResourceOfferCategoryList()) { 		    
 		    //Skip category if we have already discovered something in it
 		    for(String material: alreadyDiscoveredMaterials) {
 		        if(category.getMaterialsInCategory().contains(material))
    		        continue CATEGORY_LOOP;
            }
			// Skip category because it isn't available from this biome.
			if (biome != null && !category.isAllowedInBiome(biome)) {
				continue CATEGORY_LOOP;
			}

 	        //Add category as a candidate
 	        candidateCategories.add(category);
        }
 		//Ensure there are enough candidates left for a new discovery
        if(candidateCategories.size() < 1)
            throw new TownyException(Translatable.of("townyresources.msg_err_not_enough_offers_left"));

        /*
         * Generate a discovery map which will allow us to pick a winning offer
         * The map is in the form <ID><Candidate>
         */
        int discoveryId = 0;
        Map<Integer,ResourceOfferCategory> discoveryMap = new HashMap<>();
        for(ResourceOfferCategory category: candidateCategories) {
            discoveryMap.put(discoveryId, category);
            discoveryId += category.getDiscoveryWeight();    
        }
                
        //Determine which offer has won
        int winningNumber = (int)((Math.random() * discoveryId));
        ResourceOfferCategory winningCategory = null;
        for(Map.Entry<Integer, ResourceOfferCategory> candidate: discoveryMap.entrySet()) {
            if(winningNumber >= candidate.getKey() && winningNumber < candidate.getKey() + candidate.getValue().getDiscoveryWeight()) {
                winningCategory = candidate.getValue();
                break;
            }
        }

        return winningCategory;
    }

    private static String calculateWinningMaterial(ResourceOfferCategory winningCategory) {
        //Determine the winning material
        int winningNumber = (int)((Math.random() * winningCategory.getMaterialsInCategory().size()));
        String winningMaterial = winningCategory.getMaterialsInCategory().get(winningNumber);
        return winningMaterial;
    }

    public static void reRollAllExistingResources() {
        for(Town town: TownyUniverse.getInstance().getTowns())
        	reRollExistingResources(town, true);
        //Now recalculate production for all towns & nations
        TownResourceProductionController.recalculateAllProduction();
    }

	public static void reRollExistingResources(Town town, boolean alltowns) {
		int numTownResources = TownyResourcesGovernmentMetaDataController.getDiscoveredAsList(town).size();
		if (numTownResources > 0) {
			List<String> discoveredTownResources = new ArrayList<>();
			// ReRoll all resources of the town
			try {
				// Using Survey Plots.
				if (TownyResourcesSettings.areSurveyPlotsEnabled()) {
					List<TownBlock> surveyPlots = town.getTownBlocks().stream()
							.filter(tb -> SurveyPlotUtil.isSurveyPlot(tb.getType()))
							.collect(Collectors.toList());
					for (TownBlock surveyPlot : surveyPlots) {
						String winningMaterial = calculateWinningMaterial(calculateWinningCategory(discoveredTownResources, surveyPlot));
						discoveredTownResources.add(winningMaterial);
						SurveyPlotMetaDataController.setDiscovered(surveyPlot, winningMaterial);
					}
				// Using old system without Survey Plots and Biome dependencies.
				} else {
					for (int i = 0; i < numTownResources; i++) {
						discoveredTownResources.add(calculateWinningMaterial(calculateWinningCategory(discoveredTownResources)));
					}
				}
			} catch (Exception ignored) {
				// An exception may occur if the offers list is too small for a discovery.
				// But we assume that is intended and do not throw.
			}
			// Set the new list of town resources
			TownyResourcesGovernmentMetaDataController.setDiscovered(town, discoveredTownResources);
			// Save town
			town.save();
			
			// If this is happening to just one town, recalculate the production.
			if (!alltowns) {
				TownResourceProductionController.recalculateProductionForOneTown(town);
				if (town.hasNation()) 
					TownResourceProductionController.recalculateProductionForOneNation(town.getNationOrNull());
			}
		}
	}

	/**
	 * Attempt to remove a single resource from a Town's discovered resources list.
	 * @param town Town which is going to lose a discovered resource.
	 * @param resource String which represents a resource being removed.
	 */
	public static void removeResourceFromTown(Town town, String resource) {
		List<String> discoveredResources = TownyResourcesGovernmentMetaDataController.getDiscoveredAsList(town);
		if (discoveredResources.size() == 1 && discoveredResources.get(0).equalsIgnoreCase(resource)) {
			TownyResourcesGovernmentMetaDataController.removeDiscovered(town);
			TownResourceProductionController.recalculateProductionForOneTown(town);
			return;
		}

		List<String> resources = new ArrayList<>();
		for (String discoveredResource: discoveredResources) {
			if (discoveredResource.equalsIgnoreCase(resource))
				continue;
			resources.add(discoveredResource);
		}

		if (resources.isEmpty())
			TownyResourcesGovernmentMetaDataController.removeDiscovered(town);
		else
			TownyResourcesGovernmentMetaDataController.setDiscovered(town, resources);

		TownResourceProductionController.recalculateProductionForOneTown(town);
	}
}
