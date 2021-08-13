package io.github.townyadvanced.blankplugin;

import java.io.File;
import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;

import io.github.townyadvanced.blankplugin.settings.Settings;
import io.github.townyadvanced.blankplugin.settings.Translation;

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
		} catch (IOException e) {
            e.printStackTrace();
            severe("Config.yml failed to load! Disabling!");
            return false;
        }
		info("Config.yml loaded successfully.");		
		return true;
	}
	
	public static void info(String message) {
		plugin.getLogger().info(message);
	}
	
	public static void severe(String message) {
		plugin.getLogger().severe(message);
	}
}
