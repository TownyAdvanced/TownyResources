package io.github.townyadvanced.townyresources.metadata;

import com.palmergames.bukkit.towny.object.Government;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.metadata.StringDataField;
import com.palmergames.bukkit.towny.utils.MetaDataUtil;

import io.github.townyadvanced.townyresources.TownyResources;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 
 * @author Goosius
 *
 */
public class TownyResourcesGovernmentMetaDataController {

	@SuppressWarnings("unused")
	private TownyResources plugin;

	public TownyResourcesGovernmentMetaDataController(TownyResources plugin) {
		this.plugin = plugin;
	}

    private static String
        discoveredMetadataKey = "townyresources_discovered",  //e.g.   OAK_LOG, SUGAR
        dailyProductionMetadataKey = "townyresources_dailyproduction",  //e.g.   32-OAK_LOG, 32-SUGAR
        availableForCollectionMetadataKey = "townyresources_availableforcollection";  //e.g.  64-OAK_LOG, 64-SUGAR
	private static StringDataField discoveredSDF = new StringDataField(discoveredMetadataKey, "");
	private static StringDataField dailyProductionSDF = new StringDataField(dailyProductionMetadataKey, "");
	private static StringDataField availableForCollectionSDF = new StringDataField(availableForCollectionMetadataKey, "");

    public static String getDiscovered(Government government) {
        return MetaDataUtil.getString(government, discoveredSDF).replaceAll(" ","");
    }
    
    
     /**
     * Get the discovered resources of a town
     * 
     * Note that the order is important
     * 0 - level 1 resource
     * 1 - level 2 resource
     * etc.
     * 
     * @param town the town
     * @return the town's discovered resources, as an IMMUTABLE list
     */
    public static List<String> getDiscoveredAsList(Town town) {
        String discoveredMaterialsString = getDiscovered(town);
        if(discoveredMaterialsString.isEmpty()) {
            return Collections.emptyList();
        } else {
            String[] discoveredMaterialsArray = discoveredMaterialsString.split(",");
            List<String> result = new ArrayList<>();
            result.addAll(Arrays.asList(discoveredMaterialsArray));
            return result;
        }
    }
       
       
       /* 
    /**
     * Get the discovered resources of a town
     * 
     * Note that the order is important
     * 0 - level 1 resource
     * 1 - level 2 resource
     * etc.
     * 
     * @param town the town
     * @return the town's discovered resources
    */
    /*
    public static List<ResourceQuantity> getDiscoveredResources(Town town) {
        List<ResourceQuantity> result = new ArrayList<>();
        String resourcesString = getDiscovered(town);
        String resouces;
        int quantity;
        if(!resourcesString.isEmpty()) {
            String[] resourcesQuantitiesArray = resourcesString.split(",");
            for(String resourceQuantityPair: resourcesQuantitiesArray) {
                result.add(new ResourceQuantity(res
            }
            
            return new ArrayList<>();
        } else {
            
            return Arrays.asList(resourcesArray);        
        }
        return result;
    }

    */
    
    public static void setDiscovered(Government government, List<String> discoveredResources) {
        //Convert materials list to single string
        StringBuilder metadataStringBuilder = new StringBuilder();
        for(int i= 0; i < discoveredResources.size();i++) {
            if(i !=0)
                metadataStringBuilder.append(", "); 
            metadataStringBuilder.append(discoveredResources.get(i));
        }
        setDiscovered(government, metadataStringBuilder.toString());
    }

    public static void setDiscovered(Government government, String discovered) {
        MetaDataUtil.setString(government, discoveredSDF, discovered, false);
    }
    
    public static String getDailyProduction(Government government) {
        return MetaDataUtil.getString(government, dailyProductionSDF).replaceAll(" ","");
    }
    
    public static String getAvailableForCollection(Government government) {
        return MetaDataUtil.getString(government, availableForCollectionSDF).replaceAll(" ","");
    }

    public static Map<String, Integer> getDailyProductionAsMap(Government town) {
       return getResourceQuantitiesStringAsMap(getDailyProduction(town));
    }

    public static Map<String, Integer> getAvailableForCollectionAsMap(Government town) {
       return getResourceQuantitiesStringAsMap(getAvailableForCollection(town));
    }

    private static Map<String, Integer> getResourceQuantitiesStringAsMap(String resourceQuantitiesString) {
        Map<String,Integer> result = new HashMap<>();
        if(!resourceQuantitiesString.isEmpty()) {
            String[] resourceQuantitiesArray = resourceQuantitiesString.split(",");
            String[] resourceQuantityPair;
            String resource;
            int amount;
            for(String resourceQuantityString: resourceQuantitiesArray) {
               resourceQuantityPair = resourceQuantityString.split("-");
               amount = Integer.parseInt(resourceQuantityPair[0]); 
               resource = resourceQuantityPair[1];
               result.put(resource, amount);
            }        
        }
        return result;        
    }
    
    public static void setAvailableForCollection(Government government, Map<String, Integer> availableForCollection) {
        setResourceQuantitiesString(government, availableForCollectionMetadataKey, availableForCollection);
    }
    
    public static void setDailyProduction(Government government, Map<String, Integer> dailyProduction) {
        setResourceQuantitiesString(government, dailyProductionMetadataKey, dailyProduction);
    }
    
    private static void setResourceQuantitiesString(Government government, String metadataKey, Map<String, Integer> resourceQuantitiesMap) {
        //Order map by descending values
        Map<String, Integer> sortedResourceQuantitiesMap = resourceQuantitiesMap.entrySet().stream()
        .sorted(Comparator.comparingInt(e -> -e.getValue()))
        .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (a, b) -> { throw new AssertionError(); },
                LinkedHashMap::new
        ));

        //Create list
        List<String> resourceQuantitiesList = new ArrayList<>();
        for(Map.Entry<String,Integer> resourceQuantity: sortedResourceQuantitiesMap.entrySet()) {
            resourceQuantitiesList.add(resourceQuantity.getValue() + "-" + resourceQuantity.getKey());
        }
        setResourceQuantitiesString(government, metadataKey, resourceQuantitiesList);
    }

    private static void setResourceQuantitiesString(Government government, String metadataKey, List<String> resourceQuantitiesList) {        
        //Build string
        StringBuilder resourceQuantitiesAsStringBuilder = new StringBuilder();
        boolean firstEntry = true;
        for(String resourceQuantity: resourceQuantitiesList) {
            if(firstEntry) {
                firstEntry = false;
            } else {
                resourceQuantitiesAsStringBuilder.append(", ");                
            }
            resourceQuantitiesAsStringBuilder.append(resourceQuantity);
        }
        //Set the string into metadata
        StringDataField meta = metadataKey.equals(availableForCollectionMetadataKey) ? availableForCollectionSDF : dailyProductionSDF;
        MetaDataUtil.setString(government, meta, resourceQuantitiesAsStringBuilder.toString(), false);
    }
}