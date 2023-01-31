package io.github.townyadvanced.townyresources.listeners;

import com.palmergames.adventure.text.Component;
import com.palmergames.bukkit.towny.TownySettings;
import com.palmergames.bukkit.towny.event.statusscreen.TownStatusScreenEvent;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.Translator;
import com.palmergames.bukkit.towny.utils.TownyComponents;
import io.github.townyadvanced.townyresources.metadata.TownyResourcesGovernmentMetaDataController;
import io.github.townyadvanced.townyresources.settings.TownyResourcesSettings;
import io.github.townyadvanced.townyresources.util.TownyResourcesMessagingUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * 
 * @author Goosius
 *
 */
public class TownyResourcesTownEventListener implements Listener {

	/*
	 * TownyResources will add resource info to the town screen
	 */
	@EventHandler
	public void onTownStatusScreen(TownStatusScreenEvent event) {
		if (TownyResourcesSettings.isEnabled()) {
			Translator translator = Translator.locale(event.getCommandSender());
			Town town = event.getTown();
			String productionAsString = TownyResourcesGovernmentMetaDataController.getDailyProduction(town);
			String availableAsString = TownyResourcesGovernmentMetaDataController.getAvailableForCollection(town);

			if(productionAsString.isEmpty() && availableAsString.isEmpty())
				return;

			productionAsString = TownyResourcesMessagingUtil.adjustAmountsForTownLevelModifier(town, productionAsString);
			
			//Resources:
			Component component = Component.empty();
			component = component.append(Component.newline());
			component = component.append(TownyComponents.legacy(translator.of("townyresources.town.screen.header"))).appendNewline();

			// > Daily Productivity [2]: 32 oak Log, 32 sugar cane
			component = component.append(TownyResourcesMessagingUtil.getSubComponentForGovernmentScreens(translator, productionAsString, "townyresources.town.screen.daily.production")).appendNewline();

			// > Available For Collection [2]: 64 oak log, 64 sugar cane
			component = component.append(TownyResourcesMessagingUtil.getSubComponentForGovernmentScreens(translator, availableAsString, "townyresources.town.screen.available.for.collection")).appendNewline();
			// > TownLevel Modifier: +10%.
			if (TownySettings.getTownLevel(town).resourceProductionModifier() != 1.0)
				component = component.append(getTownModifierComponent(town, translator)).appendNewline();
			event.getStatusScreen().addComponentOf("TownyResources", component);
		}
	}

	private Component getTownModifierComponent(Town town, Translator translator) {
		double townModifier = TownySettings.getTownLevel(town).resourceProductionModifier();
		String modifierSlug = "";
		if (townModifier > 1.0)
			modifierSlug = "+" + BigDecimal.valueOf((townModifier - 1) * 100).setScale(2, RoundingMode.HALF_UP).doubleValue();
		if (townModifier < 1.0)
			modifierSlug = String.valueOf(BigDecimal.valueOf((townModifier * 100) - 100).setScale(2, RoundingMode.HALF_UP).doubleValue()); 
		return Component.text(translator.of("townyresources.town.screen.town.level.modifier", modifierSlug)).append(Component.text("%"));
	}
}
