package io.github.townyadvanced.townyresources.util;

import org.bukkit.entity.Player;

import com.palmergames.bukkit.towny.TownyMessaging;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

/**
 * FYI if the MC version is not high enough this class may fail to load with a "java.lang.noClassDefFound" error
 * But its ok because TownyResources always sends a text window message anyway
 */
public class ActionBarUtil {

    public static void sendActionBarErrorMessage(Player player, String text) {
        TownyMessaging.sendActionBarMessageToPlayer(player, Component.text(text).color(NamedTextColor.DARK_RED));
    }
}
