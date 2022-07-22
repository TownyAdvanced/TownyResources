package io.github.townyadvanced.townyresources.controllers;

import com.palmergames.bukkit.towny.object.Government;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.townyadvanced.townyresources.TownyResources;
import io.github.townyadvanced.townyresources.metadata.TownyResourcesGovernmentMetaDataController;
import io.github.townyadvanced.townyresources.settings.TownyResourcesTranslation;
import io.github.townyadvanced.townyresources.util.MythicMobsUtil;
import io.github.townyadvanced.townyresources.util.TownyResourcesMessagingUtil;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class TownResourceCollectionController {

    public static synchronized void collectAvailableTownResources(Player player, Town town, Map<String,Integer> availableForCollection) {
        //Collect resources
        collectAvailableGovernmentResources(player, town, availableForCollection);
        //Notify Player
        TownyResourcesMessagingUtil.sendMsg(player, TownyResourcesTranslation.of("resource.towncollect.success"));        
    }
    
    public static synchronized void collectAvailableNationResources(Player player, Nation nation, Map<String,Integer> availableForCollection) {
        //Collect resources
        collectAvailableGovernmentResources(player, nation, availableForCollection);
        //Notify Player
        TownyResourcesMessagingUtil.sendMsg(player, TownyResourcesTranslation.of("resource.nationcollect.success"));        
    }
    
    /**
     * Utility Method 
     * Collect all available resources of a government
     * Synchronized to avoid possibility of duping by 2 players collecting at same time.... 
     * 
     * @param player the player collecting
     * @param government the government
     * @param availableForCollection the list of currently available resources
     */
    private static synchronized void collectAvailableGovernmentResources(Player player, Government government, Map<String,Integer> availableForCollection) {
        //Calculate stuff to give player
        List<ItemStack> itemStackList = buildItemStackList(player, availableForCollection.entrySet());
        //Drop all collected itemstacks near player
        Bukkit.getServer().getScheduler().runTask(TownyResources.getPlugin(), ()-> {
            Location location = player.getLocation();
            for(ItemStack itemStack: itemStackList)
                player.getWorld().dropItemNaturally(location, itemStack);
        });

        //Clear available list
        TownyResourcesGovernmentMetaDataController.setAvailableForCollection(government, Collections.emptyMap());

        //Save government
        government.save();
    }

	private static List<ItemStack> buildItemStackList(Player player, Set<Entry<String, Integer>> availableForCollection) {
        List<ItemStack> itemStackList = new ArrayList<>();
        String materialName;
        Material material;
        int amount;
        ItemStack itemStack;
        for(Map.Entry<String,Integer> mapEntry: availableForCollection) {
            materialName = mapEntry.getKey();
            amount = mapEntry.getValue();
            
            //Don't attempt pickup if amount is less than 1
            if(amount < 1)
                continue;

            //Try creating a regular MC itemstack
            material = Material.getMaterial(materialName);
            if(material != null) {
                itemStack = new ItemStack(material, amount);
                itemStackList.add(itemStack); 
                continue;
            }
            
            //Try creating a slimefun itemstack
            if(TownyResources.getPlugin().isSlimeFunInstalled()) {
                SlimefunItem slimeFunItem = SlimefunItem.getById(materialName);
                if(slimeFunItem != null) {
                    itemStack = slimeFunItem.getRecipeOutput();
                    itemStack.setAmount(amount);
                    itemStackList.add(itemStack);
                    continue;
                }
            }

            // mythicmobs integration
            if (TownyResources.getPlugin().isMythicMobsInstalled()) {
                ItemStack mythicItem = MythicMobsUtil.getMythicItemStack(materialName);
                if (mythicItem != null) {
                    itemStack = mythicItem;
                    itemStack.setAmount(amount);
                    itemStackList.add(itemStack);
                    continue;
                }
            }

            //Unknown material. Send error message
            TownyResourcesMessagingUtil.sendErrorMsg(player, TownyResourcesTranslation.of("msg_err_cannot_collect_unknown_material", materialName));
        }
		return itemStackList;
	}
    
}
