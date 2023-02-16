package io.github.townyadvanced.townyresources.util;

import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.TownBlockData;
import com.palmergames.bukkit.towny.object.TownBlockType;
import com.palmergames.bukkit.towny.object.TownBlockTypeHandler;
import com.palmergames.bukkit.towny.object.Translatable;
import com.palmergames.bukkit.towny.object.metadata.BooleanDataField;
import com.palmergames.bukkit.towny.utils.MetaDataUtil;

import io.github.townyadvanced.townyresources.TownyResources;
import io.github.townyadvanced.townyresources.settings.TownyResourcesSettings;

public class SurveyPlotUtil {
	
	private static final String SURVEYPLOT_NAME = "surveyplot";
	private static BooleanDataField surveyPlotUsed = new BooleanDataField("townyResources_SurveyPlotUsed", false);

	public static void registerSurveyPlot() {
		if (TownBlockTypeHandler.exists(SURVEYPLOT_NAME)) {
			return;
		}


		TownBlockType surveyPlot = new TownBlockType(SURVEYPLOT_NAME, new TownBlockData() {
			@Override
			public String getMapKey() {
				return TownyResourcesSettings.getSurveyPlotASCIIMapKey();
			}
			@Override
			public double getCost() {
				return TownyResourcesSettings.getSurveyPlotCost();
			}
		});
		try {
			TownBlockTypeHandler.registerType(surveyPlot);
		} catch (TownyException e) {
			TownyResources.severe(e.getMessage());
		}
	}

	public static void verifyPlayerInUnusedSurveyPlot(TownBlock townBlock) throws TownyException {
		if (isSurveyPlot(townBlock.getType()))
			throw new TownyException(Translatable.of("townyresources.msg_err_youre_not_in_survey_plot"));
		if (isSurveyPlotAlreadyUsed(townBlock))
			throw new TownyException(Translatable.of("townyresources.msg_err_survey_plot_already_used"));
	}

	public static boolean isSurveyPlot(TownBlockType type) {
		return type.equals(TownBlockTypeHandler.getTypeInternal(SURVEYPLOT_NAME));
	}

	public static boolean isSurveyPlotAlreadyUsed(TownBlock tb) {
		return MetaDataUtil.getBoolean(tb, surveyPlotUsed);
	}

	public static void setSurveyPlotUsed(TownBlock tb) {
		MetaDataUtil.setBoolean(tb, surveyPlotUsed, true, true);
	}

	
}
