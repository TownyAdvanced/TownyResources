package io.github.townyadvanced.townyresources.controllers;

import com.palmergames.bukkit.towny.exceptions.TownyException;
import io.github.townyadvanced.townyresources.TownyResources;
import io.github.townyadvanced.townyresources.objects.ResourceOfferCategory;
import io.github.townyadvanced.townyresources.settings.TownyResourcesSettings;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TownResourceOffersController {

    private static List<ResourceOfferCategory> resourceOfferCategoryList = new ArrayList<>();
    private static Map<String, ResourceOfferCategory> materialToResourceOfferCategoryMap = new HashMap<>();

    public static void loadAllResourceOfferCategories() throws TownyException {
         //Load all categories
         resourceOfferCategoryList = TownyResourcesSettings.getResourceOfferCategories();
         //Clear the map
         materialToResourceOfferCategoryMap.clear();
         //Put each material on the map
         for(ResourceOfferCategory category: resourceOfferCategoryList) {         
             for(String material: category.getMaterialsInCategory()) {
                 materialToResourceOfferCategoryMap.put(material, category);
             }
         }
         TownyResources.info("All Resource Offer Categories Loaded");        
    }
    
    public static List<ResourceOfferCategory> getResourceOfferCategoryList() {
        return resourceOfferCategoryList;
    }
    
    public static Map<String, ResourceOfferCategory> getMaterialToResourceOfferCategoryMap() {
        return materialToResourceOfferCategoryMap;
    }
}
