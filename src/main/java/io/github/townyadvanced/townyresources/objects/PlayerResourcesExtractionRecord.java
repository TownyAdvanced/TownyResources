package io.github.townyadvanced.townyresources.objects;

import org.bukkit.Material;

import java.util.Map;

/**
 * This class represents a players resources extraction record
 */
public class PlayerResourcesExtractionRecord {
    private Map<Material, ResourceExtractionRecord> resourceExtractionRecords;
    private long timeSinceLastLimitWarning;   //helps us to avoid spamming the player with limit warnings

    public Map<Material, ResourceExtractionRecord> getResourceExtractionRecords() {
        return resourceExtractionRecords;
    }

    public void setResourceExtractionRecords(Map<Material, ResourceExtractionRecord> resourceExtractionRecords) {
        this.resourceExtractionRecords = resourceExtractionRecords;
    }

    public long getTimeSinceLastLimitWarning() {
        return timeSinceLastLimitWarning;
    }

    public void setTimeSinceLastLimitWarning(long timeSinceLastLimitWarning) {
        this.timeSinceLastLimitWarning = timeSinceLastLimitWarning;
    }
}
