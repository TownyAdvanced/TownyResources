package io.github.townyadvanced.townyresources.settings;


import org.bukkit.configuration.InvalidConfigurationException;

import com.palmergames.bukkit.config.CommentedConfiguration;
import com.palmergames.bukkit.util.Colors;
import com.palmergames.util.StringMgmt;

import io.github.townyadvanced.townyresources.TownyResources;
import io.github.townyadvanced.townyresources.util.FileMgmt;

import java.io.File;
import java.io.IOException;

/**
 * A convenience object to facilitate translation. 
 */
public final class TownyResourcesTranslation {
	
	public static CommentedConfiguration language;

	// This will read the language entry in the config.yml to attempt to load
	// custom languages
	// if the file is not found it will load the default from resource
	public static void loadLanguage(String filepath, String defaultRes) throws IOException {

		String res = TownyResourcesSettings.getString(TownyResourcesConfigNodes.LANGUAGE.getRoot(), defaultRes);
		String fullPath = filepath + File.separator + res;
		File file = FileMgmt.unpackResourceFile(fullPath, res, defaultRes);

		// read the (language).yml into memory
		language = new CommentedConfiguration(file.toPath());
		language.load();
		CommentedConfiguration newLanguage = new CommentedConfiguration(file.toPath());
		
		try {
			newLanguage.loadFromString(FileMgmt.convertStreamToString("/" + res));
		} catch (IOException e) {
			TownyResources.info("Lang: Custom language file detected, not updating.");
			TownyResources.info("Lang: " + res + " v" + TownyResourcesTranslation.of("version") + " loaded.");
			return;
		} catch (InvalidConfigurationException e) {
			TownyResources.severe("Invalid Configuration in language file detected.");
		}
		
		String resVersion = newLanguage.getString("version");
		String langVersion = TownyResourcesTranslation.of("version");

		if (!langVersion.equalsIgnoreCase(resVersion)) {
			language = newLanguage;
			TownyResources.info("Lang: Language file replaced with updated version.");
			FileMgmt.stringToFile(FileMgmt.convertStreamToString("/" + res), file);
		}
		TownyResources.info("Lang: " + res + " v" + TownyResourcesTranslation.of("version") + " loaded.");
	}

	private static String parseSingleLineString(String str) {
		return Colors.translateColorCodes(str);
	}
	
	/**
	 * Translates give key into its respective language. 
	 * 
	 * @param key The language key.
	 * @return The localized string.
	 */
	public static String of(String key) {
		String data = language.getString(key.toLowerCase());

		if (data == null) {
			TownyResources.severe("Error could not read " + key.toLowerCase() + " from " + TownyResourcesSettings.getString(TownyResourcesConfigNodes.LANGUAGE));
			return "";
		}
		return StringMgmt.translateHexColors(parseSingleLineString(data));
	}

	public static boolean hasKey(String key) {
		return language.contains(key.toLowerCase());
	}

	/**
	 * Translates give key into its respective language. 
	 *
	 * @param key The language key.
	 * @param args The arguments to format the localized string.   
	 * @return The localized string.
	 */
	public static String of(String key, Object... args) {
		return String.format(of(key), args);
	}

	private TownyResourcesTranslation() {}
}