package io.github.townyadvanced.townyresources.commands;

import com.gmail.goosius.siegewar.Messaging;
import com.gmail.goosius.siegewar.settings.Translation;
import com.gmail.goosius.siegewar.utils.SiegeWarMoneyUtil;
import com.palmergames.bukkit.towny.TownyEconomyHandler;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.WorldCoord;
import com.palmergames.bukkit.towny.utils.NameUtil;
import com.palmergames.bukkit.util.ChatTools;
import io.github.townyadvanced.townyresources.controllers.TownProductionController;
import io.github.townyadvanced.townyresources.enums.TownyResourcesPermissionNodes;
import io.github.townyadvanced.townyresources.objects.ResourceQuantity;
import io.github.townyadvanced.townyresources.settings.TownyResourcesSettings;
import io.github.townyadvanced.townyresources.settings.TownyResourcesTranslation;
import io.github.townyadvanced.townyresources.util.TownyResourcesMessagingUtil;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TownyResourcesCommand implements CommandExecutor, TabCompleter {
	
	private static final List<String> townyResourcesTabCompletes = Arrays.asList("survey", "collect");
	
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (args.length == 1)
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
		try {
			//This permission check handles all the perms checks
			if (!player.hasPermission(TownyResourcesPermissionNodes.TOWNY_RESOURCES_COMMAND.getNode(args[0]))) {
				Messaging.sendErrorMsg(player, Translation.of("msg_err_command_disable"));
				return;
			}
			switch (args[0]) {
				case "survey":
					parseSurveyCommand(player);
					break;
				case "collect":
					//parseSiegeWarHudCommand(player, StringMgmt.remFirstArg(args));
					break;
				default:
					showTownyResourcesHelp(player);
			}		
		} catch (TownyException e) {
			TownyResourcesMessagingUtil.sendErrorMsg(player, e.getMessage());
		}
	}
	
	private void parseSurveyCommand(Player player) throws TownyException{
		WorldCoord playerWorldCoord = WorldCoord.parseWorldCoord(player);

		//Check if there is a town here
		if(!playerWorldCoord.hasTownBlock())
			throw new TownyException(TownyResourcesTranslation.of("msg_err_survey_no_town"));

		//Check if there are resources left to discover at the town
		Town town = playerWorldCoord.getTownBlock().getTown();
		List<Material> discoveredResources = TownProductionController.getDiscoveredResources(town);
		List<Integer> costPerResourceLevel = TownyResourcesSettings.getSurveyCostsPerResourceLevel();
		List<Integer> townblocksRequirementPerResourceLevel = TownyResourcesSettings.getSurveyNumTownblocksRequirementsPerResourceLevel();
		if(discoveredResources.size() >= costPerResourceLevel.size())
			throw new TownyException(TownyResourcesTranslation.of("msg_err_survey_all_resources_already_discovered"));
		if(discoveredResources.size() >= townblocksRequirementPerResourceLevel.size())
			throw new TownyException(TownyResourcesTranslation.of("msg_err_survey_all_resources_already_discovered"));
		
		//Check if the town has enough townblocks
		int levelToDiscover = discoveredResources.size() + 1;
		int townblocksRequirement = townblocksRequirementPerResourceLevel.get(levelToDiscover);
		if(town.getTownBlocks().size() < townblocksRequirement)
			throw new TownyException(TownyResourcesTranslation.of("msg_err_survey_not_enough_townblocks"));
		
		//Check that the player can afford the survey
		double surveyCost = costPerResourceLevel.get(levelToDiscover);
		Resident resident = TownyUniverse.getInstance().getResident(player.getUniqueId());
		if (TownyEconomyHandler.isActive() && !resident.getAccount().canPayFromHoldings(surveyCost))
			throw new TownyException(Translation.of("msg_err_survey_too_expensive"));

		//Pay for the survey
		resident.getAccount().withdraw(surveyCost, "Cost of resources survey.");
				
		//Discover a new resource (notification will be sent from here)
		TownProductionController.discoverNewResource(resident, town, levelToDiscover);
	}
}