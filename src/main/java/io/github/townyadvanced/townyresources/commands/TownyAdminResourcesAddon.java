package io.github.townyadvanced.townyresources.commands;

import com.palmergames.bukkit.towny.TownyCommandAddonAPI;
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.TownyCommandAddonAPI.CommandType;
import com.palmergames.bukkit.towny.command.BaseCommand;
import com.palmergames.bukkit.towny.confirmations.Confirmation;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.AddonCommand;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.Translatable;
import com.palmergames.bukkit.towny.object.Translator;
import com.palmergames.bukkit.towny.utils.NameUtil;
import com.palmergames.bukkit.util.ChatTools;
import com.palmergames.util.MathUtil;
import com.palmergames.util.StringMgmt;

import io.github.townyadvanced.townyresources.TownyResources;
import io.github.townyadvanced.townyresources.controllers.TownResourceDiscoveryController;
import io.github.townyadvanced.townyresources.enums.TownyResourcesPermissionNodes;
import io.github.townyadvanced.townyresources.metadata.BypassEntries;
import io.github.townyadvanced.townyresources.metadata.TownyResourcesGovernmentMetaDataController;
import io.github.townyadvanced.townyresources.util.TownyResourcesMessagingUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class TownyAdminResourcesAddon extends BaseCommand implements CommandExecutor, TabCompleter {
	
	public TownyAdminResourcesAddon() {
		AddonCommand townyAdminResourcesCommand = new AddonCommand(CommandType.TOWNYADMIN, "resources", this);
		TownyCommandAddonAPI.addSubCommand(townyAdminResourcesCommand);
	}

	private static final List<String> tabCompletes = Arrays.asList("reload", "reroll_all_resources", "bypass", "town");

	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (args.length == 1)
			return NameUtil.filterByStart(tabCompletes, args[0]);
		else if(args.length == 2) {
			switch (args[0].toLowerCase(Locale.ROOT)) {
			case "reroll_all_resources", "town":
				return getTownyStartingWith(args[1], "t");
			}
		}
		else if (args.length == 3 && args[0].toLowerCase(Locale.ROOT).equals("town")) {
			return Arrays.asList("setmultiplier");
		}
		else if (args.length == 4 && args[0].toLowerCase(Locale.ROOT).equals("town")) {
			return Arrays.asList("90","100","120","150","...");
		}
		return Collections.emptyList();
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (args.length > 0)
			parseAdminCommand(sender, args);
		else 
			showHelp(sender);
		return true;
	}

	private void parseAdminCommand(CommandSender sender, String[] args) {
		/*
		 * Parse Command.
		 */
	 	try {
			//This permission check handles all the perms checks
			if (sender instanceof Player)
				checkPermOrThrow(sender, TownyResourcesPermissionNodes.TOWNY_RESOURCES_ADMIN_COMMAND.getNode(args[0]));

			switch (args[0]) {
				case "reload" -> parseReloadCommand(sender);
				case "reroll_all_resources" -> parseReRollCommand(sender, StringMgmt.remFirstArg(args));
				case "bypass" -> bypassExtractionLimitCommand(sender);
				case "town" -> setTownMultiplier(sender, StringMgmt.remFirstArg(args));
				/*
				 * Show help if no command found.
				 */
				default -> showHelp(sender);
			}
		} catch (TownyException e) {
			TownyResourcesMessagingUtil.sendErrorMsg(sender, e.getMessage(sender));
		}
	}

	private void showHelp(CommandSender sender) {
		Translator translator = Translator.locale(sender);
		TownyMessaging.sendMessage(sender, ChatTools.formatTitle("/townyadmin resources"));
		TownyMessaging.sendMessage(sender, ChatTools.formatCommand("Eg", "/ta resources", "reload", translator.of("townyresources.admin_help_reload")));
		TownyMessaging.sendMessage(sender, ChatTools.formatCommand("Eg", "/ta resources", "reroll_all_resources", translator.of("townyresources.admin_help_reroll")));
		TownyMessaging.sendMessage(sender, ChatTools.formatCommand("Eg", "/ta resources", "reroll_all_resources [townname]", translator.of("townyresources.admin_help_reroll_one_town")));
		TownyMessaging.sendMessage(sender, ChatTools.formatCommand("Eg", "/ta resources", "bypass", translator.of("townyresources.admin_help_bypass")));
		TownyMessaging.sendMessage(sender, ChatTools.formatCommand("Eg", "/ta resources", "town [townname] setmultiplier [percent]", translator.of("townyresources.tra_town_setmultiplierhelp")));
		TownyMessaging.sendMessage(sender, ChatTools.formatCommand("Eg", "/ta resources", "town [townname] setmultiplier [100]", translator.of("townyresources.tra_town_setmultiplierhelp2")));
	}

	private void parseReloadCommand(CommandSender sender) {
		if (TownyResources.getPlugin().reloadAll()) {
			TownyResourcesMessagingUtil.sendMsg(sender, Translatable.of("townyresources.townyresources_reloaded_successfully"));
			return;
		}
		TownyResourcesMessagingUtil.sendErrorMsg(sender, Translatable.of("townyresources.townyresources_failed_to_reload"));
	}

	private void parseReRollCommand(CommandSender sender, String[] args) throws TownyException {
		if (args.length == 0) {
			Confirmation.runOnAcceptAsync(() -> {
				TownResourceDiscoveryController.reRollAllExistingResources();
				TownyResourcesMessagingUtil.sendGlobalMessage(Translatable.of("townyresources.all_resources_rerolled"));
			})
			.setTitle(Translatable.of("townyresources.msg_confirm_reroll"))
			.sendTo(sender);
			return;
		}
		
		Town town = getTownOrThrow(args[0]);
		Confirmation.runOnAcceptAsync(() -> {
			TownResourceDiscoveryController.reRollExistingResources(town, false);
			TownyMessaging.sendPrefixedTownMessage(town, Translatable.of("townyresources.all_resources_rerolled"));
			TownyResourcesMessagingUtil.sendMsg(sender, Translatable.of("townyresources.all_resources_rerolled"));
		})
		.setTitle(Translatable.of("townyresources.msg_confirm_reroll_town"))
		.sendTo(sender);
	}

	private void bypassExtractionLimitCommand(CommandSender sender) {
		UUID playerUUID = ((Player) sender).getUniqueId();

		if (BypassEntries.bypassData.contains(playerUUID)) {
			BypassEntries.bypassData.remove(playerUUID);
			TownyResourcesMessagingUtil.sendMsg(sender, Translatable.of("townyresources.bypass_off"));
		} else {
			BypassEntries.bypassData.add(playerUUID);
			TownyResourcesMessagingUtil.sendMsg(sender, Translatable.of("townyresources.bypass_on"));
		}
	}

	private void setTownMultiplier(CommandSender sender, String[] args) throws TownyException {

		if (args.length < 3) {
			showHelp(sender);
			return;
		}
		
		Town town = getTownOrThrow(args[0]);
		int multiplier = MathUtil.getPositiveIntOrThrow(args[2]);
		TownyResourcesGovernmentMetaDataController.setTownMulitplier(town, multiplier);
		TownyResourcesMessagingUtil.sendMsg(sender, Translatable.of("townyresources.tra_town_multiplier_set", town.getName(), multiplier));
	}
}

