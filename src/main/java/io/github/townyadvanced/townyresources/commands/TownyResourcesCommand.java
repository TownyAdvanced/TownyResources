package io.github.townyadvanced.townyresources.commands;

import com.gmail.goosius.siegewar.Messaging;
import com.gmail.goosius.siegewar.SiegeController;
import com.gmail.goosius.siegewar.SiegeWar;
import com.gmail.goosius.siegewar.TownOccupationController;
import com.gmail.goosius.siegewar.command.SiegeWarAdminCommand;
import com.gmail.goosius.siegewar.enums.SiegeWarPermissionNodes;
import com.gmail.goosius.siegewar.metadata.ResidentMetaDataController;
import com.gmail.goosius.siegewar.settings.SiegeWarSettings;
import com.gmail.goosius.siegewar.settings.Translation;
import com.gmail.goosius.siegewar.utils.BookUtil;
import com.gmail.goosius.siegewar.utils.CosmeticUtil;
import com.gmail.goosius.siegewar.utils.SiegeWarMoneyUtil;
import com.palmergames.bukkit.towny.*;
import com.palmergames.bukkit.towny.confirmations.Confirmation;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Coord;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.permissions.TownyPerms;
import com.palmergames.bukkit.towny.utils.NameUtil;
import com.palmergames.bukkit.util.BukkitTools;
import com.palmergames.bukkit.util.ChatTools;
import com.palmergames.util.StringMgmt;
import io.github.townyadvanced.townyresources.enums.TownyResourcesPermissionNodes;
import io.github.townyadvanced.townyresources.settings.TownyResourcesTranslation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

public class TownyResourcesCommand implements CommandExecutor, TabCompleter {
	
	private static final List<String> townyResourcesTabCompletes = Arrays.asList("survey", "collect");
	
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
/*
		switch (args[0].toLowerCase()) {
			case "survey":
				//Survey the town you are currently standing in
				break;
			case "collect":
				//Collect available materials from your town and/or nation
				break;
		}
*/		if (args.length == 1)
			return NameUtil.filterByStart(townyResourcesTabCompletes, args[0]);
		else
			return Collections.emptyList();
	}

	private void showTownyResourcesHelp(CommandSender sender) {
		sender.sendMessage(ChatTools.formatTitle("/townyresources"));
		sender.sendMessage(ChatTools.formatCommand("Eg", "/tr collect", "", TownyResourcesTranslation.of("help_collect")));
		sender.sendMessage(ChatTools.formatCommand("Eg", "/tr survey", "", TownyResourcesTranslation.of("help_survey")));
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender instanceof Player && args.length > 0)
			parseTownyResourcesCommand((Player) sender, args);
		else 
			showTownyResourcesHelp(sender);
		return true;
	}

	private void parseTownyResourcesCommand(Player player, String[] args) {

		//This permission check handles all the perms checks
		if (!player.hasPermission(TownyResourcesPermissionNodes.TOWNY_RESOURCES_COMMAND.getNode(args[0]))) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_command_disable"));
			return;
		}
			
		switch (args[0]) {
		case "survey":
			//parseSiegeWarCollectCommand(player);
			break;
		case "collect":
			//parseSiegeWarHudCommand(player, StringMgmt.remFirstArg(args));
			break;
		default:
			showTownyResourcesHelp(player);
		}
	}
}