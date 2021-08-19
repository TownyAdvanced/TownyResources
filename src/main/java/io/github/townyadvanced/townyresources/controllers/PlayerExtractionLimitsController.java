package io.github.townyadvanced.townyresources.controllers;

import io.github.townyadvanced.townyresources.objects.ExtractionRecordForOneBlockType;
import io.github.townyadvanced.townyresources.objects.ExtractionRecordForOneMobType;
import io.github.townyadvanced.townyresources.objects.ExtractionRecordForOneResourceType;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PlayerExtractionLimitsController {

    public static Map<Entity, Entity> mobsRecentlyHitByPlayers = new HashMap<>();
    public static Map<Entity, Map<Material, ExtractionRecordForOneResourceType>> allResourceExtractionRecords = new HashMap<>();
    
    public static Map<Entity, Map<Material, ExtractionRecordForOneBlockType>> allBlockExtractionRecords = new HashMap<>();
    public static Map<Entity, Map<EntityType, ExtractionRecordForOneMobType>> allMobExtractionRecords = new HashMap<>();
   
   //TODO --- CALL ME FROM SHORT EVENT!!!!
    public static void resetMobsRecentlyHitByPlayers() {
        mobsRecentlyHitByPlayers.clear();
    }

    public static void saveDataOnPlayerExtractedResources() {
        
        
    }

    /**
     * If a mob was hit recently by a player, mark it.
     * 
     * @param event the event
     */
    public static void processEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Mob && event.getDamager() instanceof Player) {
            //Mark the mob as recently hit by the player
            mobsRecentlyHitByPlayers.put(event.getEntity(), event.getDamager());
        }         
    }

    /**
     * If a mob drops a restricted resource on death,
     * 
     * then the drop is cancelled,
     * unless the mob was recently hit by a player who was below their personal extraction limit for the mob type.
     * 
     * @param event the event
     */
    public static void processEntityDropItemEvent(EntityDropItemEvent event) {
        if(!event.isCancelled()
            && event.getEntity().isDead()
            && event.getEntity() instanceof Mob
            && !mobsRecentlyHitByPlayers.containsKey(event.getEntity())) {

            //TODO - check if the resource type is restricted
            
            //Get the player who did the dirty deed
            Entity player = mobsRecentlyHitByPlayers.get(event.getEntity());
            if(player != null) {                       
                //Get the full player extraction record
                Map<Material, ExtractionRecordForOneResourceType> materialExtractionRecordForPlayer = allResourceExtractionRecords.computeIfAbsent(player, k -> new HashMap<>());

                //Get the player extraction record for the material
                Material material = event.getItemDrop().getItemStack().getType();
                ExtractionRecordForOneResourceType extractionRecordForMaterial = materialExtractionRecordForPlayer.get(material);            
                if(extractionRecordForMaterial == null) {
                    //TODO - Fix the next line it is just a dev artefact hack
                    extractionRecordForMaterial = new ExtractionRecordForOneResourceType(null, material, 3);
                    materialExtractionRecordForPlayer.put(material, extractionRecordForMaterial);
                }

                //If the player is at their limit for the material, cancel the drop
                //Otherwise, allow the drop and update the limit
                if(extractionRecordForMaterial.isExtractionLimitReached()) {
                    event.setCancelled(true);
                    player.sendMessage("Limit Reached");
                } else {
                    extractionRecordForMaterial.addExtractedAmount(event.getItemDrop().getItemStack().getAmount());
                } 
            }            
        }               
    }
}
