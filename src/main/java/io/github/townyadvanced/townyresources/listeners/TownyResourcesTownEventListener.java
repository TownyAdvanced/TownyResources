package io.github.townyadvanced.townyresources.listeners;

import com.gmail.goosius.siegewar.TownOccupationController;
import com.gmail.goosius.siegewar.settings.Translation;
import com.palmergames.bukkit.towny.TownyFormatter;
import com.palmergames.bukkit.towny.event.statusscreen.TownStatusScreenEvent;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.util.ChatTools;
import io.github.townyadvanced.townyresources.TownyResources;
import io.github.townyadvanced.townyresources.metadata.TownyResourcesGovernmentMetaDataController;
import io.github.townyadvanced.townyresources.settings.TownyResourcesSettings;
import io.github.townyadvanced.townyresources.settings.TownyResourcesTranslation;
import io.github.townyadvanced.townyresources.util.TownyResourcesMessagingUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author Goosius
 *
 */
public class TownyResourcesTownEventListener implements Listener {

	@SuppressWarnings("unused")
	private final TownyResources plugin;
	
	public TownyResourcesTownEventListener(TownyResources instance) {

		plugin = instance;
	}

	/*
	 * TownyResources will add resource info to the town screen
	 */
	@EventHandler
	public void onTownStatusScreen(TownStatusScreenEvent event) {
		if (TownyResourcesSettings.isEnabled()) {
			List<String> textLines = new ArrayList<>();
			Town town = event.getTown();

			//Resources:
			textLines.add(TownyResourcesTranslation.of("town.screen.header"));

			// > Daily Productivity [2]: 32 oak Log, 32 sugar cane
			String resourcesAsString = TownyResourcesGovernmentMetaDataController.getDailyProduction(town);
			String[] resourcesAsFormattedArray = TownyResourcesMessagingUtil.formatResourcesStringForDisplay(resourcesAsString); 
			textLines.addAll(ChatTools.listArr(resourcesAsFormattedArray, TownyResourcesTranslation.of("town.screen.daily.production", resourcesAsFormattedArray.length)));

			// > Available For Collection [2]: 64 oak log, 64 sugar cane
			resourcesAsString = TownyResourcesGovernmentMetaDataController.getAvailableForCollection(town);
			resourcesAsFormattedArray = TownyResourcesMessagingUtil.formatResourcesStringForDisplay(resourcesAsString); 
			textLines.addAll(ChatTools.listArr(resourcesAsFormattedArray, TownyResourcesTranslation.of("town.screen.available.for.collection", resourcesAsFormattedArray.length)));
			
			event.addLines(textLines);
		}
	}
}
