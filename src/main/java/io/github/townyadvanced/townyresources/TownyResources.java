package io.github.townyadvanced.townyresources;

import com.gmail.goosius.siegewar.command.SiegeWarAdminCommand;
import com.gmail.goosius.siegewar.command.SiegeWarCommand;
import io.github.townyadvanced.townyresources.commands.TownyResourcesAdminCommand;
import io.github.townyadvanced.townyresources.commands.TownyResourcesCommand;
import io.github.townyadvanced.townyresources.listeners.TownyResourcesNationEventListener;
import io.github.townyadvanced.townyresources.listeners.TownyResourcesTownEventListener;
import io.github.townyadvanced.townyresources.settings.TownyResourcesSettings;
import io.github.townyadvanced.townyresources.settings.TownyResourcesTranslation;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

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
	
	private boolean loadAll() {
		try {
			printSickASCIIArt();
			TownyResourcesSettings.loadConfig(this.getDataFolder().getPath() + File.separator + "config.yml", getVersion());
			TownyResourcesTranslation.loadLanguage(this.getDataFolder().getPath() + File.separator , "english.yml");
			registerListeners();
			registerCommands();
		} catch (Exception e) {
            e.printStackTrace();
            severe("TownyResources failed to load! Disabling!");
            return false;
        }
		info("TownyResources loaded successfully.");		
		return true;
	}
	
	public static void info(String message) {
		plugin.getLogger().info(message);
	}
	
	public static void severe(String message) {
		plugin.getLogger().severe(message);
	}

	private void printSickASCIIArt() {
		System.out.println("    _________.__                      ");
		System.out.println("   /   _____/|__| ____   ____   ____  ");
		System.out.println("   \\_____  \\ |  |/ __ \\ / ___\\_/ __ \\ ");
		System.out.println("   /        \\|  \\  ___// /_/  >  ___/ ");
		System.out.println("  /_______  /|__|\\___  >___  / \\___  >");
		System.out.println("          \\/         \\/_____/      \\/ ");
		System.out.println("       __      __                        ");
		System.out.println("      /  \\    /  \\_____ _______          ");
		System.out.println("      \\   \\/\\/   /\\__  \\\\_  __ \\         ");
		System.out.println("       \\        /  / __ \\|  | \\/         ");
		System.out.println("        \\__/\\  /  (____  /__|            ");
		System.out.println("             \\/        \\/                ");
		System.out.println("          By Goosius         ");
		System.out.println("                                      ");
	}
	
	private void registerListeners() {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(new TownyResourcesTownEventListener(this), this);
		pm.registerEvents(new TownyResourcesNationEventListener(this), this);		
	}
	
	private void registerCommands() {
		getCommand("townyresouces").setExecutor(new TownyResourcesCommand());
		getCommand("townyresourcesadmin").setExecutor(new TownyResourcesAdminCommand());
	}

}
