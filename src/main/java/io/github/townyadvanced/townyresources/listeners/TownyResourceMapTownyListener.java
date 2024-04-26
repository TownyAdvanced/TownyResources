package io.github.townyadvanced.townyresources.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import io.github.townyadvanced.townyresources.TownyResources;
import me.silverwolfg11.maptowny.events.MapReloadEvent;

public class TownyResourceMapTownyListener implements Listener {
    @EventHandler
    public void onMapReload(MapReloadEvent event) {
        TownyResources.getPlugin().registerMapTownyReplacements(TownyResources.getPlugin().getMapTownyPlugin());
    }
}
