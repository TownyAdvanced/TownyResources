package io.github.townyadvanced.townyresources.controllers;

import io.github.townyadvanced.townyresources.objects.ExtractionRecordForOneResource;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPickupItemEvent;

import java.util.HashMap;
import java.util.Map;

public class PlayerExtractionLimitsController {
    public static Map<Entity, Map<Material, ExtractionRecordForOneResource>> allExtractionRecords = new HashMap<>();

    public static void saveDataOnPlayerExtractedResouces() {
        
        
    }

    public static void processEntityPickupItemEvent(EntityPickupItemEvent event) {
        if(event.getEntity() instanceof Player) {
            //Get the player extraction record
            Map<Material, ExtractionRecordForOneResource> extractionRecordForPlayer = allExtractionRecords.get(event.getEntity());            
            if(extractionRecordForPlayer == null) {
                extractionRecordForPlayer = allExtractionRecords.put(event.getEntity(), new HashMap<>());   
            } 
                  
            //Get the player's extract record for the given material
            Material material = event.getItem().getItemStack().getType();
            ExtractionRecordForOneResource extractionRecordForResource = extractionRecordForPlayer.get(material);            
            if(extractionRecordForResource == null) {
                extractionRecordForResource = extractionRecordForPlayer.put(material, new ExtractionRecordForOneResource(material,material, 10 ));
            }
            
            //Process the item pickup
            if(extractionRecordForResource != null) {
                extractionRecordForResource.processItemPickup(event);             
            }                  
        }
    }
   
}
