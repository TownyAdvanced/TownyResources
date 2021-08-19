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
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.inventory.ItemStack;

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
        if(!event.isCancelled()
            && event.getEntity() instanceof Mob 
            && event.getDamager() instanceof Player) {
                System.out.println("Mob was hit by plaer");
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
    public static void processEntityDeathEvent(EntityDeathEvent event) {
        if(event.getEntity() instanceof Mob
            && (mobsDamagedByPlayersThisShortTick.containsKey(event.getEntity())
                || mobsDamagedByPlayersLastShortTick.containsKey(event.getEntity()))) {

            System.out.println("Now checking mob drop of mob jilled by player");
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

                //Cycle each material dropped and decide what to do
                for(ItemStack drop: event.getDrops()) {
                    //Get the player extraction record for the material
                    ExtractionRecordForOneResourceType extractionRecordForMaterial = materialExtractionRecordForPlayer.get(drop.getType());            
                    if(extractionRecordForMaterial == null) {
                        //TODO - Fix the next line it is just a dev artefact hack
                        extractionRecordForMaterial = new ExtractionRecordForOneResourceType(null, drop.getType(), 3);
                        materialExtractionRecordForPlayer.put(drop.getType(), extractionRecordForMaterial);
                    }
    
                    //If the player is at their limit for the material, cancel the drop
                    //Otherwise, allow the drop and update the limit
                    if(extractionRecordForMaterial.isExtractionLimitReached()) {
                        drop.setAmount(0);
                        player.sendMessage("Limit Reached");
                    } else {
                        extractionRecordForMaterial.addExtractedAmount(drop.getAmount());
                    } 
                }
            }            
        }               
    }
}
