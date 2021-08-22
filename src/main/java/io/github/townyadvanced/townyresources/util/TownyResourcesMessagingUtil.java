package io.github.townyadvanced.townyresources.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.util.Colors;

import io.github.townyadvanced.townyresources.TownyResources;
import io.github.townyadvanced.townyresources.settings.TownyResourcesTranslation;

import java.util.Arrays;

public class TownyResourcesMessagingUtil {

    final static String prefix = TownyResourcesTranslation.of("plugin_prefix");
    
    public static void sendErrorMsg(CommandSender sender, String message) {
        //Ensure the sender is not null (i.e. is an online player who is not an npc)
        if(sender != null)
            sender.sendMessage(prefix + Colors.Red + message);
    }

    public static void sendMsg(CommandSender sender, String message) {
        //Ensure the sender is not null (i.e. is an online player who is not an npc)
        if(sender != null)
            sender.sendMessage(prefix + Colors.White + message);
    }
    
    public static void sendGlobalMessage(String message) {
        TownyResources.info(message);
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player != null && TownyAPI.getInstance().isTownyWorld(player.getWorld()))
                sendMsg(player, message);
        }
    }

    /**
     * Note: All spaces will already be removed
     * 
     * @param resourcesAsString
     * @return
     */
    public static String[] formatResourcesStringForTownyDisplay(String resourcesAsString) {
        String[] amountMaterialPair;
    
        if(resourcesAsString.length() == 0) {
            return new String[0];
        } else {
            //Convert given string to array
            String[] resourcesAsArray = 
                resourcesAsString
                .toLowerCase()
                .replaceAll("_", " ")
                .split(",");
            //Capitalize the first letter of each material 
            String[] resourcesAsFormattedArray = new String[resourcesAsArray.length];
            for(int i = 0; i < resourcesAsArray.length; i++) {
                amountMaterialPair = resourcesAsArray[i].split("-");                                
                resourcesAsFormattedArray[i] = amountMaterialPair[0] + " " + amountMaterialPair[1].substring(0,1).toUpperCase() + amountMaterialPair[1].substring(1);                              
            }
            //Shorter result if it is too long
            if(resourcesAsFormattedArray.length > 20) {
                resourcesAsFormattedArray = Arrays.copyOf(resourcesAsArray, 21);
                resourcesAsFormattedArray[20] = "...";
            }
            //Return result
            return resourcesAsFormattedArray;
        }
    }
    
    public static String formatResourcesStringForDynmapTownyDisplay(String resourcesAsString) {
        String[] amountMaterialPair;
    
        if(resourcesAsString.length() == 0) {
            return "";
        } else {
            //Convert given string to array
            String[] resourcesAsArray = 
                resourcesAsString
                .toLowerCase()
                .split(",");
            //Capitalize the first letter of each material 
            String[] resourcesAsFormattedArray = new String[resourcesAsArray.length];
            for(int i = 0; i < resourcesAsArray.length; i++) {
                amountMaterialPair = resourcesAsArray[i].split("-");                                
                resourcesAsFormattedArray[i] = amountMaterialPair[0] + amountMaterialPair[1].substring(0,1).substring(1).replaceAll("_","");                              
            }
            //Build result string
            StringBuilder result = new StringBuilder();
            boolean firstEntry = true;
            for(String resource: resourcesAsFormattedArray) {
                if(firstEntry) {
                    firstEntry = false;
                } else {
                    result.append(", ");
                }
                result.append(resource);
            }
            //Return result
            return result.toString();
        }
    }


    public static String formatMaterialForDisplay(Material winningMaterial) {
        String materialNameLowercase = winningMaterial.toString().toLowerCase();
        return materialNameLowercase.substring(0,1).toUpperCase() + materialNameLowercase.substring(1);                      
    }
}
