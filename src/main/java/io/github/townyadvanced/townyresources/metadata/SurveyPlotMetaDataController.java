package io.github.townyadvanced.townyresources.metadata;

import org.bukkit.block.Biome;

import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.metadata.BooleanDataField;
import com.palmergames.bukkit.towny.object.metadata.StringDataField;
import com.palmergames.bukkit.towny.utils.MetaDataUtil;

public class SurveyPlotMetaDataController {
	private static StringDataField discoveredResourcesSDF = new StringDataField("townyresources_discoveredResources", ""); // e.g. OAK_LOG, SUGAR
	private static BooleanDataField surveyPlotUsed = new BooleanDataField("townyResources_SurveyPlotUsed", false);
	private static StringDataField surveyPlotBiome= new StringDataField("townyResources_SurveyPlotBiome");


	public static String getDiscovered(TownBlock townBlock) {
		return MetaDataUtil.getString(townBlock, discoveredResourcesSDF);
	}

	public static void setDiscovered(TownBlock townBlock, String resource) {
		MetaDataUtil.setString(townBlock, discoveredResourcesSDF, resource, true);
	}

	public static Biome getBiome(TownBlock townBlock) {
		String biomeName = MetaDataUtil.getString(townBlock, surveyPlotBiome);
		return biomeName.isEmpty() ? null : Biome.valueOf(biomeName);
	}

	public static void setBiome(TownBlock townBlock, String biomeName) {
		MetaDataUtil.setString(townBlock, surveyPlotBiome, biomeName, true);
	}

	public static boolean isSurveyPlotUsed(TownBlock townBlock) {
		return MetaDataUtil.getBoolean(townBlock, surveyPlotUsed);
	}

	public static void setSurveyPlotUsed(TownBlock townBlock) {
		MetaDataUtil.setBoolean(townBlock, surveyPlotUsed, true, true);
	}

	public static void removeSurveyMetaData(TownBlock townBlock) {
		townBlock.removeMetaData(discoveredResourcesSDF.getKey());
		townBlock.removeMetaData(surveyPlotUsed.getKey());
		townBlock.removeMetaData(surveyPlotBiome.getKey());
		townBlock.save();
	}
}
