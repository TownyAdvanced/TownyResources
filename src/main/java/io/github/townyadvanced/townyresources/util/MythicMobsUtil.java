package io.github.townyadvanced.townyresources.util;

import java.util.Optional;

import org.bukkit.inventory.ItemStack;

import io.github.townyadvanced.townyresources.TownyResources;
import io.lumine.xikage.mythicmobs.items.MythicItem;

public class MythicMobsUtil {


	public static ItemStack getMythicItemStack(String materialName) {
		ItemStack mythicItem = null;
		if (TownyResources.getPlugin().isMythicMobsLegacy()) {
			mythicItem = io.lumine.xikage.mythicmobs.MythicMobs.inst().getItemManager().getItemStack(materialName);
		} else if (TownyResources.getPlugin().isMythicMobsV5()) {
			mythicItem = io.lumine.mythic.bukkit.MythicBukkit.inst().getItemManager().getItemStack(materialName);
		}
		return mythicItem;
	}
	
	public static boolean isValidItem(String materialName) {
		ItemStack mythicItem = getMythicItemStack(materialName);
		if (mythicItem != null)
			return true; // Known material
		return false;
	}

	public static String getMaterialNameForDisplay(String materialName) {
		String name = null;
		if (TownyResources.getPlugin().isMythicMobsLegacy()) {
			Optional<MythicItem> maybeMythicItem = io.lumine.xikage.mythicmobs.MythicMobs.inst().getItemManager()
					.getItem(materialName);

			if (maybeMythicItem.isPresent()) {
				MythicItem mythicItem = maybeMythicItem.get();
				String maybeDisplayName = mythicItem.getDisplayName();
				if (maybeDisplayName != null) {
					return maybeDisplayName.replaceAll("[^\\w\\s]\\w", "");
				} else {
					// MythicItem#getDisplayName() will always return null for imported items
					// try to extract the name from the raw config data
					if (mythicItem.getConfig().isSet("ItemStack")) {
						ItemStack is = mythicItem.getConfig().getItemStack("ItemStack", (String) null);
						if (is != null && is.hasItemMeta() && is.getItemMeta().hasDisplayName()) {
							return is.getItemMeta().getDisplayName();
						}
					}
				}
			}
		} else if (TownyResources.getPlugin().isMythicMobsV5()) {
			Optional<io.lumine.mythic.core.items.MythicItem> maybeMythicItem = io.lumine.mythic.bukkit.MythicBukkit
					.inst().getItemManager().getItem(materialName);
			if (maybeMythicItem.isPresent()) {
				io.lumine.mythic.core.items.MythicItem mythicItem = maybeMythicItem.get();
				String maybeDisplayName = mythicItem.getDisplayName();
				if (maybeDisplayName != null) {
					return maybeDisplayName.replaceAll("[^\\w\\s]\\w", "");
				} else {
					if (mythicItem.getConfig().isSet("ItemStack")) {
						ItemStack is = mythicItem.getConfig().getItemStack("ItemStack", (String) null);
						if (is != null && is.hasItemMeta() && is.getItemMeta().hasDisplayName()) {
							return is.getItemMeta().getDisplayName();
						}
					}
				}
			}
		}
		return name;
	}
}
