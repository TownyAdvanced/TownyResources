package io.github.townyadvanced.townyresources.objects;

import io.github.townyadvanced.townyresources.settings.TownyResourcesTranslation;
import io.github.townyadvanced.townyresources.util.TownyResourcesMessagingUtil;
import org.bukkit.Material;

import java.util.List;

public class ResourceExtractionCategory {
    private final String categoryName;
    private final int categoryExtractionLimitItems;   
    private final List<Material> materialsInCategory;

    public ResourceExtractionCategory(String categoryName, int categoryExtractionLimitItems, List<Material> materialsInCategory) {
        this.categoryName = categoryName;
        this.categoryExtractionLimitItems = categoryExtractionLimitItems;
        this.materialsInCategory = materialsInCategory;
    }

    public String getCategoryName() {
        return categoryName;
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
        if(TownyResourcesTranslation.hasKey("resource_category_" + categoryName)) {
            return TownyResourcesTranslation.of("resource_category_" + categoryName).split(",")[0]; 
        } else {
            return TownyResourcesMessagingUtil.getTranslatedMaterialName(categoryName);           
        }
    }
    
    public int getCategoryExtractionLimitItems() {
        return categoryExtractionLimitItems;
    }

    public List<Material> getMaterialsInCategory() {
        return materialsInCategory;
    }
}
