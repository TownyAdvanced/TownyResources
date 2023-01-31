package io.github.townyadvanced.townyresources.listeners;

import com.palmergames.adventure.text.Component;
import com.palmergames.bukkit.towny.event.statusscreen.NationStatusScreenEvent;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Translator;
import com.palmergames.bukkit.towny.utils.TownyComponents;
import io.github.townyadvanced.townyresources.metadata.TownyResourcesGovernmentMetaDataController;
import io.github.townyadvanced.townyresources.settings.TownyResourcesSettings;
import io.github.townyadvanced.townyresources.util.TownyResourcesMessagingUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * 
 * @author Goosius
 *
 */
public class TownyResourcesNationEventListener implements Listener {

	/*
	 * TownyResources will add resource info to the town screen
	 */
	@EventHandler
	public void onNationStatusScreen(NationStatusScreenEvent event) {
		if (TownyResourcesSettings.isEnabled()) {
			Translator translator = Translator.locale(event.getCommandSender());
			Nation nation = event.getNation();
			String productionAsString = TownyResourcesGovernmentMetaDataController.getDailyProduction(nation);
			String availableAsString = TownyResourcesGovernmentMetaDataController.getAvailableForCollection(nation);

			if(productionAsString.isEmpty() && availableAsString.isEmpty())
				return;

			//Resources:
			Component component = Component.empty();
			component = component.append(Component.newline());
			component = component.append(TownyComponents.legacy(translator.of("townyresources.nation.screen.header"))).append(Component.newline());
		
			// > Daily Productivity [2]: 32 oak Log, 32 sugar cane
			component = component.append(TownyResourcesMessagingUtil.getSubComponentForGovernmentScreens(translator, productionAsString, "townyresources.nation.screen.daily.production")).append(Component.newline());
			
			// > Available For Collection [2]: 64 oak log, 64 sugar cane
			component = component.append(TownyResourcesMessagingUtil.getSubComponentForGovernmentScreens(translator, availableAsString, "townyresources.nation.screen.available.for.collection")).append(Component.newline());
			event.getStatusScreen().addComponentOf("TownyResources", component);
		}
	}
}
