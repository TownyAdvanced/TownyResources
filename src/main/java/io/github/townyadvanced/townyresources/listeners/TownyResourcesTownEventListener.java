package io.github.townyadvanced.townyresources.listeners;

import com.palmergames.bukkit.towny.event.statusscreen.TownStatusScreenEvent;
import com.palmergames.bukkit.towny.object.Town;
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
			List<String> out = new ArrayList<>();
			Town town = event.getTown();

			//Get list of resources
			out.add(TownyResourcesTranslation.of("town.screen.header"));
			String resourcesAsString = "OAK-LOG-128, SUGAR_CANE-128";  ///Comes from town metadata
			String[] resourcesAsListOfStrings = resourcesAsString.toLowerCase().split(",");
			//Truncate list if there is too many resources
			if(resourcesAsListOfStrings.length > 10) 
				resourcesAsListOfStrings = Arrays.copyOf(resourcesAsListOfStrings, 10);
			//Build resources String to display
			StringBuilder resourcesStringToDisplay = new StringBuilder();
			resourcesStringToDisplay
				.append("[")
				.append(resourcesAsListOfStrings.length)
				.append("] ");
			String[] resourceParts;
			for(String resourceAsString: resourcesAsListOfStrings) {
				resourceParts = resourceAsString.split("-");
				if(resourceParts.length == 3) {
					resourcesStringToDisplay
						.append(resourceParts[2])
						.append(" ")
						.append(resourceParts[0])
						.append(" ")
						.append(resourceParts[1]);
				} else {
					resourcesStringToDisplay
						.append(resourceParts[1])
						.append(" ")
						.append(resourceParts[0]);
				}
			}
			//Display list
			out.add(TownyResourcesTranslation.of("town.screen.daily.production", resourcesStringToDisplay.toString()));
			out.add(TownyResourcesTranslation.of("town.screen.available.for.collection", "dummy list"));

	        event.addLines(out);
		}
	}
}
