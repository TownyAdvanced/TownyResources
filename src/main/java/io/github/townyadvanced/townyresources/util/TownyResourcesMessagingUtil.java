package io.github.townyadvanced.townyresources.util;

import org.apache.commons.lang.WordUtils;
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

    public static String[] formatResourcesStringForGovernmentScreenDisplay(String resourcesAsString) {
        if(resourcesAsString.length() == 0) {
            return new String[0];
        } else {
            //Convert given string to array
            String[] resourcesAsFormattedArray = 
                WordUtils.capitalizeFully(
                resourcesAsString
                .replaceAll("-", " ")
                .replaceAll("_", " "))
                .split(",");

            //Shorter result if it is too long
            if(resourcesAsFormattedArray.length > 20) {
                resourcesAsFormattedArray = Arrays.copyOf(resourcesAsFormattedArray, 21);
                resourcesAsFormattedArray[20] = "...";
            }
            //Return result
            return resourcesAsFormattedArray;
        }
    }
    
    public static String formatProductionStringForDynmapTownyDisplay(String productionAsString) {    
        if(productionAsString.length() == 0) {
            return "";
        } else {
            return WordUtils.capitalizeFully(productionAsString.replaceAll("_", " ").replaceAll("\\d+-", ""));
        }
    }

    public static String formatMaterialForDisplay(Material material) {
        return WordUtils.capitalizeFully(material.toString().replaceAll("_", " "));
    }
}
