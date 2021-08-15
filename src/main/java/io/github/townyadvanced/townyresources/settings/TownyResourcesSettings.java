package io.github.townyadvanced.townyresources.settings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.github.townyadvanced.townyresources.TownyResources;
import io.github.townyadvanced.townyresources.util.FileMgmt;

public class TownyResourcesSettings {
	private static CommentedConfiguration config, newConfig;
	
	public static boolean isEnabled() {
		return getBoolean(TownyResourcesConfigNodes.ENABLED);
	}

    public static List<Integer> getSurveyCostsPerResourceLevel() {
	    return getIntegerList(TownyResourcesConfigNodes.TOWN_RESOURCES_SURVEY_COST_PER_RESOURCE_LEVEL);
	}

    public static List<Integer> getSurveyNumTownblocksRequirementsPerResourceLevel() {
    	return getIntegerList(TownyResourcesConfigNodes.TOWN_RESOURCES_NUM_TOWNBLOCKS_REQUIREMENT_PER_RESOURCE_LEVEL);
	}
		
    public static List<String> getOffersOres() {
    	return getStringList(TownyResourcesConfigNodes.TOWN_RESOURCES_OFFERS_ORES);
	}

    public static List<String> getOffersTrees() {
    	return getStringList(TownyResourcesConfigNodes.TOWN_RESOURCES_OFFERS_TREES);
	}
	
    public static List<String> getOffersCrops() {
    	return getStringList(TownyResourcesConfigNodes.TOWN_RESOURCES_OFFERS_CROPS);
	}

    public static List<String> getOffersAnimals() {
    	return getStringList(TownyResourcesConfigNodes.TOWN_RESOURCES_OFFERS_ANIMALS);
	}

    public static List<String> getOffersMonsters() {
    	return getStringList(TownyResourcesConfigNodes.TOWN_RESOURCES_OFFERS_MONSTERS);
	}

	public static void loadConfig(String filepath, String version) throws IOException {
		if (FileMgmt.checkOrCreateFile(filepath)) {
			File file = new File(filepath);

			// read the config.yml into memory
			config = new CommentedConfiguration(file);
			if (!config.load())
				sendError("Failed to load Config!");

			setDefaults(version, file);
			config.save();
		}
	}

	public static void addComment(String root, String... comments) {
		newConfig.addComment(root.toLowerCase(), comments);
	}

	private static void setNewProperty(String root, Object value) {
		if (value == null) {
			value = "";
		}
		newConfig.set(root.toLowerCase(), value.toString());
	}

	@SuppressWarnings("unused")
	private static void setProperty(String root, Object value) {
		config.set(root.toLowerCase(), value.toString());
	}
	
	/**
	 * Builds a new config reading old config data.
	 */
	private static void setDefaults(String version, File file) {
		newConfig = new CommentedConfiguration(file);
		newConfig.load();

		for (TownyResourcesConfigNodes root : TownyResourcesConfigNodes.values()) {
			if (root.getComments().length > 0)
				addComment(root.getRoot(), root.getComments());
			if (root.getRoot() == TownyResourcesConfigNodes.VERSION.getRoot())
				setNewProperty(root.getRoot(), version);
			else
				setNewProperty(root.getRoot(), (config.get(root.getRoot().toLowerCase()) != null) ? config.get(root.getRoot().toLowerCase()) : root.getDefault());
		}

		config = newConfig;
		newConfig = null;
	}
	
	public static String getString(String root, String def) {
		String data = config.getString(root.toLowerCase(), def);
		if (data == null) {
			sendError(root.toLowerCase() + " from config.yml");
			return "";
		}
		return data;
	}

	private static void sendError(String msg) {
		TownyResources.severe("Error could not read " + msg);
	}
	
	public static boolean getBoolean(TownyResourcesConfigNodes node) {
		return Boolean.parseBoolean(config.getString(node.getRoot().toLowerCase(), node.getDefault()));
	}

	public static double getDouble(TownyResourcesConfigNodes node) {
		try {
			return Double.parseDouble(config.getString(node.getRoot().toLowerCase(), node.getDefault()).trim());
		} catch (NumberFormatException e) {
			sendError(node.getRoot().toLowerCase() + " from config.yml");
			return 0.0;
		}
	}

	public static int getInt(TownyResourcesConfigNodes node) {
		try {
			return Integer.parseInt(config.getString(node.getRoot().toLowerCase(), node.getDefault()).trim());
		} catch (NumberFormatException e) {
			sendError(node.getRoot().toLowerCase() + " from config.yml");
			return 0;
		}
	}

	public static String getString(TownyResourcesConfigNodes node) {
		return config.getString(node.getRoot().toLowerCase(), node.getDefault());
	}
	
	public static List<Integer> getIntegerList(TownyResourcesConfigNodes configEntry) {
		String configAsString = getString(configEntry);
		if(configAsString.isEmpty()) {
            return new ArrayList<>();
		} else {
			configAsString = configAsString.replaceAll(" ","");
			List<Integer> result = new ArrayList<>();
			for(String configValue: configAsString.split(",")) {
				result.add(Integer.parseInt(configValue));
			}
			return result;
		}    	
	}
	
	public static List<String> getStringList(TownyResourcesConfigNodes configEntry) {
		String configAsString = getString(configEntry);
		if(configAsString.isEmpty()) {
            return new ArrayList<>();
		} else {
			configAsString = configAsString.replaceAll(" ","");
			String[] configAsArray = configAsString.split(",");
			return Arrays.asList(configAsArray);
		}    	
	}

	public static boolean areTownResourcesEnabled() {
		return getBoolean(TownyResourcesConfigNodes.TOWN_RESOURCES_ENABLED);
	}
}
