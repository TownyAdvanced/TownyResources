package io.github.townyadvanced.townyresources.objects;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

/**
 * This class represents an extraction record for a single type of mob
 */
public class ExtractionRecordForOneMobType {
    private final EntityType sourceMobType;
    private final Material resourceMaterial;
    private final int extractionLimit; 
    private int amountExtracted;
    private boolean extractionLimitReached;
    
    public ExtractionRecordForOneMobType(EntityType sourceMobType, Material resourceMaterial, int extractionLimit) {
        this.sourceMobType = sourceMobType;
        this.resourceMaterial = resourceMaterial;
        this.extractionLimit = extractionLimit;
        this.amountExtracted = 0;
        this.extractionLimitReached = false;
    }
    
    public boolean isExtractionLimitReached() {
        return extractionLimitReached;
    }

}
