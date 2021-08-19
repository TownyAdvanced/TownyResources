package io.github.townyadvanced.townyresources.listeners;

import io.github.townyadvanced.townyresources.TownyResources;
import io.github.townyadvanced.townyresources.controllers.PlayerExtractionLimitsController;
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

	//Limit the picking up of resources (take care of drops like Beef)
	@EventHandler()
	public void onEntityPickupItem(EntityPickupItemEvent event) {
		if(TownyResourcesSettings.isEnabled()) {
			PlayerExtractionLimitsController.processEntityPickupItemEvent(event);
		}
	}

	//Limit the breaking of resource blocks (takes care of breaking ores e.g. Diamond)
	@EventHandler()
	public void onBlockBreak(BlockBreakEvent event) {
		if(TownyResourcesSettings.isEnabled()) {
			
		}	
	}

}
