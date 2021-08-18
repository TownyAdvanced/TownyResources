package io.github.townyadvanced.townyresources.controllers;

import com.gmail.goosius.siegewar.TownOccupationController;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import io.github.townyadvanced.townyresources.metadata.TownyResourcesGovernmentMetaDataController;
import io.github.townyadvanced.townyresources.objects.ResourceOffer;
import io.github.townyadvanced.townyresources.settings.TownyResourcesSettings;
import io.github.townyadvanced.townyresources.settings.TownyResourcesTranslation;
import io.github.townyadvanced.townyresources.util.TownyResourcesMessagingUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TownResourceDiscoveryController {
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
    public static void discoverNewResource(Resident resident, Town town, List<String> alreadyDiscoveredResources) throws TownyException{
 		/*
 		 * Get the list of all possible resources which can be found in a new discovery
 		 * This list will be comprised of all resource offers, except already discovered resources
 		 */
 		List<ResourceOffer> resourceOfferCandidates = 
 		    new ArrayList<>(TownyResourcesSettings.getResourceOffers(alreadyDiscoveredResources).values());

 		//Ensure there are enough candidates left for a new discovery
        if(resourceOfferCandidates.size() < 1)
            throw new TownyException(TownyResourcesTranslation.of("msg_err_not_enough_offers_left"));

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
        TownResourceProductionController.recalculateProductionForOneTown(town, allResourceOffers);

        //Recalculate Nation Production
        if(TownOccupationController.isTownOccupied(town)) {
            TownResourceProductionController.recalculateProductionForOneNation(TownOccupationController.getTownOccupier(town), allResourceOffers);
        } else if (town.hasNation()) {
            TownResourceProductionController.recalculateProductionForOneNation(town.getNation(),allResourceOffers);
        }
    }

}
