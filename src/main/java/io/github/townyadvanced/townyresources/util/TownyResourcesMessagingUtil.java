package io.github.townyadvanced.townyresources.util;

import com.meowj.langutils.lang.LanguageHelper;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.townyadvanced.townyresources.objects.ResourceExtractionCategory;
import io.github.townyadvanced.townyresources.objects.ResourceOfferCategory;
import io.github.townyadvanced.townyresources.settings.TownyResourcesSettings;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.palmergames.adventure.text.Component;
import com.palmergames.adventure.text.event.HoverEvent;
import com.palmergames.adventure.text.format.NamedTextColor;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.TownySettings;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.Translatable;
import com.palmergames.bukkit.towny.object.Translator;
import com.palmergames.bukkit.towny.utils.TownyComponents;
import com.palmergames.bukkit.util.Colors;
import com.palmergames.util.StringMgmt;

import io.github.townyadvanced.townyresources.TownyResources;
import org.bukkit.inventory.ItemStack;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TownyResourcesMessagingUtil {
	private static List<String> categoryNames;

	public TownyResourcesMessagingUtil(TownyResources plugin) {
		List<String> categoryNames = new ArrayList<>();
		try (InputStream is = plugin.getClass().getResourceAsStream("/lang/en-US.yml")) {
			Map<String, Object> values = new Yaml(new SafeConstructor(new LoaderOptions())).load(is);
			for (String key : values.keySet()) {
				if (key.startsWith("resource_category_"))
					categoryNames.add(key);
			}
			is.close();
		} catch (IOException ignored) {
		}
		TownyResourcesMessagingUtil.categoryNames = categoryNames;
	}

	public static void sendErrorMsg(CommandSender sender, String message) {
        //Ensure the sender is not null (i.e. is an online player who is not an npc)
        if(sender != null)
            TownyMessaging.sendMessage(sender, Translatable.of("townyresources.plugin_prefix").append(Component.text("", NamedTextColor.RED)).append(message));
	}

    public static void sendErrorMsg(CommandSender sender, Translatable message) {
        //Ensure the sender is not null (i.e. is an online player who is not an npc)
        if(sender != null)
            TownyMessaging.sendMessage(sender, Translatable.of("townyresources.plugin_prefix").append(Component.text("", NamedTextColor.RED)).append(message));
    }

    public static void sendMsg(CommandSender sender, Translatable message) {
        //Ensure the sender is not null (i.e. is an online player who is not an npc)
        if(sender != null)
        	TownyMessaging.sendMessage(sender, Translatable.of("townyresources.plugin_prefix").append(Component.text("", NamedTextColor.WHITE)).append(message));
    }

    public static void sendGlobalMessage(Translatable message) {
        TownyResources.info(message.defaultLocale());
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
            if(resourcesAsFormattedArray.length > 10) {
                resourcesAsFormattedArray = Arrays.copyOf(resourcesAsFormattedArray, 11);
                resourcesAsFormattedArray[10] = "...";
            }
            return resourcesAsFormattedArray;
        }
    }
    
    /**
     *  Format resource string to something we can send to the dynmap
     *
     * @param resourcesAsString resource string
     * @return a string we can show on the dynmap e.g. "64 Wheat, 32 Iron"
     */
    public static String formatProductionStringForDynmapTownyDisplay(String resourcesAsString) {    
        if(resourcesAsString.isEmpty()) {
            return "";
        } else {
            String[] resourcesAsFormattedArray = convertResourceAmountsStringToFormattedArray(resourcesAsString);
            return Arrays.toString(resourcesAsFormattedArray).replace("[","").replace("]","");
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
        //Return translated materials array
        List<String> resourcesAsFormattedList = new ArrayList<>();
        String[] resourcesAsArray = resourcesAmountsString.split(",");
        String[] amountAndMaterialName;
        String amount;
        String materialName;
        String translatedMaterialName;
        for(String resourceAsString: resourcesAsArray) {
            if (resourceAsString.isEmpty())
                continue;
            amountAndMaterialName = resourceAsString.split("-");
            amount = amountAndMaterialName[0];
            materialName = amountAndMaterialName[1];
            translatedMaterialName = formatMaterialNameForDisplay(materialName);
            resourcesAsFormattedList.add(amount + " " + translatedMaterialName);
        }
        return resourcesAsFormattedList.toArray(new String[0]);
    }
    
    
    /**
     * Used in the Government StatusScreen events to make the production/available components.
     * 
     * @param translator Translator used for localization
     * @param resourcesAsString String representing the Resources due.
     * @param langString The language string which will be applied to the Component.
     * @return Component to be used in the StatusScreen
     */
    public static Component getSubComponentForGovernmentScreens(Translator translator, String resourcesAsString, String langString) {
		String[] resourcesAsFormattedArray = convertResourceAmountsStringToFormattedArray(resourcesAsString);
		String[] resourcesForDisplay = formatResourcesStringForGovernmentScreenDisplay(resourcesAsString);
		Component component = Component.empty();
		component = component.append(TownyComponents.legacy(translator.of(langString , resourcesAsFormattedArray.length)))
			.append(Component.text(StringMgmt.join(resourcesForDisplay, ", "), NamedTextColor.WHITE));
		component = component.hoverEvent(HoverEvent.showText(Component.text(StringMgmt.join(resourcesAsFormattedArray, ", "))));
		return component;
	}
    
    public static String formatExtractionCategoryNameForDisplay(ResourceExtractionCategory resourceExtractionCategory, CommandSender sender) {
        String categoryName = resourceExtractionCategory.getName();
        if (categoryNames.contains("resource_category_" + categoryName)) {
            return Translatable.of("resource_category_" + categoryName).forLocale(sender).split(",")[0];
        } else {
            return formatMaterialNameForDisplay(categoryName);
        }
    }

    public static String formatOfferCategoryNameForDisplay(ResourceOfferCategory resourceOfferCategory) {
        String categoryName = resourceOfferCategory.getName();
        if (categoryNames.contains("resource_category_" + categoryName)) {
            return Translatable.of("resource_category_" + categoryName).defaultLocale().split(",")[1].trim();
        } else {
            return formatMaterialNameForDisplay(categoryName);
        }
    }

    public static String formatMaterialNameForDisplay(String materialName) {
        Material material = Material.getMaterial(materialName);
        if(material == null) {
            if(TownyResources.getPlugin().isSlimeFunInstalled()) {
                SlimefunItem slimefunItem = SlimefunItem.getById(materialName);
                if(slimefunItem != null) {
                    return slimefunItem.getItemName().replaceAll("[^\\w\\s]\\w","");
                }                
            }

            // mythicmobs integration
            if(TownyResources.getPlugin().isMythicMobsInstalled()) {
            	String mmName = MythicMobsUtil.getMaterialNameForDisplay(materialName); 
            	if (mmName != null) {
            		return mmName;
            	}
            }

            // MMOItems integration
            if (TownyResources.getPlugin().isMMOItemsInstalled() && materialName.contains(":")) {
            	String miName = MMOItemsUtil.getMaterialNameForDisplay(materialName);
            	if (miName != null)
            		return miName;
            }
        } else {
            if(TownyResources.getPlugin().isLanguageUtilsInstalled()) {           
                ItemStack fakeItemStack = new ItemStack(material);
                String translatedMaterialName = LanguageHelper.getItemDisplayName(fakeItemStack, TownyResourcesSettings.getMaterialsDisplayLanguage());
                return translatedMaterialName;
            }
        }
        
        materialName = Colors.strip(materialName);
        //Couldn't find a translation. Return un-translated material name
        return WordUtils.capitalizeFully(materialName.replaceAll("_", " "));
    }

	public static String adjustAmountsForTownLevelModifier(Town town, String productionAsString) {
        List<String> resourcesAsFormattedList = new ArrayList<>();
        String[] resourcesAsArray = productionAsString.split(",");
        String[] amountAndMaterialName;
        String amount;
        String materialName;
        double multiplier;
        for(String resourceAsString: resourcesAsArray) {
            if (resourceAsString.isEmpty())
                continue;
            amountAndMaterialName = resourceAsString.split("-");
            materialName = amountAndMaterialName[1];
            multiplier = TownyResourcesSettings.isNonDynamicAmountMaterial(materialName) ? 1.0 : TownySettings.getTownLevel(town).resourceProductionModifier(); 
            amount = String.valueOf((int) (Integer.valueOf(amountAndMaterialName[0]) * multiplier));
            resourcesAsFormattedList.add(amount + "-" + materialName);
        }
        return StringMgmt.join(resourcesAsFormattedList,",");
	}

}
