package io.github.townyadvanced.townyresources.listeners;

import io.github.townyadvanced.townyresources.controllers.PlayerExtractionLimitsController;
import io.github.townyadvanced.townyresources.settings.TownyResourcesSettings;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockShearEntityEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;

/**
 * 
 * @author Goosius
 *
 */
public class TownyResourcesBukkitEventListener implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		if(TownyResourcesSettings.isEnabled()) {
			PlayerExtractionLimitsController.processEntityDamageByEntityEvent(event);
		}
	}

	@EventHandler()
	public void onEntityDeathEvent(EntityDeathEvent event) {
		if(TownyResourcesSettings.isEnabled()) {
			PlayerExtractionLimitsController.processEntityDeathEvent(event);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		if(TownyResourcesSettings.isEnabled()) {
			PlayerExtractionLimitsController.processBlockBreakEvent(event);
		}	
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onBlockShearEntityEvent(BlockShearEntityEvent event) {
		if(TownyResourcesSettings.isEnabled() && TownyResourcesSettings.areResourceExtractionLimitsEnabled() && TownyResourcesSettings.areShearingExtractionLimitsEnabled()) {
			//Dispensers cannot shear entities
			event.setCancelled(true);
		}	
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerShearEntityEvent(PlayerShearEntityEvent event) {
		if(TownyResourcesSettings.isEnabled() && TownyResourcesSettings.areResourceExtractionLimitsEnabled() && TownyResourcesSettings.areShearingExtractionLimitsEnabled()) {
			PlayerExtractionLimitsController.processPlayerShearEntityEvent(event);
		}	
	}
	
	
	@EventHandler(ignoreCancelled = true)
	public void onItemSpawnEvent(ItemSpawnEvent event) {
		if(TownyResourcesSettings.isEnabled()) {
			PlayerExtractionLimitsController.processItemSpawnEvent(event);
		}	
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerFishEvent(PlayerFishEvent event) {
		if(TownyResourcesSettings.isEnabled()) {
			PlayerExtractionLimitsController.processPlayerFishEvent(event);
		}	
	}
	
	
	@EventHandler()
	public void onPlayerLoginEvent(PlayerLoginEvent event) {
		if(TownyResourcesSettings.isEnabled()) {		
			PlayerExtractionLimitsController.processPlayerLoginEvent(event);
		}			
	}

	@EventHandler()
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		if(TownyResourcesSettings.isEnabled()) {		
			PlayerExtractionLimitsController.processPlayerQuitEvent(event);
		}			
	}
}
