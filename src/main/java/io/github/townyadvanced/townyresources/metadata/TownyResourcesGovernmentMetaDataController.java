package io.github.townyadvanced.townyresources.metadata;

import com.gmail.goosius.siegewar.SiegeWar;
import com.palmergames.bukkit.towny.object.Government;
import org.bukkit.Material;

import java.util.List;

/**
 * 
 * @author Goosius
 *
 */
public class TownyResourcesGovernmentMetaDataController {

	@SuppressWarnings("unused")
	private SiegeWar plugin;

	public TownyResourcesGovernmentMetaDataController(SiegeWar plugin) {
		this.plugin = plugin;
	}

    private static String
        discoveredMetadataKey = "townyresources_discovered",  //e.g.   OAK_LOG, SUGAR
        dailyProductionMetadataKey = "townyresources_dailyproduction",  //e.g.   32-OAK_LOG, 32-SUGAR
        availableForCollectionMetadataKey = "townyresources_availableforcollection";  //e.g.  64-OAK_LOG, 64-SUGAR

    public static String getDiscovered(Government government) {
        return MetaDataUtil.getSdf(government, discoveredMetadataKey);
    }
    
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
        MetaDataUtil.setSdf(government, discoveredMetadataKey, discovered);
    }
    
    public static String getDailyProduction(Government government) {
        return MetaDataUtil.getSdf(government, dailyProductionMetadataKey);
    }

    public static void setDailyProduction(Government government, String dailyProduction) {
        MetaDataUtil.setSdf(government, dailyProductionMetadataKey, dailyProduction);
    }

    public static String getAvailableForCollection(Government government) {
        return MetaDataUtil.getSdf(government, availableForCollectionMetadataKey);
    }

    public static void setAvailableForCollection(Government government, String availableForCollection) {
        MetaDataUtil.setSdf(government, availableForCollectionMetadataKey, availableForCollection);
    }
}