package io.github.townyadvanced.townyresources.controllers;

import com.gmail.goosius.siegewar.TownOccupationController;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownySettings;
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
    
    public static List<String> getDiscoveredResources(Town town) {
        String resourcesString = TownyResourcesGovernmentMetaDataController.getDiscovered(town);
        if(resourcesString.isEmpty()) {
            return new ArrayList<>();
        } else {
            String[] resourcesArray = resourcesString.split(",");
            return Arrays.asList(resourcesArray);        
        }
    }

    public static void discoverNewResource(Resident resident, Town town, List<String> alreadyDiscoveredResources) throws TownyException {
 		//Get all resource offers <material name, offer>
 		Map<String, ResourceOffer> allResourceOffers = TownyResourcesSettings.getAllResourceOffers();
 		
 		//Ensure the offers list is not empty	
        if(allResourceOffers.isEmpty())
            throw new TownyException("msg_err_empty_offers_list");

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
        alreadyDiscoveredResources.add(winningCandidate.getMaterial());
        TownyResourcesGovernmentMetaDataController.setDiscovered(town, alreadyDiscoveredResources);
        town.save();
   
   		//Send global message
   		String translationkey = "discovery.message." + winningCandidate.getCategory();
		TownyResourcesMessagingUtil.sendGlobalMessage(TownyResourcesTranslation.of(translationkey, resident.getName(), town.getName(), winningCandidate.getBaseAmount(), winningCandidate.getMaterial().toLowerCase()));
     
        //Recalculate Town Production
        recalculateTownProduction(town, allResourceOffers);
      
        //Recalculate Nation Production TODO  
//        if(town.hasNation() || TownOccupationController.isTownOccupied(town)
  //          recalculateNationProduction();
    }


    /**
     * Recalculate production for a single town
     * Also do for its owner nation if it has one
     *         
     * @param town the town to recalculate production for
     */
    
    public static void recalculateTownProduction(Town town, Map<String, ResourceOffer> allResourceOffers) {
        //Get discovered resources
        List<String> discoveredResources = getDiscoveredResources(town);

        //Remove any discovered resources which are no longer on offer
        List<String> resourcesToRemove = new ArrayList<>();
        for(String resource: discoveredResources) {
            if(!allResourceOffers.containsKey(resource))
                resourcesToRemove.add(resource);
        }
        if(!resourcesToRemove.isEmpty()) {
            discoveredResources.removeAll(resourcesToRemove);
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
        double finalProductionAmount;
        double resourceLevelProductionModifierNormalized;
   
        for(int i = 0; i < discoveredResources.size(); i++) {
            //Ensure town meets the town level requirement to produce the resource
            if(TownySettings.calcTownLevel(town) <  TownyResourcesSettings.getProductionTownLevelRequirementPerResourceLevel().get(i)) 
                break;
            materialName = discoveredResources.get(i);
            baseProductionAmount = allResourceOffers.get(materialName).getBaseAmount();
            resourceLevelProductionModifierNormalized = (double) TownyResourcesSettings.getProductionPercentagesPerResourceLevel().get(i) / 100;
            if(ownerNation == null) {
                finalProductionAmount = baseProductionAmount * resourceLevelProductionModifierNormalized;
            } else {
                finalProductionAmount = baseProductionAmount * resourceLevelProductionModifierNormalized * townCutNormalized;
            }            
            townProduction.add(materialName + "-" + finalProductionAmount);
        }
        
        //Save data
        TownyResourcesGovernmentMetaDataController.setDailyProduction(town, townProduction);      
    }
   
}
