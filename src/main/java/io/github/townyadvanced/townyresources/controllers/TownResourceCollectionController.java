package io.github.townyadvanced.townyresources.controllers;

import com.palmergames.bukkit.towny.object.Government;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.Translatable;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.townyadvanced.townyresources.TownyResources;
import io.github.townyadvanced.townyresources.metadata.TownyResourcesGovernmentMetaDataController;
import io.github.townyadvanced.townyresources.util.MMOItemsUtil;
import io.github.townyadvanced.townyresources.util.MythicMobsUtil;
import io.github.townyadvanced.townyresources.util.TownyResourcesMessagingUtil;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class TownResourceCollectionController {

    public static synchronized void collectAvailableTownResources(Player player, Town town, Map<String,Integer> availableForCollection) {
        //Collect resources
        if (!collectAvailableGovernmentResources(player, town, availableForCollection))
            return;
        //Notify Player
        TownyResourcesMessagingUtil.sendMsg(player, Translatable.of("townyresources.resource.towncollect.success"));
    }
    
    public static synchronized void collectAvailableNationResources(Player player, Nation nation, Map<String,Integer> availableForCollection) {
        //Collect resources
        if (!collectAvailableGovernmentResources(player, nation, availableForCollection))
            return;
        //Notify Player
        TownyResourcesMessagingUtil.sendMsg(player, Translatable.of("townyresources.resource.nationcollect.success"));
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
    private static synchronized boolean collectAvailableGovernmentResources(Player player, Government government, Map<String,Integer> availableForCollection) {
        //Calculate stuff to give player
        List<ItemStack> itemStackList = buildItemStackList(player, availableForCollection.entrySet());

        //See if player can hold any items at all.
        PlayerInventory inv = player.getInventory();
        if (inv.firstEmpty() == -1) {
        	TownyResourcesMessagingUtil.sendMsg(player, Translatable.of("townyresources.resource.you_have_no_room_in_your_inventory"));
            return false;
        }

        final Map<Integer, ItemStack> itemsThatDontFit = inv.addItem(itemStackList.toArray(new ItemStack[0]));
        /* If map is not empty, some items were not added.... */
        if (!itemsThatDontFit.isEmpty()) {
            Map<String,Integer> remainder = new HashMap<>();
            for (ItemStack stack : itemsThatDontFit.values()) {
                if (remainder.containsKey(stack.getType().name())) {
                    int amount = remainder.get(stack.getType().name()) + stack.getAmount();
                    remainder.put(stack.getType().name(), amount);
                } else {
                	remainder.put(stack.getType().name(), stack.getAmount());
                }
            }
            TownyResourcesGovernmentMetaDataController.setAvailableForCollection(government, remainder);
        } else {
            //Clear available list
            TownyResourcesGovernmentMetaDataController.setAvailableForCollection(government, Collections.emptyMap());
        }

        //Save government
        government.save();
        return true;
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

            // MMOItems integration
            if (TownyResources.getPlugin().isMMOItemsInstalled() && materialName.contains(":")) {
            	ItemStack mmoItem = MMOItemsUtil.getMMOItemsItemStack(materialName, player);
            	if (mmoItem != null) {
            		itemStack = mmoItem;
            		itemStack.setAmount(amount);
            		itemStackList.add(itemStack);
            		continue;
            	}
            }
            //Unknown material. Send error message
            TownyResourcesMessagingUtil.sendErrorMsg(player, Translatable.of("townyresources.msg_err_cannot_collect_unknown_material", materialName));
        }
		return itemStackList;
	}
    
}
