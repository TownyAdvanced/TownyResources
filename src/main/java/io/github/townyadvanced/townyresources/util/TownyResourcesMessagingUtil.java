package io.github.townyadvanced.townyresources.util;

import com.meowj.langutils.lang.LanguageHelper;
import io.github.townyadvanced.townyresources.settings.TownyResourcesSettings;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.util.Colors;

import io.github.townyadvanced.townyresources.TownyResources;
import io.github.townyadvanced.townyresources.settings.TownyResourcesTranslation;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
     *  Format resource string to something we can send to chat utils
     *
     * @param resourcesAsString resource string
     * @return an array we can use on the town/nation screen
     */
    public static String[] formatResourcesStringForGovernmentScreenDisplay(String resourcesAsString) {
        if(resourcesAsString.isEmpty()) {
            return new String[0];
        } else {
            String[] resourcesAsFormattedArray = convertResourceAmountsStringToFormattedArray(resourcesAsString);
            if(resourcesAsFormattedArray.length > 20) {
                resourcesAsFormattedArray = Arrays.copyOf(resourcesAsFormattedArray, 21);
                resourcesAsFormattedArray[20] = "...";
            }
            return resourcesAsFormattedArray;
        }
    }
    
    /**
     *  Format resource string to something we can send to the dynmap
     *
     * @param resourcesAsString resource string
     * @return a string we can show on the dynmap e.g. "Wheat, Coal, Iron"
     */
    public static String formatProductionStringForDynmapTownyDisplay(String resourcesAsString) {    
        if(resourcesAsString.isEmpty()) {
            return "";
        } else {
            if(TownyResources.getPlugin().isLanguageUtilsInstalled()) {
                List<String> resourcesAsFormattedList = new ArrayList<>(); 
                String[] resourcesAsArray = resourcesAsString.replaceAll("\\d+-", "").split(",");
                Material material;
                String translatedMaterialName;
                for(String resourceAsString: resourcesAsArray) {
                    material = Material.getMaterial(resourceAsString);
                    translatedMaterialName = getTranslatedMaterialName(material);                    
                    resourcesAsFormattedList.add(translatedMaterialName);                
                }                       
                return Arrays.toString(resourcesAsFormattedList.toArray()).replace("[","").replace("]","");                
            } else {
                return WordUtils.capitalizeFully(resourcesAsString.replaceAll("_", " ").replaceAll("\\d+-", ""));
            }
        }
    }

    /**
     * Format one material for display.
     * 
     * @param material the material
     * @return the formatted material name
     */
    public static String formatMaterialForDisplay(Material material) {
        if(TownyResources.getPlugin().isLanguageUtilsInstalled()) {
            return getTranslatedMaterialName(material);
        } else {
            return WordUtils.capitalizeFully(material.toString().replaceAll("_", " "));
        }
    }

    /**
     * Convert a resource amount string formatted array
     * 
     * NOTE: Do not pass in an empty string
     * 
     * @param resourcesAmountsString e.g. "64-WHEAT,64-COAL"
     * @return e.g. ["64 Wheat","64 Coal"]
     */
    private static String[] convertResourceAmountsStringToFormattedArray(String resourcesAmountsString) {
        if(TownyResources.getPlugin().isLanguageUtilsInstalled()) {
            //Return translated materials array
            List<String> resourcesAsFormattedList = new ArrayList<>();    
            String[] resourcesAsArray = resourcesAmountsString.split(",");                
            String[] amountAndMaterialName;
            String amount;
            String materialName;
            String translatedMaterialName;
            Material material;
            for(String resourceAsString: resourcesAsArray) {
                amountAndMaterialName = resourceAsString.split("-");
                amount = amountAndMaterialName[0];
                materialName = amountAndMaterialName[1];
                material = Material.getMaterial(materialName);
                translatedMaterialName = getTranslatedMaterialName(material);                    
                resourcesAsFormattedList.add(amount + " " + translatedMaterialName);                
            }       
            return resourcesAsFormattedList.toArray(new String[0]);                    
        } else {
            //Return english materials array
            return WordUtils.capitalizeFully(
                resourcesAmountsString
                .replaceAll("_", " ")
                .replaceAll("-", " "))
                .split(",");                
        }
    }
        
    private static String getTranslatedMaterialName(Material material) {
        ItemStack fakeItemStack = new ItemStack(material);
        String translatedMaterialName = LanguageHelper.getItemDisplayName(fakeItemStack, TownyResourcesSettings.getMaterialsDisplayLanguage());
        return translatedMaterialName;                                
    }
}
