package io.github.townyadvanced.townyresources.controllers;

import com.gmail.goosius.siegewar.TownOccupationController;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownySettings;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import io.github.townyadvanced.townyresources.TownyResources;
import io.github.townyadvanced.townyresources.metadata.TownyResourcesGovernmentMetaDataController;
import io.github.townyadvanced.townyresources.objects.ResourceOffer;
import io.github.townyadvanced.townyresources.settings.TownyResourcesSettings;
import io.github.townyadvanced.townyresources.settings.TownyResourcesTranslation;
import io.github.townyadvanced.townyresources.util.TownyResourcesMessagingUtil;

import java.util.*;

public class TownProductionController {

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
    public static void discoverNewResource(Resident resident, Town town, List<String> alreadyDiscoveredResources) throws Exception {
 		/*
 		 * Get the list of all possible resources which can be found in a new discovery
 		 * This list will be comprised of all resource offers, except already discovered resources
 		 */
 		List<ResourceOffer> resourceOfferCandidates = 
 		    new ArrayList<>(TownyResourcesSettings.getResourceOffers(alreadyDiscoveredResources).values());

 		//Ensure there are enough candidates left for a new discovery
        if(resourceOfferCandidates.size() < 1)
            throw new TownyException("msg_err_not_enough_offers_left");

        //Generate a random number to determine which offer will win
        int winningNumber = (int)((Math.random() * TownyResourcesSettings.getSumOfAllOfferDiscoveryProbabilityWeights()) + 0.5);

        //Determine which  offer has won
        ResourceOffer winningCandidate = null;
        ResourceOffer candidate;
        for(int i = 0; i < resourceOfferCandidates.size(); i++) { 
            candidate = resourceOfferCandidates.get(i);   
            if(winningNumber >= candidate.getDiscoveryId() && winningNumber < candidate.getDiscoveryId() + candidate.getDiscoveryProbabilityWeight()) {
                winningCandidate = candidate;
                break;
            }
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
        Map<String, ResourceOffer> allResourceOffers = TownyResourcesSettings.getAllResourceOffers();
        recalculateProductionForOneTown(town, allResourceOffers);

        //Recalculate Nation Production
        if(TownOccupationController.isTownOccupied(town)) {
            recalculateProductionForOneNation(TownOccupationController.getTownOccupier(town), allResourceOffers);
        } else if (town.hasNation()) {
            recalculateProductionForOneNation(town.getNation(),allResourceOffers);
        }
    }

    /**
     * Recalculate production for all towns
     * 
     * Note: This method does not recalculate production for any nations
     */
    private static void recalculateProductionForAllTowns(Map<String, ResourceOffer> allResourceOffers) {
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
    private static void recalculateProductionForOneTown(Town town, Map<String, ResourceOffer> allResourceOffers) {
        try {
            //Get discovered resources
            List<String> discoveredResources = new ArrayList<>(TownyResourcesGovernmentMetaDataController.getDiscoveredAsList(town));
    
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
        } catch (Exception e) {
            TownyResources.severe("Problem recalculating production for town" + town.getName());
        }
    }

    /**
     * Recalculate production for all nations
     * 
     * Note: This method does not recalculate production for any towns
     */
    private static void recalculateProductionForAllNations(Map<String, ResourceOffer> allResourceOffers) {
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
    private static void recalculateProductionForOneNation(Nation nation, Map<String, ResourceOffer> allResourceOffers) {

    }

    /**
     * Recalculate production for all towns and nations
     */
    public static void recalculateAllProduction() {
        Map<String, ResourceOffer> allResourceOffers = TownyResourcesSettings.getAllResourceOffers();
        recalculateProductionForAllTowns(allResourceOffers);
        recalculateProductionForAllNations(allResourceOffers);
    }

    /**
     * Extracts town resources, and makes them available for collection
     * 
     * Extract to both town and nation
     */
    public static void extractAllResources() {
        extractAllResourcesForTowns();
        extractAllResourcesForNations();
    }
    
    public static void extractAllResourcesForTowns() {
        for(Town town: TownyUniverse.getInstance().getTowns()) {
            try {
            //Get the list of resources which are already available for collection
            Map<String,Integer> townResources = TownyResourcesGovernmentMetaDataController.getAvailableForCollectionAsMap(town);

            //Get daily production
            Map<String, Integer> townDailyProduction = TownyResourcesGovernmentMetaDataController.getDailyProductionAsMap(town);

            //Extract resources
            String resource;
            int quantity;
            for(Map.Entry<String, Integer> extractedResource: townDailyProduction.entrySet()) {
                resource = extractedResource.getKey();
                quantity = extractedResource.getValue();
                if(townResources.containsKey(resource)) {
                    //Add to existing available resources
                    townResources.put(resource, quantity + townResources.get(resource));
                } else {
                    //Add new available resource
                    townResources.put(resource, quantity);
                }
            }

            //Set the list of resources available for collection
            TownyResourcesGovernmentMetaDataController.setAvailableForCollection(town, townResources);    
            
            //Save town
            town.save();
            
            } catch (Exception e) {
                TownyResources.severe("Problem extracting resources for town" + town.getName());
            }
        }        
    }

    public static void extractAllResourcesForNations() {
    }
}
