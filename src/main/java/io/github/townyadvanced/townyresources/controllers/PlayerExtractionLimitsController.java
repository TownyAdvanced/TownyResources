package io.github.townyadvanced.townyresources.controllers;

import com.gmail.goosius.siegewar.settings.Translation;
import io.github.townyadvanced.townyresources.TownyResources;
import io.github.townyadvanced.townyresources.objects.CategoryExtractionRecord;
import io.github.townyadvanced.townyresources.objects.ResourceExtractionCategory;
import io.github.townyadvanced.townyresources.settings.TownyResourcesSettings;
import io.github.townyadvanced.townyresources.settings.TownyResourcesTranslation;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

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
             for(Material material: category.getMaterialsInCategory()) {
                 materialToResourceExtractionCategoryMap.put(material, category);
             }
         }
         TownyResources.info("All Resource Extraction Categories Loaded");        
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
     * A Mob which dies must meet the following criteria to drop items:
     * -> It must have been recently hit by a player who was below their daily extraction limit for the dropped items.
     * 
     * @param event the event
     */
    public static void processEntityDeathEvent(EntityDeathEvent event) {
        if(event.getEntity() instanceof Mob) {

            //If the mob was not recently hit by a player, it drops nothing
            if (!mobsDamagedByPlayersThisShortTick.containsKey(event.getEntity())
                && !mobsDamagedByPlayersLastShortTick.containsKey(event.getEntity())) {
                for(ItemStack drop: event.getDrops()) {
                    drop.setAmount(0);
                }
            }

            //Find the player who did the dirty deed
            Entity player;
            if(mobsDamagedByPlayersThisShortTick.containsKey(event.getEntity())) {
                player = mobsDamagedByPlayersThisShortTick.get(event.getEntity());
            } else {
                player = mobsDamagedByPlayersLastShortTick.get(event.getEntity());                
            }
            
            if(player != null) {                       
                //Get the player extraction record
                Map<Material, CategoryExtractionRecord> playerExtractionRecord = getPlayerExtractionRecord(player);
                
                //Cycle each item dropped and decide what to do
                for(ItemStack drop: event.getDrops()) {
                                                           
                    //Skip item if it is not listed as a restricted resource
                    if(!materialToResourceExtractionCategoryMap.containsKey(drop.getType()))
                        continue;

                    ///Get the category extract record
                    CategoryExtractionRecord categoryExtractionRecord = getCategoryExtractionRecord(playerExtractionRecord, drop.getType());                                            

                    //If player is at the limit, set the drop to 0, otherwise add to the record and possibly reduce the drop                     
                    if(categoryExtractionRecord.isExtractionLimitReached()) {
                       drop.setAmount(0);
                    } else {
                        drop.setAmount(categoryExtractionRecord.addExtractedAmount(drop.getAmount()));                    
                    }
                                        
                    //If the limit has been reached, send a warning message
                    if(categoryExtractionRecord.isExtractionLimitReached() && System.currentTimeMillis() > categoryExtractionRecord.getNextLimitWarningTime()) {
                        String categoryName= categoryExtractionRecord.getResourceExtractionCategory().getCategoryName();
                        int categoryExtractionLimit = categoryExtractionRecord.getResourceExtractionCategory().getCategoryExtractionLimitItems();
                        ((Player)player).spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_RED + TownyResourcesTranslation.of("msg_error_daily_extraction_limit_reached", categoryName, categoryExtractionLimit)));                    
                        categoryExtractionRecord.setNextLimitWarningTime(System.currentTimeMillis() + 5000);
                    }
                }
            }            
        }               
    }

    /**
     * A broken block must meet the following criteria to drop items:
     * -> It must have been broken by a player who was below their daily extraction limit for the dropped items.
     * 
     * @param event the event - this event is only called for Players (not entities)
     */
    public static void processBlockBreakEvent(BlockBreakEvent event) {
        //Get the player extraction record
        Map<Material, CategoryExtractionRecord> playerExtractionRecord = getPlayerExtractionRecord(event.getPlayer());

        //Cycle each item dropped and decide what to do
        for(ItemStack drop: event.getBlock().getDrops()) {
                                                                                              
            //Skip item if it is not listed as a restricted resource
            if(!materialToResourceExtractionCategoryMap.containsKey(drop.getType()))
                continue;

            ///Get the category extract record
            CategoryExtractionRecord categoryExtractionRecord = getCategoryExtractionRecord(playerExtractionRecord, drop.getType());                                            
                 
            /* 
             * If player is at the limit, cancel the event (except if STONE, COBBLE, DIRT, then only cancel the drop).
             *
             * If player is not at the limit, add extracted amount to record.                    
             */
            if(categoryExtractionRecord.isExtractionLimitReached()) {
                switch(drop.getType()) {
                    case STONE:
                    case COBBLESTONE:
                    case DIRT:
                        event.setDropItems(false);
                        break;
                    default:                        
                        event.setCancelled(true);
                }
            } else {
                categoryExtractionRecord.addExtractedAmount(drop.getAmount());                         
            }
                                
            //If the limit has been reached, send a warning message (except if STONE, COBBLE, DIRT, then don't send message).
            if(categoryExtractionRecord.isExtractionLimitReached() && System.currentTimeMillis() > categoryExtractionRecord.getNextLimitWarningTime()) {
                switch(drop.getType()) {
                    case STONE:
                    case COBBLESTONE:
                    case DIRT:
                        break;
                    default:                        
                        String categoryName= categoryExtractionRecord.getResourceExtractionCategory().getCategoryName();
                        int categoryExtractionLimit = categoryExtractionRecord.getResourceExtractionCategory().getCategoryExtractionLimitItems();
                        event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_RED + TownyResourcesTranslation.of("msg_error_daily_extraction_limit_reached", categoryName, categoryExtractionLimit)));                    
                        categoryExtractionRecord.setNextLimitWarningTime(System.currentTimeMillis() + 5000);
                }             
            }
        }
    }

    /**
     * Get the player extraction record (create it if needed)
     * 
     * @return player extraction record
     */
    private static Map<Material, CategoryExtractionRecord> getPlayerExtractionRecord(Entity player) {
       return allPlayerExtractionRecords.computeIfAbsent(player, k -> new HashMap<>());    
    }

    /**
     * Get the category extract record (create it if needed)
     * 
     * @return category extraction record for 1 player & 1 category
     */
    private static CategoryExtractionRecord getCategoryExtractionRecord(Map<Material, CategoryExtractionRecord> playerExtractionRecord, Material droppedMaterial) {    
        //Search the player extraction record 
        CategoryExtractionRecord categoryExtractionRecord = playerExtractionRecord.get(droppedMaterial);            
                  
        /*
         * If player has not yet extracted the material, check if they have extracted that CATEGORY of material.
         * 
         * If yes, re-use the category extraction record.
         * If no, create a new category extraction record.
         */
        if(categoryExtractionRecord == null) {
            ResourceExtractionCategory resourceExtractionCategory = materialToResourceExtractionCategoryMap.get(droppedMaterial);                
            for(CategoryExtractionRecord existingCategoryExtractRecord: new ArrayList<>(playerExtractionRecord.values())) {
                if(existingCategoryExtractRecord.getResourceExtractionCategory().getMaterialsInCategory().contains(droppedMaterial)) {
                    categoryExtractionRecord = existingCategoryExtractRecord;
                    break;
                }                                       
            }
            if(categoryExtractionRecord == null) {
                categoryExtractionRecord = new CategoryExtractionRecord(resourceExtractionCategory);
            }
            playerExtractionRecord.put(droppedMaterial, categoryExtractionRecord);
        }
        
        //Return category extraction record
        return categoryExtractionRecord;
    }
}
