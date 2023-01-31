package io.github.townyadvanced.townyresources.objects;

import org.bukkit.entity.Player;
import io.github.townyadvanced.townyresources.util.TownyResourcesMessagingUtil;

/**
 * This class represents an extraction record in a particular category, for just one player.
 */
public class CategoryExtractionRecord {
    private final ResourceExtractionCategory resourceExtractionCategory;
    private final int extractionLimitItems;  //Convenience variable
    private boolean extractionLimitReached;  //convenience variable
    private int amountAlreadyExtracted; 
    private long nextLimitWarningTime;    //Time when next warning is allowed. Used to reduce the number of messages 
    
    public CategoryExtractionRecord(ResourceExtractionCategory resourceExtractionCategory) {
        this.resourceExtractionCategory = resourceExtractionCategory;
        this.extractionLimitItems = resourceExtractionCategory.getCategoryExtractionLimitItems();
        this.amountAlreadyExtracted = 0;
        this.extractionLimitReached = false;
        this.nextLimitWarningTime = 0;
    }
    
    public CategoryExtractionRecord(ResourceExtractionCategory resourceExtractionCategory, int amountAlreadyExtracted) {
        this.resourceExtractionCategory = resourceExtractionCategory;
        this.extractionLimitItems = resourceExtractionCategory.getCategoryExtractionLimitItems();
        this.amountAlreadyExtracted = amountAlreadyExtracted;        
        this.extractionLimitReached = amountAlreadyExtracted >= extractionLimitItems;
        this.nextLimitWarningTime = 0;
    }
    
    public int getAmountAlreadyExtracted() {
        return amountAlreadyExtracted;
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
        if(amountAlreadyExtracted + amountToExtract >= extractionLimitItems) { 
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

    public String getTranslatedCategoryName(Player player) {
        return TownyResourcesMessagingUtil.formatExtractionCategoryNameForDisplay(getResourceExtractionCategory(), player);
    }

    public long getNextLimitWarningTime() {
        return nextLimitWarningTime;
    }

    public void setNextLimitWarningTime(long nextLimitWarningTime) {
        this.nextLimitWarningTime = nextLimitWarningTime;
    }   
}
