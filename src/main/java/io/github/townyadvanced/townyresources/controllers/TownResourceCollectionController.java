package io.github.townyadvanced.townyresources.controllers;

import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.object.Town;
import io.github.townyadvanced.townyresources.metadata.TownyResourcesGovernmentMetaDataController;
import io.github.townyadvanced.townyresources.settings.TownyResourcesTranslation;
import io.github.townyadvanced.townyresources.util.TownyResourcesMessagingUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TownResourceCollectionController {

    /**
     * Collect all available town resources
     * Synchronized to avoid possibility of duping by 2 players collecting at same time.... 
     * 
     * @param player the player collecting
     * @param town the town
     * @param availableForCollection the list of currently available resources
     */
    public static synchronized void collectAvailableTownResources(Player player, Town town, Map<String,Integer> availableForCollection) {        
        List<ItemStack> itemStackList = new ArrayList<>();
        
        //Calculate stuff to give player
        Material material;
        int amount;
        ItemStack itemStack;
        for(Map.Entry<String,Integer> mapEntry: availableForCollection.entrySet()) {
            material = Material.getMaterial(mapEntry.getKey().toUpperCase());
            if(material == null) {
                TownyResourcesMessagingUtil.sendErrorMsg(player, TownyResourcesTranslation.of("msg_err_cannot_collect_uknown_material", material));
                continue;
            }
            amount = mapEntry.getValue();
            itemStack = new ItemStack(material, amount);
            itemStackList.add(itemStack);
        }
        
        //Drop stuff near player
        Towny.getPlugin().getServer().getScheduler().runTask(Towny.getPlugin(), new Runnable() {
            public void run() {
                Location location = player.getLocation();
                for(ItemStack itemStack: itemStackList) {
                    player.getWorld().dropItemNaturally(location, itemStack);                 
                }
            }
        });    
        
        //Clear available list
        TownyResourcesGovernmentMetaDataController.setAvailableForCollection(town, Collections.emptyMap());

        //Save town
        town.save();
        
        //Notify Player
        TownyResourcesMessagingUtil.sendMsg(player, TownyResourcesTranslation.of("resource.collection.success"));
    }
    
}
