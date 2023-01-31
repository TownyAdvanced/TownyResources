package io.github.townyadvanced.townyresources.util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.townyadvanced.townyresources.TownyResources;
import io.github.townyadvanced.townyresources.settings.TownyResourcesSettings;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.Type;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import net.Indyuce.mmoitems.api.player.PlayerData;

public class MMOItemsUtil {

	static Function<String, ItemStack> itemFunction = (str) -> getMMOItemsItemStackSync(str);
	static Function<ItemStack, String> displayNameFunction = (item) -> item.getItemMeta().getDisplayName();

	public static String getMaterialNameForDisplay(String materialName) {
		return displayNameFunction.apply(itemFunction.apply(materialName));
	}

	public static boolean isValidItem(String materialName) {
		return getMMOItemsMMOItem(materialName) != null;
	}

	private static MMOItem getMMOItemsMMOItem(String materialName) {
		return MMOItems.plugin.getMMOItem(getType(materialName), getID(materialName));
	}

	public static ItemStack getMMOItemsItemStack(String materialName, Player player) {
		if (!TownyResourcesSettings.areMMOItemsGivenLeveledTowardsThePlayer())
			return getMMOItemsItemStack(materialName);
		return MMOItems.plugin.getItem(getType(materialName), getID(materialName), PlayerData.get(player));
	}

	public static ItemStack getMMOItemsItemStack(String materialName) {
		return MMOItems.plugin.getItem(getType(materialName), getID(materialName));
	}

	public static ItemStack getMMOItemsItemStackSync(String materialName) {
		MMOItem mmoItem = getMMOItemsMMOItem(materialName);
		if (mmoItem == null)
			return null;
		Future<ItemStack> future = Bukkit.getScheduler().callSyncMethod(TownyResources.getPlugin(), new MMOItemGetter(mmoItem));
		ItemStack item = null;
		try {
			item = future.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return item;
	}
	
	public static Type getType(String name) {
		return Type.get(name.split(":")[0]);
	}
	
	public static String getID(String name) {
		return name.split(":")[1];
	}
}
