package io.github.townyadvanced.townyresources.listeners;

import io.github.townyadvanced.townyresources.TownyResources;
import io.github.townyadvanced.townyresources.settings.TownyResourcesSettings;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.dynmap.towny.events.BuildTownMarkerDescriptionEvent;

public class TownyResourcesDynmapTownyListener implements Listener {

    /**
     * This method updates the town popup box on Dynmap-Towny
     *
     * 1. It looks for the %town_resources% tag in the popup
     * 2. If the %town_resouces% tag exists, it replaces it with a list of town resources.
     */
    @EventHandler
    public void on(BuildTownMarkerDescriptionEvent event) {
        if (TownyResourcesSettings.isEnabled()) {
            if (event.getDescription().contains("%town_resources%")) {
                event.setDescription(event.getDescription().replace("%town_resource%", TownyResources.getPlugin().getResourcesString(event.getTown())));
            }
        }
    }
}
