package io.github.townyadvanced.townyresources.controllers;

import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import io.github.townyadvanced.townyresources.metadata.TownyResourcesGovernmentMetaDataController;
import io.github.townyadvanced.townyresources.util.TownyResourcesMessagingUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TownProductionController {

    public static List<String> getDiscoveredResources(Town town) {
        String resourcesString = TownyResourcesGovernmentMetaDataController.getDiscovered(town);
        if(resourcesString.isEmpty()) {
            return new ArrayList<>();
        } else {
            String[] resourcesArray = TownyResourcesGovernmentMetaDataController.getDiscovered(town)
                    .replaceAll(" ","")
                    .split(",");
            return Arrays.asList(resourcesArray);        
        }
    }

    public static void discoverNewResource(Resident resident, Town town, List<String> alreadyDiscoveredResources) {
        String newResource = "GOLD_ORE"; //Logic here etc.
        
        //Compile a list of all candidate resource
        
        
        //Generate a random number to determine which candidate will win
        
        //Discover the resource
        alreadyDiscoveredResources.add(newResource);
        TownyResourcesGovernmentMetaDataController.setDiscovered(town, alreadyDiscoveredResources);
        town.save();
   
   		//Send global message
		TownyResourcesMessagingUtil.sendGlobalMessage("Somebody discovered a new resource woohoo");
     
        //TODO - recalculate the town production here (& nation if there is one)   
    }
    
    public static void recalculateTownProduction(Town town) {
        //recalculate production for a single town
        //Also do for its owner nation if it has one
    }
   
}
