package io.github.townyadvanced.townyresources;

import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.exceptions.initialization.TownyInitException;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.Translatable;
import com.palmergames.bukkit.towny.object.TranslationLoader;
import com.palmergames.bukkit.towny.scheduling.TaskScheduler;
import com.palmergames.bukkit.towny.scheduling.impl.BukkitTaskScheduler;
import com.palmergames.bukkit.towny.scheduling.impl.FoliaTaskScheduler;
import com.palmergames.bukkit.util.Colors;
import io.github.townyadvanced.townyresources.commands.NationCollectAddon;
import io.github.townyadvanced.townyresources.commands.TownResourcesAddon;
import io.github.townyadvanced.townyresources.commands.TownyAdminResourcesAddon;
import io.github.townyadvanced.townyresources.controllers.PlayerExtractionLimitsController;
import io.github.townyadvanced.townyresources.controllers.TownResourceOffersController;
import io.github.townyadvanced.townyresources.controllers.TownResourceProductionController;
import io.github.townyadvanced.townyresources.listeners.*;
import io.github.townyadvanced.townyresources.metadata.TownyResourcesGovernmentMetaDataController;
import io.github.townyadvanced.townyresources.settings.TownyResourcesSettings;
import io.github.townyadvanced.townyresources.util.SurveyPlotUtil;
import io.github.townyadvanced.townyresources.util.TownyResourcesMessagingUtil;
import me.silverwolfg11.maptowny.MapTownyPlugin;
import me.silverwolfg11.maptowny.managers.LayerManager;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TownyResources extends JavaPlugin {
	
	private static TownyResources plugin;
	private final TaskScheduler scheduler;
	private static String requiredTownyVersion = "0.100.4.0";
	private static boolean siegeWarInstalled;
	private static boolean dynmapTownyInstalled;
	private static boolean mapTownyInstalled;
	private static MapTownyPlugin mapTownyPlugin;
	private static boolean languageUtilsInstalled;
	private static boolean slimeFunInstalled;
	private static boolean mythicMobsInstalled;
	private static boolean mmmoItemsInstalled;
	private static boolean itemsAdderInstalled;
	private static boolean oraxenInstalled;

	public TownyResources() {
		plugin = this;
		this.scheduler = isFoliaClassPresent() ? new FoliaTaskScheduler(this) : new BukkitTaskScheduler(this);
	}

	@Override
	public void onLoad() {
		SurveyPlotUtil.registerSurveyPlotOnLoad(this.getDataFolder().getPath() + File.separator + "config.yml");
	}
	
    @Override
    public void onEnable() {
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

			// Run later to give items plugins a chance to load their items (ItemsAdder specifically.)
			getScheduler().runLater(() -> loadOffersAndExtractionLimitCategories(), 2L);

			//Load commands and listeners
			registerCommands();
			registerListeners();

			SurveyPlotUtil.registerSurveyPlot();

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

	private void loadOffersAndExtractionLimitCategories() {
		try {
			TownResourceOffersController.loadAllResourceOfferCategories();
			PlayerExtractionLimitsController.loadAllResourceExtractionCategories();
		} catch (TownyException te) {
			severe(te.getMessage());
			severe("TownyResources failed to load offers and extraction categories!");
			onDisable();
			return;
		} catch (Exception e) {
			severe(e.getMessage());
			e.printStackTrace();
			severe("TownyResources failed to load offers and extraction categories!");
			onDisable();
			return;
		}
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

			SurveyPlotUtil.registerSurveyPlot();
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
		if (TownyResourcesSettings.areSurveyPlotsEnabled())
			pm.registerEvents(new TownBlockListener(), this);
		if(isDynmapTownyInstalled())
			pm.registerEvents(new TownyResourcesDynmapTownyListener(), this);
		if(isMapTownyInstalled())
			pm.registerEvents(new TownyResourceMapTownyListener(), this);
	}

	private void registerCommands() {
		new TownResourcesAddon();
		new NationCollectAddon();
		new TownyAdminResourcesAddon();
	}

	public boolean isDynmapTownyInstalled() {
		return dynmapTownyInstalled;
	}

	public boolean isMapTownyInstalled() {
		return mapTownyInstalled;
	}

	public MapTownyPlugin getMapTownyPlugin() {
		return mapTownyPlugin;
	}

	public void registerMapTownyReplacements(MapTownyPlugin mapTownyPlugin) {
		LayerManager layerManager = mapTownyPlugin.getLayerManager();
		layerManager.registerReplacement("%town_resources%", this::getResourcesString);
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
	
	public boolean isMythicMobsV5() {
		return mythicMobsInstalled;
	}

	public boolean isItemsAdderInstalled() {
		return itemsAdderInstalled;
	}

	public boolean isOraxenInstalled() {
		return oraxenInstalled;
	}

	public boolean isMMOItemsInstalled() {
		return mmmoItemsInstalled;
	}

	private void townyVersionCheck() throws TownyException{
		try {
			if (Towny.isTownyVersionSupported(requiredTownyVersion))
				return;
		} catch (NoSuchMethodError ignored) {}
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
				
		mapTownyPlugin = (MapTownyPlugin) Bukkit.getPluginManager().getPlugin("MapTowny");
		mapTownyInstalled = mapTownyPlugin != null;
		if (mapTownyInstalled) {
			registerMapTownyReplacements(mapTownyPlugin);
			info("  MapTowny Integration Enabled");
		}

		Plugin slimeFun = Bukkit.getPluginManager().getPlugin("Slimefun");
		slimeFunInstalled = slimeFun != null;
		if(slimeFunInstalled) 
			info("  Slimefun Integration Enabled");

		Plugin mythicMobs = Bukkit.getPluginManager().getPlugin("MythicMobs");
		if(mythicMobs != null) {
			String className = Bukkit.getServer().getPluginManager().getPlugin("MythicMobs").getClass().getName();
			if (className.equals("io.lumine.mythic.bukkit.MythicBukkit")) {
				mythicMobsInstalled = true;
				info("  Mythic Mobs Integration Enabled");
			} else {
				mythicMobsInstalled = false;
				severe("Problem enabling mythic mobs");
			}
		}

		Plugin itemsAdder = Bukkit.getPluginManager().getPlugin("ItemsAdder");
		itemsAdderInstalled = itemsAdder != null;
		if (itemsAdderInstalled)
			info("  ItemsAdder Integration Enabled");

		Plugin oraxen = Bukkit.getPluginManager().getPlugin("Oraxen");
		oraxenInstalled = oraxen != null;
		if (oraxenInstalled)
			info("  Oraxen Integration Enabled");

		Plugin mmmoItems = Bukkit.getPluginManager().getPlugin("MMOItems");
		mmmoItemsInstalled = mmmoItems != null;
		if (mmmoItemsInstalled)
			info("  MMOItems Integration Enabled");

		Plugin languageUtils = Bukkit.getPluginManager().getPlugin("LangUtils");
		languageUtilsInstalled = languageUtils != null;
		if(languageUtilsInstalled) 
			info("  LanguageUtils Integration Enabled");
	}

	public TaskScheduler getScheduler() {
		return this.scheduler;
	}

	public String getResourcesString(Town town) {
		String productionAsString = TownyResourcesGovernmentMetaDataController.getDailyProduction(town);
		productionAsString = TownyResourcesMessagingUtil.adjustAmountsForTownLevelModifier(town, productionAsString);
		String finalDescription = TownyResourcesMessagingUtil.formatProductionStringForDynmapTownyDisplay(productionAsString);
		return finalDescription.isEmpty() ? "None" : finalDescription;
	}

	private static boolean isFoliaClassPresent() {
		try {
			Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}
}
