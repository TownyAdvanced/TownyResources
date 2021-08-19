package io.github.townyadvanced.townyresources.objects;

import org.bukkit.Material;

/**
 * This class represents an extraction record for a single resource
 */
public class ExtractionRecordForOneResourceType {
    private final Material sourceBlockType;
    private final Material resourceMaterial;
    private final int extractionLimit; 
    private int amountExtracted;
    private boolean extractionLimitReached;
    
    public ExtractionRecordForOneResourceType(Material sourceBlockType, Material resourceMaterial, int extractionLimit) {
        this.sourceBlockType = sourceBlockType;
        this.resourceMaterial = resourceMaterial;
        this.extractionLimit = extractionLimit;
        this.amountExtracted = 0;
        this.extractionLimitReached = false;
    }

    public boolean isExtractionLimitReached() {
        return extractionLimitReached;
    }

    public void addExtractedAmount(int amount) {
        amountExtracted += amount;
        if(amountExtracted > extractionLimit) 
            extractionLimitReached = true;
    }
}
