package io.github.townyadvanced.townyresources.settings;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import io.github.townyadvanced.townyresources.TownyResources;
import io.github.townyadvanced.townyresources.objects.ResourceExtractionCategory;
import io.github.townyadvanced.townyresources.objects.ResourceOffer;
import io.github.townyadvanced.townyresources.util.FileMgmt;
import org.bukkit.Material;

public class TownyResourcesSettings {
	private static CommentedConfiguration config, newConfig;
	private static int sumOfAllOfferDiscoveryProbabilityWeights = 0;  //Used when getting the resource offers
	    
	public static boolean isEnabled() {
		return getBoolean(TownyResourcesConfigNodes.ENABLED);
	}

    public static List<Integer> getSurveyCostsPerResourceLevel() {
	    return getIntegerList(TownyResourcesConfigNodes.TOWN_RESOURCES_SURVEYS_COST_PER_RESOURCE_LEVEL);
	}

    public static List<Integer> getSurveyNumTownblocksRequirementsPerResourceLevel() {
    	return getIntegerList(TownyResourcesConfigNodes.TOWN_RESOURCES_SURVEYS_NUM_TOWNBLOCKS_REQUIREMENT_PER_RESOURCE_LEVEL);
	}
		
	public static List<Integer> getProductionPercentagesPerResourceLevel() {
		return getIntegerList(TownyResourcesConfigNodes.TOWN_RESOURCES_PRODUCTION_PRODUCTIVITY_PERCENTAGE_PER_RESOURCE_LEVEL);
	}
	
	public static List<Integer> getProductionTownLevelRequirementPerResourceLevel() {
		return getIntegerList(TownyResourcesConfigNodes.TOWN_RESOURCES_PRODUCTION_TOWN_LEVEL_REQUIREMENT_PER_RESOURCE_LEVEL);
	}
		
	public static double getTownResourcesProductionNationTaxNormalized() {
		return getDouble(TownyResourcesConfigNodes.TOWN_RESOURCES_PRODUCTION_NATION_TAX_PERCENTAGE) / 100;
	}	
	
	public static int getSumOfAllOfferDiscoveryProbabilityWeights() {
		return sumOfAllOfferDiscoveryProbabilityWeights;
	}
		
	public static Map<String, ResourceOffer> getAllResourceOffers() {
		return getResourceOffers(Collections.emptyList());
    }

	public static Map<String, ResourceOffer> getResourceOffers(List<String> materialsToExclude) {
        sumOfAllOfferDiscoveryProbabilityWeights = 0;
        Map<String, ResourceOffer> resourceOffers = new HashMap<>();
        //resourceOffers.putAll(getOffersInCategory("ores", getOffersOres(), materialsToExclude));
        //resourceOffers.putAll(getOffersInCategory("trees", getOffersTrees(), materialsToExclude));
        //resourceOffers.putAll(getOffersInCategory("crops", getOffersCrops(), materialsToExclude));
        //resourceOffers.putAll(getOffersInCategory("animals", getOffersAnimals(), materialsToExclude));
        //resourceOffers.putAll(getOffersInCategory("monsters", getOffersMonsters(), materialsToExclude));   
        return resourceOffers;
	}

    private static Map<String, ResourceOffer> getOffersInCategory(String offersCategory, List<String> offersList, List<String> materialsToExclude) {
        Map<String, ResourceOffer> result = new HashMap<>();
        String[] offerAsArray;
        String offerMaterial;
        int offerBaseAmount;
        int offerDiscoveryProbabilityWeight;
        int offerDiscoveryId;
        ResourceOffer newResourceOffer;
        
        for(String offer: offersList) {
        	offerAsArray = offer.split("-");
            offerMaterial = offerAsArray[0];
            if(materialsToExclude.contains(offerMaterial))
	        	continue;      
            offerBaseAmount = (int)((Double.parseDouble(offerAsArray[1]) * 64) + 0.5);
            offerDiscoveryProbabilityWeight = Integer.parseInt(offerAsArray[2]);
            offerDiscoveryId = sumOfAllOfferDiscoveryProbabilityWeights;
            newResourceOffer = new ResourceOffer(offersCategory, offerMaterial, offerBaseAmount, offerDiscoveryProbabilityWeight, offerDiscoveryId);
            result.put(offerMaterial, newResourceOffer);                
            sumOfAllOfferDiscoveryProbabilityWeights += offerDiscoveryProbabilityWeight; 
        }
        return result;
    }

	/**
	 * Get all resource extraction categories
	 * 
	 * @return a list of all resource extraction categories
	 * @throws TownyException a towny exception
	 */
	public static List<ResourceExtractionCategory> getResourceExtractionCategories() throws TownyException{
		List<ResourceExtractionCategory> result = new ArrayList<>();
		boolean problemLoadingExtractionCategories = false;

		String categoriesAsString = getString(TownyResourcesConfigNodes.RESOURCE_EXTRACTION_LIMITS_CATEGORIES);

		//TownyResources.info("Cat string: "+ categoriesAsString);	
		
		if(!categoriesAsString.isEmpty()) {		
			Pattern pattern = Pattern.compile("\\{([^}]+)}", Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(categoriesAsString);
			String categoryAsString;
			String[] categoryAsArray;
			String categoryName;
			double categoryExtractionLimitStacks;
			int categoryExtractionLimitItems;
			Material material;
			List<Material> materialsInCategory = new ArrayList<>();
			ResourceExtractionCategory resourceExtractionCategory;
			
			while (matcher.find()) {
				//Read one resource extraction category
				categoryAsString = matcher.group(1);
				   
				categoryAsArray = categoryAsString.split(",");
				if(categoryAsArray.length < 2) {
					TownyResources.severe("Bad configuration for extraction category: " + categoryAsString);
					problemLoadingExtractionCategories = true;
					continue;
				}
				
				//Read name
				categoryName = categoryAsArray[0].trim();
				
				//Read limit
				categoryExtractionLimitStacks = Double.parseDouble(categoryAsArray[1].trim());
				categoryExtractionLimitItems = (int)((categoryExtractionLimitStacks * 64) + 0.5);
				
				//Read Materials
				materialsInCategory = new ArrayList<>();
				for(int i = 2; i < categoryAsArray.length; i++) {
					material = Material.getMaterial(categoryAsArray[i].trim());
					if(material == null) {
						TownyResources.severe("Unknown material in extraction category. Category: " + categoryName + ". Material: " + categoryAsArray[i]);
						problemLoadingExtractionCategories = true;
						continue;
					}
					materialsInCategory.add(material);
				}
				
				//Construct ResourceExtractionCategory object
				resourceExtractionCategory = new ResourceExtractionCategory(categoryName, categoryExtractionLimitItems, materialsInCategory);
				
				//Add to result
				result.add(resourceExtractionCategory);
			}		
		}

		if(problemLoadingExtractionCategories) {
			throw new TownyException("Problem Loading Extraction Categories");
		} else {
			return result;
		}
	}

	public static void loadConfig(String filepath, String version) throws TownyException{
		if (FileMgmt.checkOrCreateFile(filepath)) {
			File file = new File(filepath);

			// read the config.yml into memory
			config = new CommentedConfiguration(file);
			if (!config.load())
				throw new TownyException("Failed to load Config!");

			setDefaults(version, file);
			config.save();
			TownyResources.info("Config Loaded");
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

	/**
     * Return an IMMUTABLE list of integers
	 */
	public static List<Integer> getIntegerList(TownyResourcesConfigNodes configEntry) {
		String configAsString = getString(configEntry);
		if(configAsString.isEmpty()) {
            return Collections.emptyList();
		} else {
			configAsString = configAsString.replaceAll(" ","");
			List<Integer> result = new ArrayList<>();
			for(String configValue: configAsString.split(",")) {
				result.add(Integer.parseInt(configValue));
			}
			return result;
		}    	
	}
	
	/**
     * Return an IMMUTABLE list of strings
	 */
	public static List<String> getStringList(TownyResourcesConfigNodes configEntry) {
		String configAsString = getString(configEntry);
		if(configAsString.isEmpty()) {
            return Collections.emptyList();
		} else {
			configAsString = configAsString.replaceAll(" ","");
			String[] configAsArray = configAsString.split(",");
			return Arrays.asList(configAsArray);
		}    	
	}

	public static boolean areSurveysEnabled() {
		return getBoolean(TownyResourcesConfigNodes.TOWN_RESOURCES_SURVEYS_ENABLED);
	}

	public static int getStorageLimitModifier() {
		return getInt(TownyResourcesConfigNodes.TOWN_RESOURCES_PRODUCTION_STORAGE_LIMIT_MODIFIER);
	}

	public static List<Double> getNormalizedProductionBonusesPerResourceLevel() {
		List<Integer> percentageBonuses = getProductionPercentagesPerResourceLevel();
		List<Double> normalizedBonuses = new ArrayList<>();
		for(Integer percentageBonus: percentageBonuses) {
			normalizedBonuses.add((double)percentageBonus / 100);
		}
		return normalizedBonuses;
	}
}
