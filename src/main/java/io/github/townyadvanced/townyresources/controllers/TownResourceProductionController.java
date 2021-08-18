package io.github.townyadvanced.townyresources.controllers;

import com.gmail.goosius.siegewar.TownOccupationController;
import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.TownySettings;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import io.github.townyadvanced.townyresources.TownyResources;
import io.github.townyadvanced.townyresources.metadata.TownyResourcesGovernmentMetaDataController;
import io.github.townyadvanced.townyresources.objects.ResourceOffer;
import io.github.townyadvanced.townyresources.settings.TownyResourcesSettings;
import io.github.townyadvanced.townyresources.settings.TownyResourcesTranslation;
import io.github.townyadvanced.townyresources.util.TownyResourcesMessagingUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class TownResourceProductionController {

    /**
     * Recalculate production for all towns and nations
     */
    public static void recalculateAllProduction() {
        Map<String, ResourceOffer> allResourceOffers = TownyResourcesSettings.getAllResourceOffers();
        recalculateProductionForAllTowns(allResourceOffers);
        recalculateProductionForAllNations(allResourceOffers);
    }

    /**
     * Recalculate production for all towns
     * 
     * Note: This method does not recalculate production for any nations
     */
    private static void recalculateProductionForAllTowns(Map<String, ResourceOffer> allResourceOffers) {
        for(Town town: TownyUniverse.getInstance().getTowns()) {           
            recalculateProductionForOneTown(town, allResourceOffers);
        }
    }

    /**
     * Recalculate production for a single town
     * 
     * Note: This method does not recalculate the nation production
     *         
     * @param town the town to recalculate production for
     * @param allResourceOffers all resource offers
     */
    static void recalculateProductionForOneTown(Town town, Map<String, ResourceOffer> allResourceOffers) {
        try {
            //Get discovered resources
            List<String> discoveredResources = new ArrayList<>(TownyResourcesGovernmentMetaDataController.getDiscoveredAsList(town));
    
            //Remove any discovered resources which are no longer on offer
            List<String> resourcesToRemove = new ArrayList<>();
            for(String resource: discoveredResources) {
                if(!allResourceOffers.containsKey(resource))
                    resourcesToRemove.add(resource);
            }
            if(!resourcesToRemove.isEmpty()) {
                discoveredResources.removeAll(resourcesToRemove);
                TownyResourcesGovernmentMetaDataController.setDiscovered(town, discoveredResources);
                town.save();
            }
    
            //Determine town cut
            double townCutNormalized;
            if(town.hasNation() || TownOccupationController.isTownOccupied(town)) {
                townCutNormalized = 1 - TownyResourcesSettings.getTownResourcesProductionNationTaxNormalized();
            } else {
                townCutNormalized = 1;
            }
    
            //Build the town production map
            Map<String, Integer> townProduction = calculateProduction(allResourceOffers, town, townCutNormalized);
    
            //Save data
            TownyResourcesGovernmentMetaDataController.setDailyProduction(town, townProduction);    
            town.save();            
        } catch (Exception e) {
            TownyResources.severe("Problem recalculating production for town" + town.getName());
            e.printStackTrace();
        }
    }

    /**
     * Recalculate production for all nations
     * 
     * Note: This method does not recalculate production for any towns
     */
    private static void recalculateProductionForAllNations(Map<String, ResourceOffer> allResourceOffers) {
        for(Nation nation: TownyUniverse.getInstance().getNations()) {
            recalculateProductionForOneNation(nation, allResourceOffers);
        }
    }

    /**
     * Recalculate production for a single nation
     *         
     * Note: This method does not recalculate production for any towns

     * @param nation the nation to recalculate production for
     * @param allResourceOffers all resource offers
     */
    static void recalculateProductionForOneNation(Nation nation, Map<String, ResourceOffer> allResourceOffers) {
        double nationCutNormalized = TownyResourcesSettings.getTownResourcesProductionNationTaxNormalized();
        List<Town> occupiedForeignTowns = TownOccupationController.getOccupiedForeignTowns(nation);
        List<Town> occupiedHomeTowns = TownOccupationController.getOccupiedHomeTowns(nation);
        Map<String,Integer> nationProduction = new HashMap<>();
        List<Town> townsToTakeFrom = new ArrayList<>();
        Map<String,Integer> resourcesTakenFromTown;
        String takenResource;
        int takenQuantity;
        
        //Build list of towns to take from
        for(Town town: nation.getTowns()) {
            if(!occupiedHomeTowns.contains(town) || TownOccupationController.getTownOccupier(town) == nation) {
                townsToTakeFrom.add(town);
            }
        }
        townsToTakeFrom.addAll(occupiedForeignTowns);
        
        //Take resources from towns and give to nation
        for(Town town: townsToTakeFrom) {
            //Take resources from town
            resourcesTakenFromTown = calculateProduction(allResourceOffers, town, nationCutNormalized);
            //Add resources to nation
            for(Map.Entry<String, Integer> resourceTakenFromTown: resourcesTakenFromTown.entrySet()) {
                takenResource = resourceTakenFromTown.getKey();
                takenQuantity = resourceTakenFromTown.getValue();
                if(nationProduction.containsKey(takenResource)) {
                    nationProduction.put(takenResource, nationProduction.get(takenResource) + takenQuantity);
                } else {
                    nationProduction.put(takenResource, takenQuantity);
                }
            }
        }
        
        //Set nation production & save
        TownyResourcesGovernmentMetaDataController.setDailyProduction(nation, nationProduction);
        nation.save();
    }


    /**
     * Utility Method 
     * Calculate Production
     * This can be used for a town or nation
     * 
     * @param allResourceOffers all resource offers
     * @param town the town producing the resource
     * @param cutNormalized the cut of the resource to return
     * @return the production as a map, with each value multiplied by the given cutNormalized value
     */
    private static Map<String, Integer> calculateProduction(Map<String, ResourceOffer> allResourceOffers, Town town, double cutNormalized) {        
        //Get discovered resources
        List<String> discoveredResources = new ArrayList<>(TownyResourcesGovernmentMetaDataController.getDiscoveredAsList(town));

        //Get configured resource level bonuses
        List<Double> normalizedBonusesPerResourceLevel = TownyResourcesSettings.getNormalizedProductionBonusesPerResourceLevel();

        //Calculate the production
        Map<String, Integer> production = new HashMap<>();
        String materialName;
        double baseProducedAmount;
        int finalProducedAmount;

        for(int i = 0; i < discoveredResources.size(); i++) {
            //Ensure town meets the town level requirement to produce the resource
            if(TownySettings.calcTownLevel(town) <  TownyResourcesSettings.getProductionTownLevelRequirementPerResourceLevel().get(i)) 
                break;
            materialName = discoveredResources.get(i);
            baseProducedAmount = allResourceOffers.get(materialName).getBaseAmount();
            finalProducedAmount = (int)((baseProducedAmount * normalizedBonusesPerResourceLevel.get(i) * cutNormalized) + 0.5);
            production.put(materialName, finalProducedAmount);
        }
            
        return production;
    }

    /////////////////////// PRODUCING THE RESOURCES ////////////////////////////////

    /**
     * Produce all resources
     */
    public static void produceAllResources() {
        produceAllResourcesForAllTowns();
        produceAllResourcesForAllNations();
    }

    /**
     * Produce resources of all towns
     */
    private static void produceAllResourcesForAllTowns() {
        int numProducingTowns = 0;
        for(Town town: TownyUniverse.getInstance().getTowns()) {
            if(produceResourcesForOneTown(town))
                numProducingTowns++;
        }
        TownyResourcesMessagingUtil.sendGlobalMessage(TownyResourcesTranslation.of("production.message", numProducingTowns));        
    }

    /**
     * Produce resources for just one town
     * 
     * @param town the town
     * @return true if any resources were extracted
     */
    private static boolean produceResourcesForOneTown(Town town) {
        try {
            //Get daily production
            Map<String, Integer> townDailyProduction = TownyResourcesGovernmentMetaDataController.getDailyProductionAsMap(town);
    
            if(townDailyProduction.isEmpty())
                return false;
                
            //Get the list of resources which are already available for collection
            Map<String,Integer> availableResources = TownyResourcesGovernmentMetaDataController.getAvailableForCollectionAsMap(town);
    
            //Get storage Limit modifier
            int storageLimitModifier = TownyResourcesSettings.getStorageLimitModifier();
            
            //Extract resources
            String resource;
            int quantityToExtract;
            int currentQuantity;
            int storageLimit;
            for(Map.Entry<String, Integer> townProductionEntry: townDailyProduction.entrySet()) {
                resource = townProductionEntry.getKey();
                quantityToExtract =townProductionEntry.getValue();
                if(availableResources.containsKey(resource)) {
                    //Don't go over limit
                    currentQuantity = availableResources.get(resource);
                    storageLimit = quantityToExtract * storageLimitModifier;
                    if(currentQuantity == storageLimit) {
                        continue; //Already at limit
                    } else if (currentQuantity + quantityToExtract > storageLimit) {
                        quantityToExtract = storageLimit - currentQuantity; 
                    }                       
                    //Add to existing available resources
                    availableResources.put(resource, currentQuantity + quantityToExtract);
                } else {
                    //Add new available resource
                    availableResources.put(resource, quantityToExtract);
                }
            }
    
            //Set the list of available resources
            TownyResourcesGovernmentMetaDataController.setAvailableForCollection(town, availableResources);    
            
            //Save town
            town.save();                       
            } catch (Exception e) {
                TownyResources.severe("Problem extracting resources for town " + town.getName());
                e.printStackTrace();
                return false;
            }

        //Some resources were extracted. Return true;
        return true;
    }

    private static void produceAllResourcesForAllNations() {
    }

}
