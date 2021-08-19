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

import java.util.HashMap;
import java.util.Map;

public class PlayerExtractionLimitsController {

    public static Map<Entity, Entity> mobsDamagedByPlayersThisShortTick = new HashMap<>();
    public static Map<Entity, Entity> mobsDamagedByPlayersLastShortTick = new HashMap<>();   
    public static Map<Entity, Map<Material, ExtractionRecordForOneResourceType>> allResourceExtractionRecords = new HashMap<>();
    public static Map<Entity, Map<Material, ExtractionRecordForOneBlockType>> allBlockExtractionRecords = new HashMap<>();
    public static Map<Entity, Map<EntityType, ExtractionRecordForOneMobType>> allMobExtractionRecords = new HashMap<>();

    public static void resetMobsDamagedByPlayers() {
        mobsDamagedByPlayersLastShortTick.clear();
        mobsDamagedByPlayersLastShortTick.putAll(mobsDamagedByPlayersThisShortTick);
        mobsDamagedByPlayersThisShortTick.clear();
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
            mobsDamagedByPlayersThisShortTick.put(event.getEntity(), event.getDamager());
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
            && (mobsDamagedByPlayersThisShortTick.containsKey(event.getEntity())
                || mobsDamagedByPlayersLastShortTick.containsKey(event.getEntity()))) {

            //TODO - check if the resource type is restricted
            
            //Find the player who did the dirty deed
            Entity player;
            if(mobsDamagedByPlayersThisShortTick.containsKey(event.getEntity())) {
                player = mobsDamagedByPlayersThisShortTick.get(event.getEntity());
            } else {
                player = mobsDamagedByPlayersLastShortTick.get(event.getEntity());                
            }
            
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
