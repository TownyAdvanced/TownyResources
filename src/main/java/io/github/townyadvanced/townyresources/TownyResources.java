package io.github.townyadvanced.townyresources;

import io.github.townyadvanced.townyresources.commands.TownyResourcesAdminCommand;
import io.github.townyadvanced.townyresources.commands.TownyResourcesCommand;
import io.github.townyadvanced.townyresources.controllers.PlayerExtractionLimitsController;
import io.github.townyadvanced.townyresources.controllers.TownResourceCollectionController;
import io.github.townyadvanced.townyresources.controllers.TownResourceProductionController;
import io.github.townyadvanced.townyresources.listeners.TownyResourcesBukkitEventListener;
import io.github.townyadvanced.townyresources.listeners.TownyResourcesNationEventListener;
import io.github.townyadvanced.townyresources.listeners.TownyResourcesTownEventListener;
import io.github.townyadvanced.townyresources.listeners.TownyResourcesTownyEventListener;
import io.github.townyadvanced.townyresources.settings.TownyResourcesSettings;
import io.github.townyadvanced.townyresources.settings.TownyResourcesTranslation;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class TownyResources extends JavaPlugin {
	
	private static TownyResources plugin;
	
    @Override
    public void onEnable() {
    	
    	plugin = this;
    	
        if (!loadAll())
        	onDisable();

    }

	public String getVersion() {
		return this.getDescription().getVersion();
	}

	public static TownyResources getPlugin() {
		return plugin;
	}

	public static String getPrefix() {
		return TownyResourcesTranslation.language != null ? TownyResourcesTranslation.of("plugin_prefix") : "[" + plugin.getName() + "]";
	}

	/**
	 * Load towny resources
	 * 
	 * @return true if load succeeded
	 */
	public boolean loadAll() {
		try {
			printSickASCIIArt();
			TownyResourcesSettings.loadConfig(this.getDataFolder().getPath() + File.separator + "config.yml", getVersion());
			TownyResourcesTranslation.loadLanguage(this.getDataFolder().getPath() + File.separator , "english.yml");
			registerListeners();
			registerCommands();
			TownResourceProductionController.recalculateAllProduction();
			PlayerExtractionLimitsController.loadAllResourceExtractionCategories();
		} catch (Exception e) {
            e.printStackTrace();
            severe("TownyResources failed to load! Disabling!");
            return false;
        }
		info("TownyResources loaded successfully.");		
		return true;
	}

	/**
	 * Re-Load towny resources
	 * 
	 * @return true if reload succeeded
	 */
	public boolean reloadAll() {
		try {
			TownyResourcesSettings.loadConfig(this.getDataFolder().getPath() + File.separator + "config.yml", getVersion());
			TownyResourcesTranslation.loadLanguage(this.getDataFolder().getPath() + File.separator , "english.yml");
			TownResourceProductionController.recalculateAllProduction();
			PlayerExtractionLimitsController.loadAllResourceExtractionCategories();
		} catch (Exception e) {
            e.printStackTrace();
            severe("TownyResources failed to reload!");
            return false;
        }
		info("TownyResources reloaded successfully.");		
		return true;
	}

	public static void info(String message) {
		plugin.getLogger().info(message);
	}

	public static void severe(String message) {
		plugin.getLogger().severe(message);
	}

	private void printSickASCIIArt() {	
			Bukkit.getLogger().info("");	
			Bukkit.getLogger().info("__________                                                  ________                                                                           ");
			Bukkit.getLogger().info("MMMMMMMMMM                                                  `MMMMMMMb.                                                                         ");
			Bukkit.getLogger().info("/   MM   \\                                                   MM    `Mb                                                                         ");
			Bukkit.getLogger().info("    MM   _____  ____    _    ___ ___  __  ____    ___        MM     MM   ____     ____     _____  ___   ___ ___  __   ____     ____     ____");   
			Bukkit.getLogger().info("    MM  6MMMMMb `MM(   ,M.   )M' `MM 6MMb `MM(    )M'        MM     MM  6MMMMb   6MMMMb\\  6MMMMMb `MM    MM `MM 6MM  6MMMMb.  6MMMMb   6MMMMb\\ ");
			Bukkit.getLogger().info("    MM 6M'   `Mb `Mb   dMb   d'   MMM9 `Mb `Mb    d'         MM    .M9 6M'  `Mb MM'    ` 6M'   `Mb MM    MM  MM69 \" 6M'   Mb 6M'  `Mb MM'    ` ");
			Bukkit.getLogger().info("    MM MM     MM  YM. ,PYM. ,P    MM'   MM  YM.  ,P          MMMMMMM9' MM    MM YM.      MM     MM MM    MM  MM'    MM    `' MM    MM YM.      ");
			Bukkit.getLogger().info("    MM MM     MM  `Mb d'`Mb d'    MM    MM   MM  M           MM  \\M\\   MMMMMMMM  YMMMMb  MM     MM MM    MM  MM     MM       MMMMMMMM  YMMMMb  ");
			Bukkit.getLogger().info("    MM MM     MM   YM,P  YM,P     MM    MM   `Mbd'           MM   \\M\\  MM            `Mb MM     MM MM    MM  MM     MM       MM            `Mb ");
			Bukkit.getLogger().info("    MM YM.   ,M9   `MM'  `MM'     MM    MM    YMP            MM    \\M\\ YM    d9 L    ,MM YM.   ,M9 YM.   MM  MM     YM.   d9 YM    d9 L    ,MM ");
			Bukkit.getLogger().info("   _MM_ YMMMMM9     YP    YP     _MM_  _MM_    M            _MM_    \\M\\_YMMMM9  MYMMMM9   YMMMMM9   YMMM9MM__MM_     YMMMM9   YMMMM9  MYMMMM9  ");
			Bukkit.getLogger().info("                                              d'                                                                                               ");
			Bukkit.getLogger().info("                                          (8),P                                                                                              ");
			Bukkit.getLogger().info("                                           YMM                                            By Goosius                                           ");
			Bukkit.getLogger().info("");	
	}

	private void registerListeners() {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(new TownyResourcesBukkitEventListener(this), this);
		pm.registerEvents(new TownyResourcesTownyEventListener(this), this);
		pm.registerEvents(new TownyResourcesTownEventListener(this), this);
		pm.registerEvents(new TownyResourcesNationEventListener(this), this);
		TownyResources.info("Listeners Loaded");		
	}

	private void registerCommands() {
		getCommand("townyresources").setExecutor(new TownyResourcesCommand());
		getCommand("townyresourcesadmin").setExecutor(new TownyResourcesAdminCommand());
		TownyResources.info("Commands Loaded");		
	}

}
