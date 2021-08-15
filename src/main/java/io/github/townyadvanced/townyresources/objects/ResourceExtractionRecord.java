package io.github.townyadvanced.townyresources.objects;

import org.bukkit.Material;

/**
 * This class represents an extraction record for a particular resource
 */
public class ResourceExtractionRecord {
    private Material resource;
    private int extractedAmount;

    public Material getResource() {
        return resource;
    }

    public void setResource(Material resource) {
        this.resource = resource;
    }

    public int getExtractedAmount() {
        return extractedAmount;
    }

    public void setExtractedAmount(int extractedAmount) {
        this.extractedAmount = extractedAmount;
    }
}
