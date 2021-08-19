package io.github.townyadvanced.townyresources.listeners;

import com.palmergames.bukkit.towny.event.PreNewDayEvent;
import com.palmergames.bukkit.towny.event.TownyLoadedDatabaseEvent;
import com.palmergames.bukkit.towny.event.time.NewShortTimeEvent;
import io.github.townyadvanced.townyresources.TownyResources;
import io.github.townyadvanced.townyresources.controllers.PlayerExtractionLimitsController;
import io.github.townyadvanced.townyresources.controllers.TownResourceProductionController;
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
	private static long nextProductionRecalculationTime = 0;

	public TownyResourcesTownyEventListener(TownyResources instance) {
		plugin = instance;
	}

	/**
     * Whe the Towny database gets reloaded, Townyresources reloads also.
     */
    @EventHandler
    public void onTownyDatabaseLoad(TownyLoadedDatabaseEvent event) {
        if(TownyResourcesSettings.isEnabled()) {
            TownyResources.info("Towny database reload detected, reloading townyresources...");
              TownyResources.getPlugin().reloadAll();
        }
    }

    /**
     * On Towny new day, town resources are automatically extracted.
     */
    @EventHandler
    public void onNewDay(PreNewDayEvent event) {
        if(TownyResourcesSettings.isEnabled()) {
            TownResourceProductionController.produceAllResources();
        }
    }
       
    /**
     * On each ShortTime period, TownyResources saves data on player-extracted resources.
     * 
     * Every 10 mins, the produced town & nation resources are recalculated. 
     */
    @EventHandler
    public void onNewShortTime(NewShortTimeEvent event) {
        if(TownyResourcesSettings.isEnabled()) {
            PlayerExtractionLimitsController.resetMobsDamagedByPlayers();
            PlayerExtractionLimitsController.saveDataOnPlayerExtractedResources();

            if(System.currentTimeMillis() > nextProductionRecalculationTime) {
                nextProductionRecalculationTime = System.currentTimeMillis() + 600000; //10 mins
                TownResourceProductionController.recalculateAllProduction();
            }
        }
    }
}
