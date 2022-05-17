package io.github.townyadvanced.townyresources;

import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.util.Colors;
import com.palmergames.bukkit.util.Version;
import io.github.townyadvanced.townyresources.commands.TownyResourcesAdminCommand;
import io.github.townyadvanced.townyresources.commands.TownyResourcesCommand;
import io.github.townyadvanced.townyresources.controllers.PlayerExtractionLimitsController;
import io.github.townyadvanced.townyresources.controllers.TownResourceOffersController;
import io.github.townyadvanced.townyresources.controllers.TownResourceProductionController;
import io.github.townyadvanced.townyresources.listeners.*;
import io.github.townyadvanced.townyresources.settings.TownyResourcesSettings;
import io.github.townyadvanced.townyresources.settings.TownyResourcesTranslation;
import io.lumine.mythic.api.items.ItemManager;
import io.lumine.mythic.bukkit.MythicBukkit;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class TownyResources extends JavaPlugin {
	
	private static TownyResources plugin;
	private static Version requiredTownyVersion = Version.fromString("0.97.5.0");
	private static boolean siegeWarInstalled;
	private static boolean dynmapTownyInstalled; 
	private static boolean languageUtilsInstalled;
	private static boolean slimeFunInstalled;
	private static boolean mythicMobsInstalled;
	
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
		plugin.getLogger().info(message);
	}

	public static void severe(String message) {
		plugin.getLogger().severe(message);
	}

	private void printSickASCIIArt() {
			Bukkit.getLogger().info("");
			Bukkit.getLogger().info(              "        --------------- Goosius' --------------  ");
			Bukkit.getLogger().info(Colors.Gold + "     ╔╦╗┌─┐┬ ┬┌┐┌┬ ┬  ╦═╗┌─┐┌─┐┌─┐┬ ┬┬─┐┌─┐┌─┐┌─┐");
			Bukkit.getLogger().info(Colors.Gold + "      ║ │ │││││││└┬┘  ╠╦╝├┤ └─┐│ ││ │├┬┘│  ├┤ └─┐");
			Bukkit.getLogger().info(Colors.Gold + "      ╩ └─┘└┴┘┘└┘ ┴   ╩╚═└─┘└─┘└─┘└─┘┴└─└─┘└─┘└─┘");
			Bukkit.getLogger().info("");	
	}

	private void registerListeners() {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(new TownyResourcesBukkitEventListener(), this);
		pm.registerEvents(new TownyResourcesTownyEventListener(), this);
		pm.registerEvents(new TownyResourcesTownEventListener(), this);
		pm.registerEvents(new TownyResourcesNationEventListener(), this);
		if(isDynmapTownyInstalled())
			pm.registerEvents(new TownyResourcesDynmapTownyListener(), this);
	}

	private void registerCommands() {
		getCommand("townyresources").setExecutor(new TownyResourcesCommand());
		getCommand("townyresourcesadmin").setExecutor(new TownyResourcesAdminCommand());
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

	public boolean isMythicMobsInstalled() { return mythicMobsInstalled; }

	public ItemManager getMythicItemManager() {
		Plugin mythicMobs = Bukkit.getPluginManager().getPlugin("MythicMobs");
		return mythicMobsInstalled ? (MythicBukkit.inst().getItemManager()) : null;
	}
	
	private String getTownyVersion() {
        return Bukkit.getPluginManager().getPlugin("Towny").getDescription().getVersion();
    }
    
	private void townyVersionCheck() throws TownyException{
		if (!(Version.fromString(getTownyVersion()).compareTo(requiredTownyVersion) >= 0))
			throw new TownyException("Towny version does not meet required minimum version: " + requiredTownyVersion.toString());
    }
    
    private void setupIntegrationsWithOtherPlugins() {
		//Determine if other plugins are installed
		Plugin siegeWar = Bukkit.getPluginManager().getPlugin("SiegeWar");
		siegeWarInstalled = siegeWar != null;
		if(siegeWarInstalled) 
			info("  SiegeWar Integration Enabled");

		Plugin dynmapTowny = Bukkit.getPluginManager().getPlugin("Dynmap-Towny");
		dynmapTownyInstalled = dynmapTowny != null;
		if(dynmapTownyInstalled) 
			info("  DynmapTowny Integration Enabled");
				
		Plugin slimeFun = Bukkit.getPluginManager().getPlugin("Slimefun");
		slimeFunInstalled = slimeFun != null;
		if(slimeFunInstalled) 
			info("  Slimefun Integration Enabled");

		Plugin mythicMobs = Bukkit.getPluginManager().getPlugin("MythicMobs");
		if(mythicMobs != null) {
			try {
				MythicBukkit.inst().getItemManager().getItems();
				mythicMobsInstalled = true;
				info("  Mythic Mobs Integration Enabled");
			} catch (Throwable t) {
                                mythicMobsInstalled = false;
			        t.printStackTrace();
			        severe( "Problem enabling mythic mobs");
			}
		}

		Plugin languageUtils = Bukkit.getPluginManager().getPlugin("LangUtils");
		languageUtilsInstalled = languageUtils != null;
		if(languageUtilsInstalled) 
			info("  LanguageUtils Integration Enabled");
	}
}
