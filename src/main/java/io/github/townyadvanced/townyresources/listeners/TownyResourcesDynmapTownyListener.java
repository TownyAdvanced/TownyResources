package io.github.townyadvanced.townyresources.listeners;

import io.github.townyadvanced.townyresources.TownyResources;
import io.github.townyadvanced.townyresources.metadata.TownyResourcesGovernmentMetaDataController;
import io.github.townyadvanced.townyresources.settings.TownyResourcesSettings;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.dynmap.towny.events.BuildTownMarkerDescriptionEvent;

public class TownyResourcesDynmapTownyListener implements Listener {

    @SuppressWarnings("unused")
    private final TownyResources plugin;

    public TownyResourcesDynmapTownyListener(TownyResources instance) {
        plugin = instance;
    }

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
                String productionAsString = TownyResourcesGovernmentMetaDataController.getDailyProduction(event.getTown());
                String resourcesAsFormattedString = productionAsString.replaceAll("-"," ").replaceAll("_", "");                 
                String finalDescription = event.getDescription().replace("%town_resources%", resourcesAsFormattedString);
                event.setDescription(finalDescription);
            }
        }
    }
}
