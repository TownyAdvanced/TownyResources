package io.github.townyadvanced.townyresources.controllers;

import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import io.github.townyadvanced.townyresources.objects.ResourceQuantity;
import io.github.townyadvanced.townyresources.util.TownyResourcesMessagingUtil;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class TownProductionController {


    public static List<Material> getDiscoveredResources(Town town) {
        List<Material> resources =new ArrayList<>();
        resources.add(Material.OAK_LOG);
        //TODO - read from metadata instead purlease
        return resources;
    }

    public static void discoverNewResource(Resident resident, Town town, int levelToDiscover) {
//        return new ResourceQuantity(Material.GOLD_ORE, 32);
		//Send global message
		TownyResourcesMessagingUtil.sendGlobalMessage("Somebody discovered a new resource woohoo");
        
    }
}
