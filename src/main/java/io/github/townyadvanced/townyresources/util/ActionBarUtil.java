package io.github.townyadvanced.townyresources.util;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * FYI if the MC version is not high enough this class may fail to load with a "java.lang.noClassDefFound" error
 * But its ok because TownyResources always sends a text window message anyway
 */
public class ActionBarUtil {

    public static void sendActionBarErrorMessage(Player player, String text) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_RED + text));
    }
}
