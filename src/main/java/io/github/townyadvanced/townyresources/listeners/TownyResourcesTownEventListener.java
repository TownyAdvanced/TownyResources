package io.github.townyadvanced.townyresources.listeners;

import com.gmail.goosius.siegewar.settings.Translation;
import com.palmergames.bukkit.towny.event.statusscreen.TownStatusScreenEvent;
import com.palmergames.bukkit.towny.object.Town;
import io.github.townyadvanced.townyresources.TownyResources;
import io.github.townyadvanced.townyresources.settings.Settings;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
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
		if (Settings.isEnabled()) {
			List<String> out = new ArrayList<>();
			Town town = event.getTown();

			//Resources:
			out.add(Translation.of("town.screen.header"));
			out.add(Translation.of("town.screen.daily.production", "dummy list"));
			out.add(Translation.of("town.screen.available.for.collection", "dummy list"));

	        event.addLines(out);
		}
	}
}
