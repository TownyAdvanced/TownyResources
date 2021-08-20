package io.github.townyadvanced.townyresources.objects;

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

    public int getCategoryExtractionLimitItems() {
        return categoryExtractionLimitItems;
    }

    public List<Material> getMaterialsInCategory() {
        return materialsInCategory;
    }
}
