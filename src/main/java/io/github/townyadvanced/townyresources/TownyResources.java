package io.github.townyadvanced.townyresources;

import io.github.townyadvanced.townyresources.listeners.TownyResourcesNationEventListener;
import io.github.townyadvanced.townyresources.listeners.TownyResourcesTownEventListener;
import io.github.townyadvanced.townyresources.settings.Settings;
import io.github.townyadvanced.townyresources.settings.Translation;
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
    	
        if (!loadSettings())
        	onDisable();

    }
    
	public String getVersion() {
		return this.getDescription().getVersion();
	}
	
	public static TownyResources getPlugin() {
		return plugin;
	}
	
	public static String getPrefix() {
		return Translation.language != null ? Translation.of("plugin_prefix") : "[" + plugin.getName() + "]";
	}
	
	private boolean loadSettings() {
		try {
			Settings.loadConfig(this.getDataFolder().getPath() + File.separator + "config.yml", getVersion());
			Translation.loadLanguage(this.getDataFolder().getPath() + File.separator , "english.yml");
			registerListeners();
		} catch (IOException e) {
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
	
	private void registerListeners() {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(new TownyResourcesTownEventListener(this), this);
		pm.registerEvents(new TownyResourcesNationEventListener(this), this);		
	}
}
