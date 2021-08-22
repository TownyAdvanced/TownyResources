package io.github.townyadvanced.townyresources.objects;

import org.bukkit.Material;

import java.util.List;

public class ResourceOfferCategory {
    private final String categoryName;
    private final int categoryDiscoveryWeight;
    private final int categoryBaseOfferItems;   
    private final List<Material> materialsInCategory;

    public ResourceOfferCategory(String categoryName, int categoryDiscoveryWeight, int categoryBaseOfferItems, List<Material> materialsInCategory) {
        this.categoryName = categoryName;
        this.categoryDiscoveryWeight = categoryDiscoveryWeight;
        this.categoryBaseOfferItems = categoryBaseOfferItems;
        this.materialsInCategory = materialsInCategory;
    }


    public String getCategoryName() {
        return categoryName;
    }

    public int getCategoryBaseOfferItems() {
        return categoryBaseOfferItems;
    }

    public List<Material> getMaterialsInCategory() {
        return materialsInCategory;
    }

    public int getCategoryDiscoveryWeight() {
        return categoryDiscoveryWeight;
    }
}
