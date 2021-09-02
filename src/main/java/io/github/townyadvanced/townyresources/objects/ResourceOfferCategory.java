package io.github.townyadvanced.townyresources.objects;

import io.github.townyadvanced.townyresources.settings.TownyResourcesTranslation;
import io.github.townyadvanced.townyresources.util.TownyResourcesMessagingUtil;

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

    /**
     * There are 3 possibilities here
     * 1. The category is in the translations file
     * 2. The category is a valid material name
     * 3. None of the above
     * 
     * @return the translated category name
     */
    public String getTranslatedName() {
        if(TownyResourcesTranslation.hasKey("resource_category_" + name)) {
            return TownyResourcesTranslation.of("resource_category_" + name).split(",")[1].trim(); 
        } else {
            return TownyResourcesMessagingUtil.getTranslatedMaterialName(name);           
        }
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
