package io.github.townyadvanced.townyresources.controllers;

import com.gmail.goosius.siegewar.TownOccupationController;
import com.gmail.goosius.siegewar.metadata.TownMetaDataController;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownySettings;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import io.github.townyadvanced.townyresources.metadata.TownyResourcesGovernmentMetaDataController;
import io.github.townyadvanced.townyresources.objects.ResourceOffer;
import io.github.townyadvanced.townyresources.settings.TownyResourcesSettings;
import io.github.townyadvanced.townyresources.settings.TownyResourcesTranslation;
import io.github.townyadvanced.townyresources.util.TownyResourcesMessagingUtil;

import java.util.*;

public class TownProductionController {

    /**
     * Get the discovered resources of a town
     * 
     * @param town the town
     * @return the town's discovered resources
     */
    public static List<String> getDiscoveredResources(Town town) {
        String resourcesString = TownyResourcesGovernmentMetaDataController.getDiscovered(town);
        if(resourcesString.isEmpty()) {
            return new ArrayList<>();
        } else {
            String[] resourcesArray = resourcesString.split(",");
            return Arrays.asList(resourcesArray);        
        }
    }

    /**
     * Discover a new resource for a town
     * 
     * After discovery, recalculates town production
     * After discovery, recalculates nation production (if the town has an owner nation)
     * 
     * @param resident the resident who did the survey
     * @param town the town
     * @param alreadyDiscoveredResources list of the town's already-discovered resources
     * @throws TownyException 
     */
    public static void discoverNewResource(Resident resident, Town town, List<String> alreadyDiscoveredResources) throws TownyException {
 		//Get all resource offers <material name, offer>
 		Map<String, ResourceOffer> allResourceOffers = TownyResourcesSettings.getAllResourceOffers();

 		//Remove already discovered resources
 		for(String alreadyDiscoveredResource: alreadyDiscoveredResources) {
 		    if(allResourceOffers.containsKey(alreadyDiscoveredResource)) {
 		        allResourceOffers.remove(alreadyDiscoveredResource);
            }
        }

 		//Ensure there are enough offers left for a new discovery
        if(allResourceOffers.size() < 1)
            throw new TownyException("msg_err_not_enough_offers_left");

        //Generate a random number to determine which offer will win
        int winningNumber = (int)((Math.random() * TownyResourcesSettings.getSumOfAllOfferDiscoveryProbabilityWeights()) + 0.5);

        //Determine which  offer has won
        List<ResourceOffer> candidates = new ArrayList<>(allResourceOffers.values());
        ResourceOffer winningCandidate = null;
        ResourceOffer candidate;
        ResourceOffer nextCandidate = null;
        for(int i = 0; i < candidates.size() - 1; i++) {      //Don't check the last entry 
            candidate = candidates.get(i);   
            nextCandidate = candidates.get(i+1);
            if(winningNumber >= candidate.getDiscoveryId() && winningNumber < nextCandidate.getDiscoveryId()) {
                winningCandidate = candidate;
                break;
            }
        }

        //If no winner was found yet, the only remaining candidate is the last entry, which must be the winner
        if(winningCandidate == null) {
            winningCandidate = nextCandidate;
        }

        //Discover the resource
        List<String> discoveredResources = new ArrayList<>(alreadyDiscoveredResources);
        discoveredResources.add(winningCandidate.getMaterial());
        TownyResourcesGovernmentMetaDataController.setDiscovered(town, discoveredResources);
        town.save();

   		//Send global message
   		int levelOfNewResource = discoveredResources.size();
   		double productivityModifierNormalized = (double)TownyResourcesSettings.getProductionPercentagesPerResourceLevel().get(levelOfNewResource-1) / 100;
        int preTaxProduction = (int)((winningCandidate.getBaseAmount() * productivityModifierNormalized) + 0.5); 
   		String translationkey = "discovery.message." + winningCandidate.getCategory();
        String formattedMaterialName = winningCandidate.getMaterial().replaceAll("_", " ");
		TownyResourcesMessagingUtil.sendGlobalMessage(TownyResourcesTranslation.of(translationkey, resident.getName(), town.getName(), preTaxProduction, formattedMaterialName));

        //Recalculate Town Production
        recalculateProductionForOneTown(town, allResourceOffers);

        //Recalculate Nation Production
        if(TownOccupationController.isTownOccupied(town)) {
            recalculateProductionForOneNation(TownOccupationController.getTownOccupier(town), allResourceOffers);
        } else if (town.hasNation()) {
            recalculateProductionForOneNation(town.getNation(), allResourceOffers);
        }
    }

    /**
     * Recalculate production for all towns
     * 
     * Note: This method does not recalculate production for any nations
     */
    public static void recalculateProductionForAllTowns() {
        Map<String, ResourceOffer> allResourceOffers = TownyResourcesSettings.getAllResourceOffers();

        for(Town town: TownyUniverse.getInstance().getTowns()) {
            recalculateProductionForOneTown(town, allResourceOffers);
        }
    }

    /**
     * Recalculate production for a single town
     * 
     * Note: This method does not recalculate the nation production
     *         
     * @param town the town to recalculate production for
     * @param allResourceOffers all resource offers
     */
    public static void recalculateProductionForOneTown(Town town, Map<String, ResourceOffer> allResourceOffers) {
        //Get discovered resources
        List<String> discoveredResources = new ArrayList<>(getDiscoveredResources(town));

        //Remove any discovered resources which are no longer on offer
        List<String> resourcesToRemove = new ArrayList<>();
        for(String resource: discoveredResources) {
            if(!allResourceOffers.containsKey(resource))
                resourcesToRemove.add(resource);
        }
        if(!resourcesToRemove.isEmpty()) {
            discoveredResources.removeAll(resourcesToRemove);
            TownyResourcesGovernmentMetaDataController.setDiscovered(town, discoveredResources);
            town.save();
        }

        //Determine owner nation
        Nation ownerNation = null;
        double nationCutNormalized = 0;
        double townCutNormalized = 0;
        if(TownOccupationController.isTownOccupied(town)) {
            ownerNation = TownOccupationController.getTownOccupier(town);
            nationCutNormalized = TownyResourcesSettings.getTownResourcesProductionNationTaxNormalized();
            townCutNormalized = 1 - nationCutNormalized;
        } else if (town.hasNation()) {
            ownerNation = TownyAPI.getInstance().getTownNationOrNull(town);
            nationCutNormalized = TownyResourcesSettings.getTownResourcesProductionNationTaxNormalized();
            townCutNormalized = 1 - nationCutNormalized;
        } 

        //Build the town production list
        List<String> townProduction = new ArrayList<>();
        String materialName;
        double baseProductionAmount;
        int finalProductionAmount;
        double resourceLevelProductionModifierNormalized;

        for(int i = 0; i < discoveredResources.size(); i++) {
            //Ensure town meets the town level requirement to produce the resource
            if(TownySettings.calcTownLevel(town) <  TownyResourcesSettings.getProductionTownLevelRequirementPerResourceLevel().get(i)) 
                break;
            materialName = discoveredResources.get(i);
            baseProductionAmount = allResourceOffers.get(materialName).getBaseAmount();
            resourceLevelProductionModifierNormalized = (double) TownyResourcesSettings.getProductionPercentagesPerResourceLevel().get(i) / 100;
            if(ownerNation == null) {
                finalProductionAmount = (int)((baseProductionAmount * resourceLevelProductionModifierNormalized) + 0.5);
            } else {
                finalProductionAmount = (int)((baseProductionAmount * resourceLevelProductionModifierNormalized * townCutNormalized) + 0.5);
            }            
            townProduction.add(finalProductionAmount + "-" + materialName);
        }

        //Save data
        TownyResourcesGovernmentMetaDataController.setDailyProduction(town, townProduction);    
        town.save();  
    }

    /**
     * Recalculate production for all nations
     * 
     * Note: This method does not recalculate production for any towns
     */
    public static void recalculateProductionForAllNations() {
        Map<String, ResourceOffer> allResourceOffers = TownyResourcesSettings.getAllResourceOffers();

        for(Nation nation: TownyUniverse.getInstance().getNations()) {
            recalculateProductionForOneNation(nation, allResourceOffers);
        }
    }

    /**
     * Recalculate production for a single nation
     *         
     * Note: This method does not recalculate production for any towns

     * @param nation the nation to recalculate production for
     * @param allResourceOffers all resource offers
     */
    public static void recalculateProductionForOneNation(Nation nation, Map<String, ResourceOffer> allResourceOffers) {

    }

    /**
     * Recalculate production for all towns and nations
     */
    public static void recalculateAllProduction() {
        recalculateProductionForAllTowns();
        recalculateProductionForAllNations();
    }

    /**
     * Extracts town resources, and makes them available for collection
     */
    public static void extractResources() {
        
    }
}
