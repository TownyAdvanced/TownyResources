package io.github.townyadvanced.townyresources.controllers;

import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import io.github.townyadvanced.townyresources.metadata.TownyResourcesGovernmentMetaDataController;
import io.github.townyadvanced.townyresources.objects.CandidateResource;
import io.github.townyadvanced.townyresources.settings.TownyResourcesSettings;
import io.github.townyadvanced.townyresources.settings.TownyResourcesTranslation;
import io.github.townyadvanced.townyresources.util.TownyResourcesMessagingUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TownProductionController {

    private static int candidateId;  //Used for resource discovery
    
    public static List<String> getDiscoveredResources(Town town) {
        String resourcesString = TownyResourcesGovernmentMetaDataController.getDiscovered(town);
        if(resourcesString.isEmpty()) {
            return new ArrayList<>();
        } else {
            String[] resourcesArray = resourcesString.split(",");
            return Arrays.asList(resourcesArray);        
        }
    }

    public static void discoverNewResource(Resident resident, Town town, List<String> alreadyDiscoveredResources) {
        //Reset the candidateID
        candidateId = 0;    
        
        //Compile a list of all candidate resources
        List<CandidateResource> allCandidates = new ArrayList<>();
        allCandidates.addAll(generateCandidatesForDiscovery(TownyResourcesSettings.getOffersOres(), "ores"));
        allCandidates.addAll(generateCandidatesForDiscovery(TownyResourcesSettings.getOffersTrees(), "trees"));
        allCandidates.addAll(generateCandidatesForDiscovery(TownyResourcesSettings.getOffersCrops(), "crops"));
        allCandidates.addAll(generateCandidatesForDiscovery(TownyResourcesSettings.getOffersAnimals(), "animals"));
        allCandidates.addAll(generateCandidatesForDiscovery(TownyResourcesSettings.getOffersMonsters(), "monsters"));
 
        /*
         * Generate a random number to determine which candidate will win
         * This number will be between 0 and the current candidateId
         */
        int winningNumber = (int)((Math.random() * candidateId) + 0.5);
        
        //Determine which candidate has won
        CandidateResource winningCandidate = null;
        CandidateResource candidate = null;
        CandidateResource nextCandidate;
        for(int i = 0; i < allCandidates.size() - 1 ; i++) {               
            candidate = allCandidates.get(i);    //Not the last entry. Check if it is the winner
            nextCandidate = allCandidates.get(i+1);
            if(winningNumber >= candidate.getCandidateID() && winningNumber < nextCandidate.getCandidateID()) {
                winningCandidate = candidate;
                break;
            }
        }

        //If winning candidate is still won, we must be at the last candidate in the list, who now wins
        if(winningCandidate == null) {
            winningCandidate = candidate;
        }

        //Discover the resource
        alreadyDiscoveredResources.add(winningCandidate.getResourceMaterialName());
        TownyResourcesGovernmentMetaDataController.setDiscovered(town, alreadyDiscoveredResources);
        town.save();
   
   		//Send global message
   		String translationkey = "discovery.message." + winningCandidate.getResourceCategory();
		TownyResourcesMessagingUtil.sendGlobalMessage(TownyResourcesTranslation.of(translationkey, resident.getName(), town.getName()));
     
        //TODO - recalculate the town production here (& nation if there is one)   
    }

    /**
     * Generate a list of candidate resources for discovery
     * 
     * @param offersList a list of offers
     * @param offersCategory the category of that list
     * @return A list of candidate resources
     */
    private static List<CandidateResource> generateCandidatesForDiscovery(List<String> offersList, String offersCategory) {
        List<CandidateResource> result = new ArrayList<>();
        CandidateResource candidateResource;
        String[] offerMaterialAndWeight;
        int offerWeight;
        String offerMaterial;

        for(String offer: offersList) {
            offerMaterialAndWeight = offer.split("-");
            offerMaterial = offerMaterialAndWeight[0];
            offerWeight = Integer.parseInt(offerMaterialAndWeight[1]);
            candidateResource = new CandidateResource(offerMaterial, offersCategory, candidateId); 
            result.add(candidateResource);
            candidateId += offerWeight;  //Calculate id of next candidate 
        }

        return result;
    }
    
    public static void recalculateTownProduction(Town town) {
        //recalculate production for a single town
        //Also do for its owner nation if it has one
    }
   
}
