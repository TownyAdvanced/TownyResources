package io.github.townyadvanced.townyresources.controllers;

import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import io.github.townyadvanced.townyresources.metadata.TownyResourcesGovernmentMetaDataController;
import io.github.townyadvanced.townyresources.util.TownyResourcesMessagingUtil;

import java.util.Arrays;
import java.util.List;

public class TownProductionController {

    public static List<String> getDiscoveredResources(Town town) {
        String[] resourcesArray = TownyResourcesGovernmentMetaDataController.getDiscovered(town)
                    .replaceAll(" ","")
                    .split(",");
        return Arrays.asList(resourcesArray);
    }

    public static void discoverNewResource(Resident resident, Town town, List<String> alreadyDiscoveredResources) {
        String newResource = "GOLD_ORE"; //Logic here etc.
        alreadyDiscoveredResources.add(newResource);
        TownyResourcesGovernmentMetaDataController.setDiscovered(town, alreadyDiscoveredResources);
        town.save();
   
   		//Send global message
		TownyResourcesMessagingUtil.sendGlobalMessage("Somebody discovered a new resource woohoo");
        
    }
}
