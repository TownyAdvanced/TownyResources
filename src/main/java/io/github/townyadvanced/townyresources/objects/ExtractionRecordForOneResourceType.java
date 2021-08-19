package io.github.townyadvanced.townyresources.objects;

import org.bukkit.Material;

/**
 * This class represents an extraction record for a single resource
 */
public class ExtractionRecordForOneResourceType {
    private final Material sourceBlockType;
    private final Material resourceMaterial;
    private final int extractionLimit; 
    private int amountAlreadyExtracted;
    private boolean extractionLimitReached;
    
    public ExtractionRecordForOneResourceType(Material sourceBlockType, Material resourceMaterial, int extractionLimit) {
        this.sourceBlockType = sourceBlockType;
        this.resourceMaterial = resourceMaterial;
        this.extractionLimit = extractionLimit;
        this.amountAlreadyExtracted = 0;
        this.extractionLimitReached = false;
    }

    public boolean isExtractionLimitReached() {
        return extractionLimitReached;
    }

    /**
     * Add extracted amount to record
     * Return the amount to extract  (could be modified)
     * 
     * @param amountToExtract extracted amount
     */
    public int addExtractedAmount(int amountToExtract) {
        if(amountAlreadyExtracted + amountToExtract > extractionLimit) { 
            //We hit the limit
            amountToExtract = extractionLimit - amountAlreadyExtracted;
            amountAlreadyExtracted = extractionLimit;
            extractionLimitReached = true;
        } else {
            //We did not hit the limit
            amountAlreadyExtracted += amountToExtract;
        }            
        return amountToExtract;  
    }
}
