package io.github.townyadvanced.townyresources.util;

import java.util.Locale;
import java.util.Map;

import org.bukkit.block.Biome;
import org.jetbrains.annotations.Nullable;

import com.palmergames.adventure.text.Component;
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.TownBlockData;
import com.palmergames.bukkit.towny.object.TownBlockType;
import com.palmergames.bukkit.towny.object.TownBlockTypeHandler;
import com.palmergames.bukkit.towny.object.Translatable;
import com.palmergames.bukkit.towny.object.Translator;
import com.palmergames.bukkit.towny.object.metadata.BooleanDataField;
import com.palmergames.bukkit.towny.object.metadata.StringDataField;
import com.palmergames.bukkit.towny.utils.MetaDataUtil;
import com.palmergames.bukkit.towny.utils.TownyComponents;
import com.palmergames.util.StringMgmt;

import io.github.townyadvanced.townyresources.TownyResources;
import io.github.townyadvanced.townyresources.controllers.TownResourceOffersController;
import io.github.townyadvanced.townyresources.metadata.SurveyPlotMetaDataController;
import io.github.townyadvanced.townyresources.objects.ResourceOfferCategory;
import io.github.townyadvanced.townyresources.settings.TownyResourcesSettings;

public class SurveyPlotUtil {
	
	private static final String SURVEYPLOT_NAME = "surveysite";
	private static BooleanDataField surveyPlotUsed = new BooleanDataField("townyResources_SurveyPlotUsed", false);
	private static StringDataField surveyPlotBiome= new StringDataField("townyResources_SurveyPlotBiome");

	public static void registerSurveyPlotOnLoad(String mapKey, double cost) {
		TownBlockType surveyPlot = new TownBlockType(SURVEYPLOT_NAME, new TownBlockData() {
			@Override
			public String getMapKey() {
				return mapKey;
			}
			@Override
			public double getCost() {
				return cost;
			}
		});
		try {
			TownBlockTypeHandler.registerType(surveyPlot);
		} catch (TownyException e) {
			TownyResources.severe(e.getMessage());
		}
	}

	public static void registerSurveyPlot() {
		if (!TownyResourcesSettings.areSurveyPlotsEnabled()
			|| TownBlockTypeHandler.exists(SURVEYPLOT_NAME)) {
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
		if (!TownyResourcesSettings.areSurveyPlotsEnabled())
			return;
		if (!isSurveyPlot(townBlock.getType()))
			throw new TownyException(Translatable.of("townyresources.msg_err_youre_not_in_survey_plot"));
		if (isSurveyPlotAlreadyUsed(townBlock))
			throw new TownyException(Translatable.of("townyresources.msg_err_survey_plot_already_used"));
	}

	public static boolean isSurveyPlot(TownBlockType type) {
		return !TownyResourcesSettings.areSurveyPlotsEnabled() || type.equals(TownBlockTypeHandler.getTypeInternal(SURVEYPLOT_NAME));
	}

	public static boolean isSurveyPlotAlreadyUsed(TownBlock tb) {
		return !TownyResourcesSettings.areSurveyPlotsEnabled() || MetaDataUtil.getBoolean(tb, surveyPlotUsed);
	}

	public static void setSurveyPlotUsed(TownBlock tb) {
		if (!TownyResourcesSettings.areSurveyPlotsEnabled())
			return;
		MetaDataUtil.setBoolean(tb, surveyPlotUsed, true, true);
	}

	public static void setSurveyPlotBiome(TownBlock townBlock, Biome biome) {
		MetaDataUtil.setString(townBlock, surveyPlotBiome, biome.name(), true);
	}

	@Nullable
	public static Biome getSurveyPlotBiome(TownBlock townBlock) {
		String biomeName = MetaDataUtil.getString(townBlock, surveyPlotBiome);
		return biomeName.isEmpty() ? null : Biome.valueOf(biomeName);
	}
	
	public static String getSurveyPlotBiomeFormatted(TownBlock townBlock) {
		Biome biome = getSurveyPlotBiome(townBlock);
		if (biome == null)
			return "Unknown";
		return StringMgmt.capitalize(biome.name().toLowerCase(Locale.ROOT));
	}

	public static Component getStatusScreenComponent(TownBlock townBlock, Translator translator) {
		Component comp = Component.empty();
		comp = comp.append(TownyComponents.legacy(translator.of("townyresources.townblock.screen.header"))).appendNewline();
		comp = comp.append(TownyComponents.legacy(translator.of("townyresources.townblock.screen.plot_status")
				+ (isSurveyPlotAlreadyUsed(townBlock)
					? translator.of("townyresources.townblock.screen.plot_status_used")
					: translator.of("townyresources.townblock.screen.plot_status_unused"))));
		comp = comp.appendNewline();
		comp = comp.append(TownyComponents.legacy(translator.of("townyresources.townblock.screen.biome", getSurveyPlotBiomeFormatted(townBlock))));
		comp = comp.appendNewline();
		if (isSurveyPlotAlreadyUsed(townBlock)) {
			comp = comp.append(TownyComponents.legacy(translator.of("townyresources.townblock.screen.daily.production",
					getProductionAmount(townBlock),
					getProductionResource(townBlock)))).appendNewline();
			comp = comp.append(TownyComponents.legacy(translator.of("townyresources.townblock.screen.amount_disclaimer"))).appendNewline();
		}
		return comp;
	}
	
	public static int getProductionAmount(TownBlock townBlock) {
		Map<String, ResourceOfferCategory> allOffers = 
				TownResourceOffersController.getMaterialToResourceOfferCategoryMap();
		String mat = SurveyPlotMetaDataController.getDiscovered(townBlock);
		if (!allOffers.containsKey(mat))
			return 0;
		return allOffers.get(mat).getBaseAmountItems();
	}

	public static String getProductionResource(TownBlock tb) {
		return TownyResourcesMessagingUtil.formatMaterialNameForDisplay(SurveyPlotMetaDataController.getDiscovered(tb));
	}

	public static void initializeSurveyPlot(TownBlock townBlock, Resident resident) {
		Biome biome = BiomeUtil.getWorldCoordBiome(townBlock.getWorldCoord());
		setSurveyPlotBiome(townBlock, biome);
		String biomeName = getSurveyPlotBiomeFormatted(townBlock);
		TownyMessaging.sendMsg(resident, Translatable.of("townyresources.dominantbiome", biomeName));
	}
}
