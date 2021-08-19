package io.github.townyadvanced.townyresources.objects;

import org.bukkit.Material;
import org.bukkit.event.entity.EntityPickupItemEvent;

/**
 * This class represents an extraction record for a single resource
 */
public class ExtractionRecordForOneResource {
    private final Material blockMaterial;
    private final Material itemStackMaterial;
    private final int extractionLimit; 
    private int amountExtracted;
    private boolean extractionLimitReached;
    
    public ExtractionRecordForOneResource(Material blockMaterial, Material itemStackMaterial, int extractionLimit) {
        this.blockMaterial = blockMaterial;
        this.itemStackMaterial = itemStackMaterial;
        this.extractionLimit = extractionLimit;
        this.amountExtracted = 0;
        this.extractionLimitReached = false;
    }

    /**
     * Process item pickup.
     * 
     * Note: We would normally do this in a util class rather than directly in the object.
     * However due to performance-sensitivity of this operation, we do it here
     * 
     * @param event the pickup event
     */
    public void processItemPickup(EntityPickupItemEvent event) {
        int amountToPickup = event.getItem().getItemStack().getAmount();
        if(amountExtracted + amountToPickup > extractionLimit) {
            event.setCancelled(true);
            //todo - action bar message;
        } else {
            amountExtracted += amountToPickup;
        }
    }
}
