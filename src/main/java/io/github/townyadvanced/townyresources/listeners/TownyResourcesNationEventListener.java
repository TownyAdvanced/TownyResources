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
			Nation nation = event.getNation();
			String productionAsString = TownyResourcesGovernmentMetaDataController.getDailyProduction(nation);
			String availableAsString = TownyResourcesGovernmentMetaDataController.getAvailableForCollection(nation);

			if(productionAsString.isEmpty() && availableAsString.isEmpty())
				return;

			//Resources:	
			List<String> textLines = new ArrayList<>();
			textLines.add(TownyResourcesTranslation.of("nation.screen.header"));
				
			// > Daily Productivity [2]: 32 oak Log, 32 sugar cane
			String[] resourcesAsFormattedArray = TownyResourcesMessagingUtil.formatResourcesStringForTownyDisplay(productionAsString); 
			textLines.addAll(ChatTools.listArr(resourcesAsFormattedArray, TownyResourcesTranslation.of("nation.screen.daily.production", resourcesAsFormattedArray.length)));

			// > Available For Collection [2]: 64 oak log, 64 sugar cane
			resourcesAsFormattedArray = TownyResourcesMessagingUtil.formatResourcesStringForTownyDisplay(availableAsString); 
			textLines.addAll(ChatTools.listArr(resourcesAsFormattedArray, TownyResourcesTranslation.of("nation.screen.available.for.collection", resourcesAsFormattedArray.length)));
			
			event.addLines(textLines);
		}
	}
}
