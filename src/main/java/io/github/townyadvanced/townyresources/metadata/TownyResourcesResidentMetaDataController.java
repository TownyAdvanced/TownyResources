package io.github.townyadvanced.townyresources.metadata;

import com.gmail.goosius.siegewar.SiegeWar;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Government;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import io.github.townyadvanced.townyresources.TownyResources;
import io.github.townyadvanced.townyresources.objects.CategoryExtractionRecord;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 
 * @author Goosius
 *
 */
public class TownyResourcesResidentMetaDataController {


	@SuppressWarnings("unused")
	private TownyResources plugin;

	public TownyResourcesResidentMetaDataController(TownyResources plugin) {
		this.plugin = plugin;
	}

    private final static String
        extractionRecordMetadataKey = "townyresources_extractionRecord";  //e.g.   "Common Rocks-25,Wheat-64"

    public static String getExtractionRecord(Resident resident) {
        return MetaDataUtil.getSdf(resident, extractionRecordMetadataKey);
    }
    

    public static Map<Material, CategoryExtractionRecord> getExtractionRecord(Player player) {
        Resident resident = TownyUniverse.getInstance().getResident(player.getUniqueId());
        if(resident == null)
            return new HashMap<>();
        String recordAsString = MetaDataUtil.getSdf(resident, extractionRecordMetadataKey);;
        
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
    public static List<Material> getDiscoveredAsList(Town town) {
        String discoveredMaterialsString = getDiscovered(town);
        if(discoveredMaterialsString.isEmpty()) {
            return Collections.emptyList();
        } else {
            String[] discoveredMaterialsArray = discoveredMaterialsString.split(",");
            List<Material> result = new ArrayList<>();
            for(String discoveredMaterial: discoveredMaterialsArray) {
                result.add(Material.getMaterial(discoveredMaterial));
            }
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
    
    public static void setDiscovered(Government government, List<Material> discoveredResources) {
        //Convert materials list to single string
        StringBuilder metadataStringBuilder = new StringBuilder();
        for(int i= 0; i < discoveredResources.size();i++) {
            if(i !=0)
                metadataStringBuilder.append(", "); 
            metadataStringBuilder.append(discoveredResources.get(i).toString()); //TODO nice format pls
        }
        setDiscovered(government, metadataStringBuilder.toString());
    }

    public static void setDiscovered(Government government, String discovered) {
        MetaDataUtil.setSdf(government, discoveredMetadataKey, discovered);
    }
    
    public static String getDailyProduction(Government government) {
        return MetaDataUtil.getSdf(government, dailyProductionMetadataKey).replaceAll(" ","");
    }
    
    public static String getAvailableForCollection(Government government) {
        return MetaDataUtil.getSdf(government, availableForCollectionMetadataKey).replaceAll(" ","");
    }

    public static Map<Material, Integer> getDailyProductionAsMap(Government town) {
       return getResourceQuantitiesStringAsMap(getDailyProduction(town));
    }

    public static Map<Material, Integer> getAvailableForCollectionAsMap(Government town) {
       return getResourceQuantitiesStringAsMap(getAvailableForCollection(town));
    }

    private static Map<Material, Integer> getResourceQuantitiesStringAsMap(String resourceQuantitiesString) {
        Map<Material,Integer> result = new HashMap<>();
        if(!resourceQuantitiesString.isEmpty()) {
            String[] resourceQuantitiesArray = resourceQuantitiesString.split(",");
            String[] resourceQuantityPair;
            String resource;
            int amount;
            for(String resourceQuantityString: resourceQuantitiesArray) {
               resourceQuantityPair = resourceQuantityString.split("-");
               amount = Integer.parseInt(resourceQuantityPair[0]); 
               resource = resourceQuantityPair[1];
               result.put(Material.getMaterial(resource), amount);
            }        
        }
        return result;        
    }
    
    public static void setAvailableForCollection(Government government, Map<Material, Integer> availableForCollection) {
        setResourceQuantitiesString(government, availableForCollectionMetadataKey, availableForCollection);
    }
    
    public static void setDailyProduction(Government government, Map<Material, Integer> dailyProduction) {
        setResourceQuantitiesString(government, dailyProductionMetadataKey, dailyProduction);
    }
    
    private static void setResourceQuantitiesString(Government government, String metadataKey, Map<Material, Integer> resourceQuantitiesMap) {
        //Order map by descending values
        Map<Material, Integer> sortedResourceQuantitiesMap = resourceQuantitiesMap.entrySet().stream()
        .sorted(Comparator.comparingInt(e -> -e.getValue()))
        .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (a, b) -> { throw new AssertionError(); },
                LinkedHashMap::new
        ));

        //Create list
        List<String> resourceQuantitiesList = new ArrayList<>();
        for(Map.Entry<Material,Integer> resourceQuantity: sortedResourceQuantitiesMap.entrySet()) {
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
        MetaDataUtil.setSdf(government, metadataKey, resourceQuantitiesAsStringBuilder.toString());        
    }

    public static Map<Material, CategoryExtractionRecord> getPlayerExtractionRecord(Player player) {
        
        
    }
}