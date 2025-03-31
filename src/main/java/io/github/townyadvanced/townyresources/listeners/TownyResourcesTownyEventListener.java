package io.github.townyadvanced.townyresources.listeners;

import com.palmergames.bukkit.towny.event.PreNewDayEvent;
import com.palmergames.bukkit.towny.event.TownyLoadedDatabaseEvent;
import com.palmergames.bukkit.towny.event.TranslationLoadEvent;
import com.palmergames.bukkit.towny.event.time.NewShortTimeEvent;
import com.palmergames.bukkit.towny.object.TranslationLoader;

import io.github.townyadvanced.townyresources.TownyResources;
import io.github.townyadvanced.townyresources.controllers.PlayerExtractionLimitsController;
import io.github.townyadvanced.townyresources.controllers.TownResourceProductionController;
import io.github.townyadvanced.townyresources.settings.TownyResourcesSettings;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

/**
 * 
 * @author Goosius
 *
 */
public class TownyResourcesTownyEventListener implements Listener {

	private static int PRODUCTION_RECALCULATION_INTERVAL_MILLIS = 600000; //10 mins
	private static long nextProductionRecalculationTime = 0; //0 so that it recalculates immediately on the 1st short tick

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
            PlayerExtractionLimitsController.resetDailyExtractionLimits();
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
            PlayerExtractionLimitsController.saveExtractionRecordsForOnlinePlayers();

            if(System.currentTimeMillis() > nextProductionRecalculationTime) {
                nextProductionRecalculationTime = System.currentTimeMillis() + PRODUCTION_RECALCULATION_INTERVAL_MILLIS; 
                TownResourceProductionController.recalculateAllProduction();
            }
        }
    }

	/*
	 * When Towny is reloading the languages, make sure we're re-injecting our language strings. 
	 */
	@EventHandler(ignoreCancelled = true)
	public void onTownyLoadLanguages(TranslationLoadEvent event) {
		Plugin plugin = TownyResources.getPlugin();
		Path langFolderPath = Paths.get(plugin.getDataFolder().getPath()).resolve("lang");
		TranslationLoader loader = new TranslationLoader(langFolderPath, plugin, TownyResources.class);
		loader.load();
		Map<String, Map<String, String>> translations = loader.getTranslations();

		for (String language : translations.keySet())
			for (Map.Entry<String, String> map : translations.get(language).entrySet())
				event.addTranslation(language, map.getKey(), map.getValue());
	}
}
