package io.github.townyadvanced.townyresources.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.palmergames.bukkit.towny.event.ChunkNotificationEvent;
import com.palmergames.bukkit.towny.event.TownBlockTypeRegisterEvent;
import com.palmergames.bukkit.towny.event.town.TownPreUnclaimEvent;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.Translatable;
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
		event.setCancelled(true);
		event.setCancelMessage(
				Translatable.of("townyresources.msg_err_you_cannot_unclaim_survey_plot").defaultLocale());
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
		event.setMessage(message);
	}
}
