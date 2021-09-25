package io.github.townyadvanced.townyresources;

import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.util.Version;
import io.github.townyadvanced.townyresources.commands.TownyResourcesAdminCommand;
import io.github.townyadvanced.townyresources.commands.TownyResourcesCommand;
import io.github.townyadvanced.townyresources.controllers.PlayerExtractionLimitsController;
import io.github.townyadvanced.townyresources.controllers.TownResourceOffersController;
import io.github.townyadvanced.townyresources.controllers.TownResourceProductionController;
import io.github.townyadvanced.townyresources.listeners.*;
import io.github.townyadvanced.townyresources.settings.TownyResourcesSettings;
import io.github.townyadvanced.townyresources.settings.TownyResourcesTranslation;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class TownyResources extends JavaPlugin {
	
	private static Version requiredTownyVersion = Version.fromString("0.97.1.0");
	private static boolean siegeWarInstalled;
	private static boolean dynmapTownyInstalled; 
	private static boolean languageUtilsInstalled;
	private static boolean slimeFunInstalled;
	
    @Override
    public void onEnable() {

        if (!loadAll())
        	onDisable();

    }

	public String getVersion() {
		return this.getDescription().getVersion();
	}

	public static TownyResources getPlugin() {
		return getPlugin(TownyResources.class);
	}

	public static String getPrefix() {
		return TownyResourcesTranslation.language != null ? TownyResourcesTranslation.of("plugin_prefix") : "[" + getPlugin().getName() + "]";
	}

	/**
	 * Load towny resources
	 * 
	 * @return true if load succeeded
	 */
	public boolean loadAll() {
		try {
			printSickASCIIArt();
			townyVersionCheck();
			//Setup integrations with other plugins
			setupIntegrationsWithOtherPlugins();
			//Load settings and languages
			TownyResourcesSettings.loadConfig(this.getDataFolder().getPath() + File.separator + "config.yml", getVersion());
			TownyResourcesTranslation.loadLanguage(this.getDataFolder().getPath() + File.separator , "english.yml");
			//Load controllers
			TownResourceOffersController.loadAllResourceOfferCategories();
			//WARNING: Do not try to recalculate production here, because unless SW has been loaded first, the results will be incorrect.
			PlayerExtractionLimitsController.loadAllResourceExtractionCategories();
			//Load commands and listeners
			registerCommands();
			registerListeners();
		} catch (TownyException te) {
			severe(te.getMessage());
            severe("TownyResources failed to load! Disabling!");
            return false;
		} catch (Exception e) {
			severe(e.getMessage());
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
			//Load settings and languages
			TownyResourcesSettings.loadConfig(this.getDataFolder().getPath() + File.separator + "config.yml", getVersion());
			TownyResourcesTranslation.loadLanguage(this.getDataFolder().getPath() + File.separator , "english.yml");
			//Load controllers
			TownResourceOffersController.loadAllResourceOfferCategories();
			TownResourceProductionController.recalculateAllProduction();
			PlayerExtractionLimitsController.loadAllResourceExtractionCategories();
			PlayerExtractionLimitsController.reloadAllExtractionRecordsForLoggedInPlayers();
		} catch (Exception e) {
            e.printStackTrace();
			severe(e.getMessage());
            severe("TownyResources failed to reload!");
            return false;
        }
		info("TownyResources reloaded successfully.");		
		return true;
	}

	public static void info(String message) {
		getPlugin().getLogger().info(message);
	}

	public static void severe(String message) {
		getPlugin().getLogger().severe(message);
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
		if(isDynmapTownyInstalled())
			pm.registerEvents(new TownyResourcesDynmapTownyListener(this), this);
		TownyResources.info("Listeners Loaded");		
	}

	private void registerCommands() {
		getCommand("townyresources").setExecutor(new TownyResourcesCommand());
		getCommand("townyresourcesadmin").setExecutor(new TownyResourcesAdminCommand());
		TownyResources.info("Commands Loaded");		
	}

	public boolean isDynmapTownyInstalled() {
		return dynmapTownyInstalled;
	}

	/**
	* This method is used before checking for the effects of occupation on resources.
	* Ideally we would also like to check if siegewar is enabled before doing this.
	* However to do that, we would need to load siegewar before townyresources,
	* which would change the position of resources on the town screen.
	* Maybe this will be added in future, especially if someone actually needs it. 
	*/
	public boolean isSiegeWarInstalled() {
		return siegeWarInstalled;
	}

	public boolean isLanguageUtilsInstalled() {
		return languageUtilsInstalled;
	}

	public boolean isSlimeFunInstalled() {
		return slimeFunInstalled;
	}
	
	private String getTownyVersion() {
        return Bukkit.getPluginManager().getPlugin("Towny").getDescription().getVersion();
    }
    
	private void townyVersionCheck() throws TownyException{
		String actualTownyVersion = getTownyVersion();
        boolean comparisonResult = Version.fromString(actualTownyVersion).compareTo(requiredTownyVersion) >= 0;        
		if (!comparisonResult) {
			throw new TownyException("Towny version does not meet required minimum version: " + requiredTownyVersion.toString());
		} else {
			info("Towny version " + actualTownyVersion + " found.");
		}
    }
    
    private void setupIntegrationsWithOtherPlugins() {
		//Determine if other plugins are installed
		Plugin siegeWar = Bukkit.getPluginManager().getPlugin("SiegeWar");
		siegeWarInstalled = siegeWar != null;
		if(siegeWarInstalled) 
			info("SiegeWar Integration Enabled");
		else
			info("SiegeWar Integration Not Enabled");					

		Plugin dynmapTowny = Bukkit.getPluginManager().getPlugin("Dynmap-Towny");
		dynmapTownyInstalled = dynmapTowny != null;
		if(dynmapTownyInstalled) 
			info("DynmapTowny Integration Enabled");
		else
			info("DynmapTowny Integration Not Enabled");					
				
		Plugin slimeFun = Bukkit.getPluginManager().getPlugin("Slimefun");
		slimeFunInstalled = slimeFun != null;
		if(slimeFunInstalled) 
			info("Slimefun Integration Enabled");
		else
			info("Slimefun Integration Not Enabled");
			
		Plugin languageUtils = Bukkit.getPluginManager().getPlugin("LangUtils");
		languageUtilsInstalled = languageUtils != null;
		if(languageUtilsInstalled) 
			info("LanguageUtils Integration Enabled");
		else
			info("LanguageUtils Integration Not Enabled");						
	}
}
