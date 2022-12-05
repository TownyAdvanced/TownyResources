package io.github.townyadvanced.townyresources.util;

import java.util.concurrent.Callable;

import org.bukkit.inventory.ItemStack;

import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.Type;

public class MMOItemGetter implements Callable<ItemStack> {
	private String name;
	public MMOItemGetter(String name) {
		this.name = name;
	}

	@Override
	public ItemStack call() {
		return MMOItems.plugin.getItem(getType(name), getID(name));
	}

	public static Type getType(String name) {
		return Type.get(name.split(":")[0]);
	}

	public static String getID(String name) {
		return name.split(":")[1];
	}
}
