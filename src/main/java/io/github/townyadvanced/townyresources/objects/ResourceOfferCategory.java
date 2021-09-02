package io.github.townyadvanced.townyresources.objects;

import java.util.List;

public class ResourceOfferCategory {
    private final String name;
    private final int discoveryWeight;
    private final int baseAmountItems;    
    private final List<String> materialsInCategory;

    public ResourceOfferCategory(String name, int discoveryWeight, int baseOfferItems, List<String> materialsInCategory) {
        this.name = name;
        this.discoveryWeight = discoveryWeight;
        this.baseAmountItems = baseOfferItems;
        this.materialsInCategory = materialsInCategory;
    }

    public String getName() {
        return name;
    }

    public int getBaseAmountItems() {
        return baseAmountItems;
    }

    public List<String> getMaterialsInCategory() {
        return materialsInCategory;
    }

    public int getDiscoveryWeight() {
        return discoveryWeight;
    }
}
