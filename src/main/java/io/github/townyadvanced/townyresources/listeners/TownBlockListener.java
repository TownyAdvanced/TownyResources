package io.github.townyadvanced.townyresources.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.palmergames.bukkit.towny.event.ChunkNotificationEvent;
import com.palmergames.bukkit.towny.event.plot.PlayerChangePlotTypeEvent;
import com.palmergames.bukkit.towny.event.TownBlockTypeRegisterEvent;
import com.palmergames.bukkit.towny.event.statusscreen.TownBlockStatusScreenEvent;
import com.palmergames.bukkit.towny.event.town.TownPreUnclaimEvent;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.TownBlockType;
import com.palmergames.bukkit.towny.object.Translatable;
import com.palmergames.bukkit.towny.object.Translator;

import io.github.townyadvanced.townyresources.util.SurveyPlotUtil;

public class TownBlockListener implements Listener {

	/* Handles injecting SurveyPlot into Towny when Towny reloads. */
	@EventHandler
	public void onTownyLoadTownBlockTypes(TownBlockTypeRegisterEvent event) {
		SurveyPlotUtil.registerSurveyPlot();
	}

	/* Prevents unclaiming of survey plots. */
	@EventHandler
	public void onTownTriesUnclaim(TownPreUnclaimEvent event) {
		if (!SurveyPlotUtil.isSurveyPlot(event.getTownBlock().getType()))
			return;
		if (SurveyPlotUtil.isSurveyPlotAlreadyUsed(event.getTownBlock())) {
			event.setCancelled(true);
			event.setCancelMessage(Translatable.of("townyresources.msg_err_you_cannot_unclaim_survey_plot").defaultLocale());
		}
	}

	/* Adds Blurb to chunk notification showing whether a survey plot is unclaimed. */
	@EventHandler
	public void onChunkNotification(ChunkNotificationEvent event) {
		TownBlock tb = event.getToCoord().getTownBlockOrNull();
		if (tb == null || !SurveyPlotUtil.isSurveyPlot(tb.getType()))
			return;

		String message = event.getMessage();
		message += Translatable.of(SurveyPlotUtil.isSurveyPlotAlreadyUsed(tb) ? "townyresources.notification.surveyed"
				: "townyresources.notification.unsurveyed").forLocale(event.getPlayer());
		if (SurveyPlotUtil.isSurveyPlotAlreadyUsed(tb))
			message += Translatable.of("townyresources.notification.surveyed_resource", SurveyPlotUtil.getProductionResource(tb));
		event.setMessage(message);
	}

	/* Initializes a SurveyPlot on changing TownBlockType. */
	@EventHandler
	public void onTownBlockSetToSurveyPlot(PlayerChangePlotTypeEvent event) {
		TownBlockType type = event.getNewType();
		if (!SurveyPlotUtil.isSurveyPlot(type)) // Not becoming a SurveyPlot
			return;
		if (SurveyPlotUtil.isSurveyPlot(event.getOldType())) // Already was a SurveyPlot, probably shouldn't ever happen.
			return;
		SurveyPlotUtil.initializeSurveyPlot(event.getTownBlock(), event.getResident());
	}

	/* Adds survey plot details to SurveyPlots */
	@EventHandler
	public void onTownBlockStatusEvent(TownBlockStatusScreenEvent event) {
		if (!SurveyPlotUtil.isSurveyPlot(event.getTownBlock().getType()))
			return;
		Translator translator = Translator.locale(event.getCommandSender());
		event.getStatusScreen().addComponentOf("surveyComp", SurveyPlotUtil.getStatusScreenComponent(event.getTownBlock(), translator));
	}
	
}
