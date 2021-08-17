package io.github.townyadvanced.townyresources.objects;

import org.bukkit.Material;

/**
 * This class represents a quantity of a resource
 */
public class ResourceQuantity {
    private String resource;
    private int quantity;
 
     public ResourceQuantity(String resource, int quantity) {
        this.resource = resource;
        this.quantity = quantity;       
    }

    public int getQuantity() {
        return quantity;
    }

    public String getResource() {
        return resource;
    }
}
