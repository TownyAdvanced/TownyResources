package io.github.townyadvanced.townyresources.listeners;

import io.github.townyadvanced.townyresources.TownyResources;
import io.github.townyadvanced.townyresources.settings.TownyResourcesSettings;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;

/**
 * 
 * @author Goosius
 *
 */
public class TownyResourcesBukkitEventListener implements Listener {

	@SuppressWarnings("unused")
	private final TownyResources plugin;
	
	public TownyResourcesBukkitEventListener(TownyResources instance) {

		plugin = instance;
	}

	/*
	 * TownyResources limits the amount of mob drops of which each player can collect
	 */
	@EventHandler()
	public void onEntityPickupItem(EntityPickupItemEvent event) {
		if(TownyResourcesSettings.isEnabled()) {
			
		}
	}

	/*
	 * TownyResources limit the amount of resource blocks which each player can break.
	 */
	@EventHandler()
	public void onEntityPickupItem(BlockBreakEvent event) {
		if(TownyResourcesSettings.isEnabled()) {
			
		}	
	}

}
