package io.github.townyadvanced.townyresources.listeners;

import java.io.File;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import com.palmergames.bukkit.config.CommentedConfiguration;
import com.palmergames.bukkit.towny.event.TownBlockTypeRegisterEvent;

import io.github.townyadvanced.townyresources.TownyResources;
import io.github.townyadvanced.townyresources.util.SurveyPlotUtil;

public class SurveyPlotRegistrationListener implements Listener {

	/* Handles injecting SurveyPlot into Towny when Towny reloads. */
	@EventHandler
	public void onTownyLoadTownBlockTypes(TownBlockTypeRegisterEvent event) {
		String mapKey = "S";
		double cost = 1000.00;
		
		File file = new File(TownyResources.getPlugin().getDataFolder().getPath() + "config.yml");
		if (!file.exists()) {
			// Survey plots are off by default.
			return;
		}
		
		CommentedConfiguration config = new CommentedConfiguration(file);
		config.load();
		boolean usingSurveyPlots = config.getBoolean("survey_plots.enabled");
		if (!usingSurveyPlots) {
			System.out.println("Survey Plots disabled, not registering SurveyPlot");
			return;
		}
			
		mapKey = config.getString("survey_plots.ascii_map_key");
		cost = config.getDouble("survey_plots.plot_cost");
		System.out.println("Survey Plots key = " + mapKey + ", and cost = " + cost);
		SurveyPlotUtil.registerSurveyPlotOnLoad(mapKey, cost);
	}
}
