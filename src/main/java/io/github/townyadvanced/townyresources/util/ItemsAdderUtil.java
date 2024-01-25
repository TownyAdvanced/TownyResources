package io.github.townyadvanced.townyresources.util;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import dev.lone.itemsadder.api.CustomStack;

public class ItemsAdderUtil {

	@Nullable
	public static ItemStack getItemStack(String materialName, int amount) {
		CustomStack itemsAdderItem = CustomStack.getInstance(materialName);
		if (itemsAdderItem == null)
			return null;
		ItemStack itemStack = itemsAdderItem.getItemStack();
		itemStack.setAmount(amount);
		return itemStack;
	}

	public static String getMaterialNameForDisplay(String materialName) {
		CustomStack itemsAdderItem = CustomStack.getInstance(materialName);
		if (itemsAdderItem == null)
			return null;
		return itemsAdderItem.getDisplayName();
	}

	public static boolean isValidItem(String materialName) {
		return CustomStack.getInstance(materialName) != null;
	}
}
