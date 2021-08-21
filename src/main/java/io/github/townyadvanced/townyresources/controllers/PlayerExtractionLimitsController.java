package io.github.townyadvanced.townyresources.controllers;

import io.github.townyadvanced.townyresources.TownyResources;
import io.github.townyadvanced.townyresources.objects.CategoryExtractionRecord;
import io.github.townyadvanced.townyresources.objects.ResourceExtractionCategory;
import io.github.townyadvanced.townyresources.settings.TownyResourcesSettings;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerExtractionLimitsController {

    public static Map<Entity, Entity> mobsDamagedByPlayersThisShortTick = new HashMap<>();
    public static Map<Entity, Entity> mobsDamagedByPlayersLastShortTick = new HashMap<>();   
    public static Map<Material, ResourceExtractionCategory> materialToResourceExtractionCategoryMap = new HashMap<>();    
    public static Map<Entity, Map<Material, CategoryExtractionRecord>> allPlayerExtractionRecords = new HashMap<>();
    

    public static void resetMobsDamagedByPlayers() {
        mobsDamagedByPlayersLastShortTick.clear();
        mobsDamagedByPlayersLastShortTick.putAll(mobsDamagedByPlayersThisShortTick);
        mobsDamagedByPlayersThisShortTick.clear();
    }
    
    public static void loadAllResourceExtractionCategories() throws Exception{
         //Load all categories
         List<ResourceExtractionCategory> resourceExtractionCategories = TownyResourcesSettings.getResourceExtractionCategories();
         //Clear the map
         materialToResourceExtractionCategoryMap.clear();
         //Put each material on the map
         for(ResourceExtractionCategory category: resourceExtractionCategories) {
            TownyResources.info(category.getCategoryName() + ": " + Arrays.toString(category.getMaterialsInCategory().toArray())) ;
         
             for(Material material: category.getMaterialsInCategory()) {
                 materialToResourceExtractionCategoryMap.put(material, category);
             }
         }
        
    }

    public static void saveAllPlayerExtractionRecords() {
        //TODO  do every short tick
        
    }

    /**
     * If a mob was hit recently by a player, mark it.
     * 
     * @param event the event
     */
    public static void processEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if(!event.isCancelled() && event.getEntity() instanceof Mob) {
                    
            if(event.getDamager() instanceof Player) {
                //Mark the mob as recently hit by the player
                mobsDamagedByPlayersThisShortTick.put(event.getEntity(), event.getDamager());                
            } else if (event.getDamager() instanceof Projectile && ((Projectile)event.getDamager()).getShooter() instanceof Player) {
                //Mark the mob as recently hit by the player
                mobsDamagedByPlayersThisShortTick.put(event.getEntity(), (Entity)((Projectile)event.getDamager()).getShooter());                           
            }
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
                        
            //Find the player who did the dirty deed
            Entity player;
            if(mobsDamagedByPlayersThisShortTick.containsKey(event.getEntity())) {
                player = mobsDamagedByPlayersThisShortTick.get(event.getEntity());
            } else {
                player = mobsDamagedByPlayersLastShortTick.get(event.getEntity());                
            }
            
            if(player != null) {                       
                //Get the player extraction record (create it if needed)
                Map<Material, CategoryExtractionRecord> playerExtractionRecord = allPlayerExtractionRecords.computeIfAbsent(player, k -> new HashMap<>());

                //Cycle each item dropped and decide what to do
                for(ItemStack drop: event.getDrops()) {
                                                           
                    //Skip item if it is not listed as a restricted resource
                    if(!materialToResourceExtractionCategoryMap.containsKey(drop.getType()))
                        continue;

                    //Get the extraction record for the item's category
                    CategoryExtractionRecord categoryExtractionRecord = playerExtractionRecord.get(drop.getType());            
                    if(categoryExtractionRecord == null) {
                        ResourceExtractionCategory resourceExtractionCategory = materialToResourceExtractionCategoryMap.get(drop.getType());
                        categoryExtractionRecord = new CategoryExtractionRecord(resourceExtractionCategory);
                        playerExtractionRecord.put(drop.getType(), categoryExtractionRecord);
                    }
    
                    //If the player is at their limit for the material, cancel the drop, otherwise allow it and update the limit
                    if(categoryExtractionRecord.isExtractionLimitReached()) {
                        drop.setAmount(0);
                        player.sendMessage("Limit Reached");
                    } else {
                        drop.setAmount(categoryExtractionRecord.addExtractedAmount(drop.getAmount()));
                    } 
                }
            }            
        }               
    }
}
