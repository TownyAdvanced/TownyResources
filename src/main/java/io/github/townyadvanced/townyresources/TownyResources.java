package io.github.townyadvanced.townyresources;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.exceptions.initialization.TownyInitException;
import com.palmergames.bukkit.towny.object.Translatable;
import com.palmergames.bukkit.towny.object.TranslationLoader;
import com.palmergames.bukkit.util.Colors;
import com.palmergames.bukkit.util.Version;

import io.github.townyadvanced.townyresources.commands.NationCollectAddon;
import io.github.townyadvanced.townyresources.commands.TownResourcesAddon;
import io.github.townyadvanced.townyresources.commands.TownyAdminResourcesAddon;
import io.github.townyadvanced.townyresources.controllers.PlayerExtractionLimitsController;
import io.github.townyadvanced.townyresources.controllers.TownResourceOffersController;
import io.github.townyadvanced.townyresources.controllers.TownResourceProductionController;
import io.github.townyadvanced.townyresources.listeners.*;
import io.github.townyadvanced.townyresources.settings.TownyResourcesSettings;
import io.github.townyadvanced.townyresources.util.TownyResourcesMessagingUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;
import java.nio.file.Paths;

public class TownyResources extends JavaPlugin {
	
	private static TownyResources plugin;
	private static Version requiredTownyVersion = Version.fromString("0.98.6.3");
	private static boolean siegeWarInstalled;
	private static boolean dynmapTownyInstalled; 
	private static boolean languageUtilsInstalled;
	private static boolean slimeFunInstalled;
	private static boolean legacyMythicMobsInstalled;
	private static boolean mythicMobsInstalled;
	private static boolean mmmoItemsInstalled;
	
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
		return "[" + plugin.getName() + "]";
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
			TownyResourcesSettings.loadConfig();
			loadLocalization(false);
			new TownyResourcesMessagingUtil(this);

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
			TownyResourcesSettings.loadConfig();
			loadLocalization(true);
			new TownyResourcesMessagingUtil(this);
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

	private void loadLocalization(boolean reload) throws TownyException {
		try {
			Plugin plugin = getPlugin(); 
			Path langFolderPath = Paths.get(plugin.getDataFolder().getPath()).resolve("lang");
			TranslationLoader loader = new TranslationLoader(langFolderPath, plugin, TownyResources.class);
			loader.load();
			TownyAPI.getInstance().addTranslations(plugin, loader.getTranslations());
		} catch (TownyInitException e) {
			throw new TownyException("Locale files failed to load! Disabling!");
		}
		if (reload) {
			info(Translatable.of("msg_reloaded_lang").defaultLocale());
		}
	}

	public static void info(String message) {
		plugin.getLogger().info(message);
	}

	public static void severe(String message) {
		plugin.getLogger().severe(message);
	}

	private void printSickASCIIArt() {
		Bukkit.getConsoleSender().sendMessage("");
		Bukkit.getConsoleSender().sendMessage(              "       --------------- Goosius' ---------------  ");
		Bukkit.getConsoleSender().sendMessage(Colors.Gold + "     ╔╦╗┌─┐┬ ┬┌┐┌┬ ┬  ╦═╗┌─┐┌─┐┌─┐┬ ┬┬─┐┌─┐┌─┐┌─┐");
		Bukkit.getConsoleSender().sendMessage(Colors.Gold + "      ║ │ │││││││└┬┘  ╠╦╝├┤ └─┐│ ││ │├┬┘│  ├┤ └─┐");
		Bukkit.getConsoleSender().sendMessage(Colors.Gold + "      ╩ └─┘└┴┘┘└┘ ┴   ╩╚═└─┘└─┘└─┘└─┘┴└─└─┘└─┘└─┘");
		Bukkit.getConsoleSender().sendMessage("");
		Bukkit.getConsoleSender().sendMessage(              "       ---------- Maintained by LlmDl ---------  ");
		Bukkit.getConsoleSender().sendMessage(              "       -- https://github.com/sponsors/LlmDl  --  ");
		Bukkit.getConsoleSender().sendMessage("");
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
		new TownResourcesAddon();
		new NationCollectAddon();
		new TownyAdminResourcesAddon();
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
	
	public boolean isMythicMobsLegacy() {
		return legacyMythicMobsInstalled;
	}
	
	public boolean isMythicMobsV5() {
		return mythicMobsInstalled;
	}

	public boolean isMMOItemsInstalled() {
		return mmmoItemsInstalled;
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
			String className = Bukkit.getServer().getPluginManager().getPlugin("MythicMobs").getClass().getName();
			if (className.equals("io.lumine.xikage.mythicmobs.MythicMobs")) {
				legacyMythicMobsInstalled = true;
				info("  Legacy Mythic Mobs Integration Enabled");
			} else if (className.equals("io.lumine.mythic.bukkit.MythicBukkit")) {
				mythicMobsInstalled = true;
				info("  Mythic Mobs Integration Enabled");
			} else {
				mythicMobsInstalled = false;
				severe("Problem enabling mythic mobs");
			}
		}

		Plugin mmmoItems = Bukkit.getPluginManager().getPlugin("MMOItems");
		mmmoItemsInstalled = mmmoItems != null;
		if (mmmoItemsInstalled)
			info("  MMOItems Integration Enabled");

		Plugin languageUtils = Bukkit.getPluginManager().getPlugin("LangUtils");
		languageUtilsInstalled = languageUtils != null;
		if(languageUtilsInstalled) 
			info("  LanguageUtils Integration Enabled");
	}
}
