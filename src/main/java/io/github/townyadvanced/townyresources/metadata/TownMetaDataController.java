package io.github.townyadvanced.townyresources.metadata;

import com.gmail.goosius.siegewar.SiegeWar;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.metadata.BooleanDataField;
import com.palmergames.bukkit.towny.object.metadata.IntegerDataField;
import com.palmergames.bukkit.towny.object.metadata.LongDataField;
import com.palmergames.bukkit.towny.object.metadata.StringDataField;
import org.jetbrains.annotations.Nullable;

/**
 * 
 * @author Goosius
 *
 */
public class TownMetaDataController {

	@SuppressWarnings("unused")
	private SiegeWar plugin;

	public TownMetaDataController(SiegeWar plugin) {
		this.plugin = plugin;
	}

    private static String
        dailyProductionMetadataKey = "townyresouces_dailyproduction",
        availableforcollectionMetadataKey = "townyresouces_availableforcollection";
        
        
    public static int getDailyProduction(Town town) {
        return MetaDataUtil.getSdf(town, dailyProductionMetadataKey);
    }

    public static void setDailyProduction(Town town, String dailyProduction) {
        setSdf(town, dailyProductionMetadataKey, dailyProduction);
    }

        
        
        
        
	private static StringDataField resourcesDailyProduction = new StringDataField("townyresouces_dailyproduction", "");
	private static StringDataField resourcesAvailableForCollection = new StringDataField("townyresouces_availableforcollection", "");
	
	public static void setPrePeacefulOccupierUUID(Town town, String uuid) {
		StringDataField sdf = (StringDataField) prePeacefulOccupierUUID.clone();
		if (town.hasMeta(sdf.getKey()))
			MetaDataUtil.setString(town, sdf, uuid);
		else
			town.addMetaData(new StringDataField("siegewar_prePeacefulOccupierUUID", uuid));
	}

	public static void removePrePeacefulOccupierUUID(Town town) {
		StringDataField sdf = (StringDataField) prePeacefulOccupierUUID.clone();
		if (town.hasMeta(sdf.getKey()))
			town.removeMetaData(sdf);
	}
}
