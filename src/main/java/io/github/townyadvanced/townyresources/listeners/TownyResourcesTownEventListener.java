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

			// > Daily Productivity [2]: 128 Oak Log, 128 Sugar Cane
			String resourcesAsString = "OAK-LOG-128, SUGAR_CANE-128";  ///Comes from town metadata
			String[] resourcesAsArray = resourcesAsString.toLowerCase().split(",");
			//Truncate list if there is too many resources
			if(resourcesAsArray.length > 10) 
				resourcesAsArray = Arrays.copyOf(resourcesAsArray, 10);
			//Create list for display
			String[] formattedListOfResources = new String[resourcesAsArray.length];
			String[] resourceParts;
			StringBuilder stringBuilder;
			for(int i = 0; i < resourcesAsArray.length; i++) {
				resourceParts = resourcesAsArray[i].split("-");
				stringBuilder = new StringBuilder();
				if(resourceParts.length == 3) {
					formattedListOfResources[i] = 
						stringBuilder
							.append(resourceParts[0])
							.append(" ")
							.append(resourceParts[2])
							.append(" ")
							.append(resourceParts[0])
							.toString();
				} else {
					formattedListOfResources[i] = 
						stringBuilder
							.append(resourceParts[2])
							.append(" ")
							.append(resourceParts[0])
							.toString();
				}
			}
			textLines.addAll(ChatTools.listArr(formattedListOfResources, Translation.of("town.screen.daily.production", formattedListOfResources.length)));

			textLines.add(TownyResourcesTranslation.of("town.screen.available.for.collection", "dummy list"));	
			
			event.addLines(textLines);
		}
	}
}
