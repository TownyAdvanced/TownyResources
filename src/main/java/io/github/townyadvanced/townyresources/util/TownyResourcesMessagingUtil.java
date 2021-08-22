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
    
    public static String[] formatResourcesStringForDisplay(String resourcesAsString) {
        if(resourcesAsString.length() == 0) {
            return new String[0];
        } else {
            //Convert given string to array
            String[] resourcesAsArray = 
                resourcesAsString
                .replaceAll("-"," ")
                .replaceAll("_"," ")
                .toLowerCase()
                .split(",");
            //Capitalize the first letter of each item 
            String[] result = new String[resourcesAsArray.length];
            for(int i = 0; i < resourcesAsArray.length; i++) {
                result[i] = resourcesAsArray[i].substring(0,1).toUpperCase() + resourcesAsArray[i].substring(1);              
            }
            //Shorter result if it is too long
            if(result.length > 20) {
                result = Arrays.copyOf(resourcesAsArray, 21);
                result[20] = "...";
            }
            //Return result
            return result;
        }
    }

    public static String formatMaterialForDisplay(Material winningMaterial) {
        String materialNameLowercase = winningMaterial.toString().toLowerCase();
        return materialNameLowercase.substring(0,1).toUpperCase() + materialNameLowercase.substring(1);                      
    }
}
