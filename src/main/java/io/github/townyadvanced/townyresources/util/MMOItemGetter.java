package io.github.townyadvanced.townyresources.util;

import java.util.concurrent.Callable;

import org.bukkit.inventory.ItemStack;

import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;

public class MMOItemGetter implements Callable<ItemStack> {
	private MMOItem mmoItem;
	public MMOItemGetter(MMOItem mmoItem) {
		this.mmoItem = mmoItem;
	}

	@Override
	public ItemStack call() {
		return mmoItem.newBuilder().build();
	}
}
