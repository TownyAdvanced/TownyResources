package io.github.townyadvanced.townyresources.util;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import io.th0rgal.oraxen.api.OraxenItems;
import io.th0rgal.oraxen.items.ItemBuilder;

public class OraxenUtil {

	@Nullable
	public static ItemStack getItemStack(String materialName, int amount) {
		ItemBuilder builder = OraxenItems.getItemById(materialName);
		if (builder == null)
			return null;
		ItemStack itemStack = builder.build();
		itemStack.setAmount(amount);
		return itemStack;
	}

	public static String getMaterialNameForDisplay(String materialName) {
		ItemBuilder builder = OraxenItems.getItemById(materialName);
		if (builder == null)
			return null;
		return builder.getDisplayName();
	}

	public static boolean isValidItem(String materialName) {
		return OraxenItems.exists(materialName);
	}
}
