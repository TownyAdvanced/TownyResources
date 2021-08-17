package io.github.townyadvanced.townyresources.listeners;

import com.palmergames.bukkit.towny.event.statusscreen.NationStatusScreenEvent;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.util.ChatTools;
import io.github.townyadvanced.townyresources.TownyResources;
import io.github.townyadvanced.townyresources.metadata.TownyResourcesGovernmentMetaDataController;
import io.github.townyadvanced.townyresources.settings.TownyResourcesSettings;
import io.github.townyadvanced.townyresources.settings.TownyResourcesTranslation;
import io.github.townyadvanced.townyresources.util.TownyResourcesMessagingUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Goosius
 *
 */
public class TownyResourcesNationEventListener implements Listener {

	@SuppressWarnings("unused")
	private final TownyResources plugin;
	
	public TownyResourcesNationEventListener(TownyResources instance) {

		plugin = instance;
	}

	/*
	 * TownyResources will add resource info to the town screen
	 */
	@EventHandler
	public void onNationStatusScreen(NationStatusScreenEvent event) {
		if (TownyResourcesSettings.isEnabled()) {
			List<String> textLines = new ArrayList<>();
			Nation nation = event.getNation();

			//Resources:
			textLines.add(TownyResourcesTranslation.of("town.screen.header"));

			// > Daily Productivity [2]: 96 oak Log, 96 sugar cane, 24 gold_ore
			String resourcesAsString = TownyResourcesGovernmentMetaDataController.getDailyProduction(nation);
			String[] resourcesAsFormattedArray = TownyResourcesMessagingUtil.formatResourcesStringForDisplay(resourcesAsString); 
			textLines.addAll(ChatTools.listArr(resourcesAsFormattedArray, TownyResourcesTranslation.of("nation.screen.daily.production", resourcesAsFormattedArray.length)));

			// > Available For Collection [2]: 192 oak log, 192 sugar cane, 48 gold ore
			resourcesAsString = TownyResourcesGovernmentMetaDataController.getAvailableForCollection(nation);
			resourcesAsFormattedArray = TownyResourcesMessagingUtil.formatResourcesStringForDisplay(resourcesAsString); 
			textLines.addAll(ChatTools.listArr(resourcesAsFormattedArray, TownyResourcesTranslation.of("nation.screen.available.for.collection", resourcesAsFormattedArray.length)));

	        event.addLines(textLines);
		}
	}
}
