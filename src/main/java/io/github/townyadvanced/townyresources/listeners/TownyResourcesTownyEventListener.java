package io.github.townyadvanced.townyresources.listeners;

import com.gmail.goosius.siegewar.SiegeWar;
import com.palmergames.bukkit.towny.event.PreNewDayEvent;
import com.palmergames.bukkit.towny.event.TownyLoadedDatabaseEvent;
import com.palmergames.bukkit.towny.event.time.NewShortTimeEvent;
import io.github.townyadvanced.townyresources.TownyResources;
import io.github.townyadvanced.townyresources.settings.TownyResourcesSettings;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * 
 * @author Goosius
 *
 */
public class TownyResourcesTownyEventListener implements Listener {

	@SuppressWarnings("unused")
	private final TownyResources plugin;
	
	public TownyResourcesTownyEventListener(TownyResources instance) {

		plugin = instance;
	}
	
	/*
     * Whe the Towny database gets reloaded, Townyresources reloads  also
     */
    @EventHandler
    public void onTownyDatabaseLoad(TownyLoadedDatabaseEvent event) {
        if(TownyResourcesSettings.isEnabled()) {
            TownyResources.info(SiegeWar.prefix + "Towny database reload detected, reloading townyresources...");
            //TownyResourcesController.loadAll();
        }
    }
    
    /*
     * On Towny new day, town resources are automatically extracted
     */
    @EventHandler
    public void onNewDay(PreNewDayEvent event) {
        if(TownyResourcesSettings.isEnabled()) {
            //TownyResourcesController.extractResources();
        }
    }
            
    /*
     * On each ShortTime period, TownyResources saves data on player-extract resources
     */
    @EventHandler
    public void onShortTime(NewShortTimeEvent event) {
        if(TownyResourcesSettings.isEnabled()) {
            //TownyResourcesController.saveDataOnPlayerExtractedResource();
        }
    }
}
