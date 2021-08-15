package io.github.townyadvanced.townyresources.listeners;

import com.gmail.goosius.siegewar.TownOccupationController;
import com.gmail.goosius.siegewar.settings.Translation;
import com.palmergames.bukkit.towny.TownyFormatter;
import com.palmergames.bukkit.towny.event.statusscreen.TownStatusScreenEvent;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.util.ChatTools;
import io.github.townyadvanced.townyresources.TownyResources;
import io.github.townyadvanced.townyresources.settings.TownyResourcesSettings;
import io.github.townyadvanced.townyresources.settings.TownyResourcesTranslation;
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
			String resourcesAsString = "32-OAK-LOG, 32-SUGAR_CANE";  ///Comes from town metadata
			String[] formattedListOfResources = resourcesAsString.toLowerCase().replaceAll("-", " ").replaceAll("_"," ").split(",");
			if(formattedListOfResources.length > 20) {
				formattedListOfResources = Arrays.copyOf(formattedListOfResources, 21);
				formattedListOfResources[20] = "...";
			}
			textLines.addAll(ChatTools.listArr(formattedListOfResources, TownyResourcesTranslation.of("town.screen.daily.production", formattedListOfResources.length)));

			// > Available For Collection [2]: 64 oak log, 64 sugar cane
			resourcesAsString = "64-OAK-LOG, 64-SUGAR_CANE";  ///Comes from town metadata
			formattedListOfResources = resourcesAsString.toLowerCase().replaceAll("-", " ").replaceAll("_"," ").split(",");
			if(formattedListOfResources.length > 20) {
				formattedListOfResources = Arrays.copyOf(formattedListOfResources, 21);
				formattedListOfResources[20] = "...";
			}
			textLines.addAll(ChatTools.listArr(formattedListOfResources, TownyResourcesTranslation.of("town.screen.available.for.collection", formattedListOfResources.length)));
			
			event.addLines(textLines);
		}
	}
}
