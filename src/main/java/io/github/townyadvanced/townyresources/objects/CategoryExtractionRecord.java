package io.github.townyadvanced.townyresources.objects;

/**
 * This class represents an extraction record in a particular category, for just one player.
 */
public class CategoryExtractionRecord {
    private final ResourceExtractionCategory resourceExtractionCategory;
    private final int extractionLimitItems;  // Convenience variable
    private boolean extractionLimitReached;  //convenience variable
    private int amountAlreadyExtracted; 
    
    public CategoryExtractionRecord(ResourceExtractionCategory resourceExtractionCategory) {
        this.resourceExtractionCategory = resourceExtractionCategory;
        this.extractionLimitItems = resourceExtractionCategory.getCategoryExtractionLimitItems();
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
     * @param amountToExtract the amount to extract
     */
    public int addExtractedAmount(int amountToExtract) {
        if(amountAlreadyExtracted + amountToExtract > extractionLimitItems) { 
            //We hit the limit
            amountToExtract = extractionLimitItems - amountAlreadyExtracted;
            amountAlreadyExtracted = extractionLimitItems;
            extractionLimitReached = true;
        } else {
            //We did not hit the limit
            amountAlreadyExtracted += amountToExtract;
        }            
        return amountToExtract;  
    }

    public ResourceExtractionCategory getResourceExtractionCategory() {
        return resourceExtractionCategory;
    }
    
    public int getExtractionLimitItems() {
        return extractionLimitItems;
    }
    
    public int getAmountAlreadyExtracted() {
        return amountAlreadyExtracted;
    }
}
