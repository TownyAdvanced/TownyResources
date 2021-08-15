package io.github.townyadvanced.townyresources.objects;

import org.bukkit.Material;

/**
 * This class represents a quantity of a resource
 */
public class ResourceQuantity {
    private Material resourceType;
    private int quantity;

    public ResourceQuantity(Material material) {
        this.resourceType = material;
        this.quantity = 0;       
    }
 
     public ResourceQuantity(Material material, int quantity) {
        this.resourceType = material;
        this.quantity = quantity;       
    }
    
    public Material getResourceType() {
        return resourceType;
    }

    public void setResourceType(Material resourceType) {
        this.resourceType = resourceType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
