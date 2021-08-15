package io.github.townyadvanced.townyresources.objects;

import org.bukkit.Material;

import java.util.Map;

/**
 * This class represents a players resources extraction record
 */
public class PlayerResourcesExtractionRecord {
    private Map<Material, ResourceQuantity> resourceExtractionRecords;
    private long timeSinceLastLimitWarning;   //helps us to avoid spamming the player with limit warnings

    public Map<Material, ResourceQuantity> getResourceExtractionRecords() {
        return resourceExtractionRecords;
    }

    public void setResourceExtractionRecords(Map<Material, ResourceQuantity> resourceExtractionRecords) {
        this.resourceExtractionRecords = resourceExtractionRecords;
    }

    public long getTimeSinceLastLimitWarning() {
        return timeSinceLastLimitWarning;
    }

    public void setTimeSinceLastLimitWarning(long timeSinceLastLimitWarning) {
        this.timeSinceLastLimitWarning = timeSinceLastLimitWarning;
    }
}
