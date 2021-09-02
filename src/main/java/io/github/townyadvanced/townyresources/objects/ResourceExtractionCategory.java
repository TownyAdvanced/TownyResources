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

    public String getName() {
        return categoryName;
    }

    public int getCategoryExtractionLimitItems() {
        return categoryExtractionLimitItems;
    }

    public List<Material> getMaterialsInCategory() {
        return materialsInCategory;
    }
}
