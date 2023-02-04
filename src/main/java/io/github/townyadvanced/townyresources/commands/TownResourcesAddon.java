package io.github.townyadvanced.townyresources.commands;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyCommandAddonAPI;
import com.palmergames.bukkit.towny.TownyEconomyHandler;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.TownyCommandAddonAPI.CommandType;
import com.palmergames.bukkit.towny.command.BaseCommand;
import com.palmergames.bukkit.towny.confirmations.Confirmation;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.AddonCommand;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.Translatable;
import com.palmergames.bukkit.towny.object.Translator;
import com.palmergames.bukkit.towny.utils.NameUtil;
import com.palmergames.bukkit.util.ChatTools;
import io.github.townyadvanced.townyresources.controllers.TownResourceCollectionController;
import io.github.townyadvanced.townyresources.controllers.TownResourceDiscoveryController;
import io.github.townyadvanced.townyresources.enums.TownyResourcesPermissionNodes;
import io.github.townyadvanced.townyresources.metadata.TownyResourcesGovernmentMetaDataController;
import io.github.townyadvanced.townyresources.settings.TownyResourcesSettings;
import io.github.townyadvanced.townyresources.util.TownyResourcesMessagingUtil;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TownResourcesAddon extends BaseCommand implements TabExecutor {
	
	public TownResourcesAddon() {
		AddonCommand townResourcesCommand = new AddonCommand(CommandType.TOWN, "resources", this);
		TownyCommandAddonAPI.addSubCommand(townResourcesCommand);
	}

	private static final List<String> townyResourcesTabCompletes = Arrays.asList("survey", "collect");
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (args.length == 1)
			return NameUtil.filterByStart(townyResourcesTabCompletes, args[0]);
		else
			return Collections.emptyList();
	}

	private void showTownResourcesHelp(CommandSender sender) {
		Translator translator = Translator.locale(sender);
		sender.sendMessage(ChatTools.formatTitle("/town resources"));
		sender.sendMessage(ChatTools.formatCommand("Eg", "/t resources", "survey", translator.of("townyresources.help_survey")));
		sender.sendMessage(ChatTools.formatCommand("Eg", "/t resources", "collect", translator.of("townyresources.help_towncollect")));
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender instanceof Player player && args.length > 0)
			parseTownResourcesCommand(player, args);
		else 
			showTownResourcesHelp(sender);
		return true;
	}

	private void parseTownResourcesCommand(Player player, String[] args) {
		try {

			switch (args[0].toLowerCase(Locale.ROOT)) {
			case "survey" -> parseSurveyCommand(player);
			case "collect" -> parseTownCollectCommand(player);
			default -> showTownResourcesHelp(player);
			}

		} catch (TownyException te) {
			//Expected type of exception (e.g. not enough money)
			TownyResourcesMessagingUtil.sendErrorMsg(player, te.getMessage(player));
		} catch (Exception e) {
			//Unexpected exception
			TownyResourcesMessagingUtil.sendErrorMsg(player, e.getMessage());
		}
	}

	private void parseSurveyCommand(Player player) throws TownyException{
		checkPermOrThrow(player, TownyResourcesPermissionNodes.TOWNY_RESOURCES_COMMAND_SURVEY.getNode());

		//Check if surveys are enabled
		if(!TownyResourcesSettings.areSurveysEnabled())
			throw new TownyException(Translatable.of("msg_err_command_disable"));

		Town town = TownyAPI.getInstance().getTown(player.getLocation());
		//Check if there is a town here
		if(town == null)
			throw new TownyException(Translatable.of("townyresources.msg_err_survey_no_town"));

		if (!town.hasResident(player))
			throw new TownyException(Translatable.of("townyresources.not_your_town"));

		//Check if there are resources left to discover at the town
		List<String> discoveredResources = TownyResourcesGovernmentMetaDataController.getDiscoveredAsList(town);
		List<Integer> costPerResourceLevel = TownyResourcesSettings.getSurveyCostsPerResourceLevel();
		List<Integer> requiredNumTownblocksPerResourceLevel = TownyResourcesSettings.getSurveyNumTownblocksRequirementsPerResourceLevel();
		if(discoveredResources.size() >= costPerResourceLevel.size())
			throw new TownyException(Translatable.of("townyresources.msg_err_survey_all_resources_already_discovered"));
		if(discoveredResources.size() >= requiredNumTownblocksPerResourceLevel.size())
			throw new TownyException(Translatable.of("townyresources.msg_err_survey_all_resources_already_discovered"));
		
		//Check if the town has enough townblocks
		int indexOfNextResourceLevel = discoveredResources.size();
		int requiredNumTownblocks = requiredNumTownblocksPerResourceLevel.get(indexOfNextResourceLevel);
		int currentNumTownblocks = town.getTownBlocks().size();
		if(currentNumTownblocks < requiredNumTownblocks)
			throw new TownyException(Translatable.of("townyresources.msg_err_survey_not_enough_townblocks", 
				requiredNumTownblocks, currentNumTownblocks));
		
		//Get survey level & cost
		int surveyLevel = indexOfNextResourceLevel+1;
		double surveyCost = costPerResourceLevel.get(indexOfNextResourceLevel);

		//Send confirmation request message
		String surveyCostFormatted = "0";
		if(TownyEconomyHandler.isActive())
			surveyCostFormatted = TownyEconomyHandler.getFormattedBalance(surveyCost);

		TownyResourcesMessagingUtil.sendMsg(player, Translatable.of("townyresources.msg_confirm_survey", town.getName(), surveyLevel, surveyCostFormatted));

		//Send warning message if town level is too low
		int requiredTownLevel = TownyResourcesSettings.getProductionTownLevelRequirementPerResourceLevel().get(indexOfNextResourceLevel);
		int actualTownLevel = town.getLevel();
		if(actualTownLevel < requiredTownLevel) {
			TownyResourcesMessagingUtil.sendMsg(player, Translatable.of("townyresources.msg_confirm_survey_town_level_warning", requiredTownLevel, actualTownLevel));
		}
		Resident resident = TownyUniverse.getInstance().getResident(player.getUniqueId());
		Confirmation.runOnAcceptAsync(() -> {
			try {
				TownResourceDiscoveryController.discoverNewResource(resident, town, surveyLevel, surveyCost, discoveredResources);
			} catch (TownyException te) {
				TownyResourcesMessagingUtil.sendErrorMsg(player, te.getMessage(player));
			}
		})
		.sendTo(player);
	}
	
	private static void parseTownCollectCommand(Player player) throws TownyException {
		checkPermOrThrow(player, TownyResourcesPermissionNodes.TOWNY_RESOURCES_COMMAND_TOWN_COLLECT.getNode());

		//Ensure player a town member
		Resident resident = TownyAPI.getInstance().getResident(player.getUniqueId());
		if(!resident.hasTown()) 
			throw new TownyException(Translatable.of("townyresources.msg_err_cannot_towncollect_not_a_town_member"));
		
		//Ensure player is actually in their own town
		Town town = TownyAPI.getInstance().getTown(player.getLocation());
		if(town == null || !town.hasResident(resident))
			throw new TownyException(Translatable.of("townyresources.msg_err_cannot_towncollect_not_in_own_town"));
		
		//Ensure some resources are available
		Map<String, Integer> availableForCollection = TownyResourcesGovernmentMetaDataController.getAvailableForCollectionAsMap(town);
		if(availableForCollection.isEmpty())
			throw new TownyException(Translatable.of("townyresources.msg_err_cannot_towncollect_no_resources_available"));
		
		//Collect resources
		TownResourceCollectionController.collectAvailableTownResources(player, town, availableForCollection);
	}}