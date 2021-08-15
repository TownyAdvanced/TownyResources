package io.github.townyadvanced.townyresources.metadata;

import com.gmail.goosius.siegewar.SiegeWar;
import com.palmergames.bukkit.towny.object.Government;

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
        discoveredMetadataKey = "townyresources_discovered",  //only used for towns
        dailyProductionMetadataKey = "townyresources_dailyproduction",
        availableForCollectionMetadataKey = "townyresources_availableforcollection";

    public static String getDiscovered(Government government) {
        return MetaDataUtil.getSdf(government, discoveredMetadataKey);
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