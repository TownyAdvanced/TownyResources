package io.github.townyadvanced.townyresources.controllers;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.TownBlock;
import io.github.townyadvanced.townyresources.TownyResources;
import io.github.townyadvanced.townyresources.objects.CategoryExtractionRecord;
import io.github.townyadvanced.townyresources.objects.ResourceExtractionCategory;
import io.github.townyadvanced.townyresources.settings.TownyResourcesSettings;
import io.github.townyadvanced.townyresources.settings.TownyResourcesTranslation;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class PlayerExtractionLimitsController {

    public static Map<Entity, Entity> mobsDamagedByPlayersThisShortTick = new HashMap<>();
    public static Map<Entity, Entity> mobsDamagedByPlayersLastShortTick = new HashMap<>();   
    public static Map<Material, ResourceExtractionCategory> materialToResourceExtractionCategoryMap = new HashMap<>();    
    public static Map<UUID, Map<Material, CategoryExtractionRecord>> allPlayerExtractionRecords = new HashMap<>();
    public static final int DELAY_BETWEEN_LIMIT_MESSAGES_MILLIS = 5000;

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
        //Return if not a mob
        if(!(event.getEntity() instanceof Mob))
            return;
                
        if(event.getDamager() instanceof Player) {
            //Mark the mob as recently hit by the player
            mobsDamagedByPlayersThisShortTick.put(event.getEntity(), event.getDamager());                
        } else if (event.getDamager() instanceof Projectile && ((Projectile)event.getDamager()).getShooter() instanceof Player) {
            //Mark the mob as recently hit by the player
            mobsDamagedByPlayersThisShortTick.put(event.getEntity(), (Entity)((Projectile)event.getDamager()).getShooter());                           
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
                Map<Material, CategoryExtractionRecord> playerExtractionRecord = getPlayerExtractionRecord(player.getUniqueId());
                
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
                    if(categoryExtractionRecord.isExtractionLimitReached())
                        sendLimitReachedWarningMessage((Player)event.getEntity(), categoryExtractionRecord);                       
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
        Map<Material, CategoryExtractionRecord> playerExtractionRecord = getPlayerExtractionRecord(event.getPlayer().getUniqueId());

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
            if(categoryExtractionRecord.isExtractionLimitReached()) {
                switch(drop.getType()) {
                    case STONE:
                    case COBBLESTONE:
                    case DIRT:
                        break;
                    default:                        
                        sendLimitReachedWarningMessage(event.getPlayer(), categoryExtractionRecord);                       
                }             
            }
        }
    }

    /**
     * Process player shear entity event
     * Limits extraction of wool
     * 
     * @param event event
     */
    public static void processPlayerShearEntityEvent(PlayerShearEntityEvent event) {
        // Only limit if sheep  (mooshroom & iron-golem mechanics don't seem worth limiting
        if(event.getEntity().getType() != EntityType.SHEEP)
            return;  
            
        //Get the player extraction record
        Map<Material, CategoryExtractionRecord> playerExtractionRecord = getPlayerExtractionRecord(event.getPlayer().getUniqueId());
        
        DyeColor sheepColour = ((Sheep)event.getEntity()).getColor();
        Material itemMaterial = Material.getMaterial(sheepColour.toString() + "_WOOL");
        
        //Return if item is not listed as a restricted resource        
        if(!materialToResourceExtractionCategoryMap.containsKey(itemMaterial))
            return;

        ///Get the category extraction record
        CategoryExtractionRecord categoryExtractionRecord = getCategoryExtractionRecord(playerExtractionRecord, itemMaterial);                                            
             
        /* 
         * If player is at the limit, cancel the event
         *
         * If player is not at the limit, add extracted amount to record.                    
         */
        if(categoryExtractionRecord.isExtractionLimitReached()) {
            event.setCancelled(true);
        } else {
            categoryExtractionRecord.addExtractedAmount(2); //Had to hardcode the amount because the event doesn't have it                         
        }
                            
        //If the limit has been reached, send a warning message
        if(categoryExtractionRecord.isExtractionLimitReached())
            sendLimitReachedWarningMessage(event.getPlayer(), categoryExtractionRecord);                       
    }
    
    /**
     * Process on item spawning event
     * Limits egg creation
     * 
     * @param event event
     */
    public static void processItemSpawnEvent(ItemSpawnEvent event) {    
        //Return if item is not an egg
        Material itemMaterial = event.getEntity().getItemStack().getType();        
        if(itemMaterial != Material.EGG)
            return;

        //Return if item is not listed as a restricted resource
        if(!materialToResourceExtractionCategoryMap.containsKey(itemMaterial))
            return;

        //If location is not a town, cancel the event
        TownBlock townblock = TownyAPI.getInstance().getTownBlock(event.getLocation());           
        if(townblock == null) {
            event.setCancelled(true);
            return;
        }
            
        //If there is no townblock owner, cancel the event
        Resident resident;
        try {
            resident = townblock.getResident();
        } catch (NotRegisteredException nre) {
            event.setCancelled(true);
            return;
        }
                
        //Get the player extraction record
        Map<Material, CategoryExtractionRecord> playerExtractionRecord = getPlayerExtractionRecord(resident.getUUID());

        ///Get the category extraction record
        CategoryExtractionRecord categoryExtractionRecord = getCategoryExtractionRecord(playerExtractionRecord, itemMaterial);                                            
             
        /* 
         * If player is at the limit, cancel the event 
         * If player is not at the limit, add extracted amount to record.                    
         */
        if(categoryExtractionRecord.isExtractionLimitReached()) {
            event.setCancelled(true);
        } else {
            categoryExtractionRecord.addExtractedAmount(event.getEntity().getItemStack().getAmount());                         
        }
                 
        //Do not send a warning message in the case of egg drops.
    }
        
    /**
     * Process fishing event
     * Limits player fishing of resources
     * 
     * @param event event
     */
    public static void processPlayerFishEvent(PlayerFishEvent event) {
        //Get the player extraction record
        Map<Material, CategoryExtractionRecord> playerExtractionRecord = getPlayerExtractionRecord(event.getPlayer().getUniqueId());
        
        //Return if nothing is caught yet
        if(event.getState() != PlayerFishEvent.State.CAUGHT_FISH && event.getState() != PlayerFishEvent.State.CAUGHT_ENTITY)
            return;

        //Return is caught entity is not an item
        if(!(event.getCaught() instanceof Item))
            return;

        //Return if item is not listed as a restricted resource        
        Material itemMaterial = ((Item)event.getCaught()).getItemStack().getType();
        if(!materialToResourceExtractionCategoryMap.containsKey(itemMaterial))
            return;

        ///Get the category extraction record
        CategoryExtractionRecord categoryExtractionRecord = getCategoryExtractionRecord(playerExtractionRecord, itemMaterial);                                            
             
        /* 
         * If player is at the limit, cancel the event
         *
         * If player is not at the limit, add extracted amount to record.                    
         */
        if(categoryExtractionRecord.isExtractionLimitReached()) {
            event.setCancelled(true);
        } else {
            categoryExtractionRecord.addExtractedAmount(((Item)event.getCaught()).getItemStack().getAmount());                       
        }
                            
        //If the limit has been reached, send a warning message
        if(categoryExtractionRecord.isExtractionLimitReached())
            sendLimitReachedWarningMessage(event.getPlayer(), categoryExtractionRecord);            
    }


    ///////////// HELPER METHODS //////////////////////

    /**
     * Get the player extraction record (create it if needed)
     * 
     * @return player extraction record
     */
    private static Map<Material, CategoryExtractionRecord> getPlayerExtractionRecord(UUID playerUUID) {
       return allPlayerExtractionRecords.computeIfAbsent(playerUUID, k -> new HashMap<>());    
    }

    /**
     * Get the category extract record (create it if needed)
     * 
     * @return category extraction record for 1 player & 1 category
     */
    private static CategoryExtractionRecord getCategoryExtractionRecord(Map<Material, CategoryExtractionRecord> playerExtractionRecord, Material droppedMaterial) {    
        //Get the player extraction record 
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

    private static void sendLimitReachedWarningMessage(Player player, CategoryExtractionRecord categoryExtractionRecord) {
        if(System.currentTimeMillis() > categoryExtractionRecord.getNextLimitWarningTime()) {
            String categoryName= categoryExtractionRecord.getResourceExtractionCategory().getCategoryName();
            int categoryExtractionLimit = categoryExtractionRecord.getResourceExtractionCategory().getCategoryExtractionLimitItems();
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_RED + TownyResourcesTranslation.of("msg_error_daily_extraction_limit_reached", categoryName, categoryExtractionLimit)));                    
            categoryExtractionRecord.setNextLimitWarningTime(System.currentTimeMillis() + DELAY_BETWEEN_LIMIT_MESSAGES_MILLIS);
        }
    }
}
