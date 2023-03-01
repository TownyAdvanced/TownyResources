package io.github.townyadvanced.townyresources.metadata;

import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.metadata.StringDataField;
import com.palmergames.bukkit.towny.utils.MetaDataUtil;

public class SurveyPlotMetaDataController {
	private static StringDataField discoveredResourcesSDF = 
			new StringDataField("townyresources_discoveredResources", ""); // e.g. OAK_LOG, SUGAR

	public static String getDiscovered(TownBlock townBlock) {
		return MetaDataUtil.getString(townBlock, discoveredResourcesSDF);
	}

	public static void setDiscovered(TownBlock townBlock, String resource) {
		MetaDataUtil.setString(townBlock, discoveredResourcesSDF, resource, true);
	}
}
