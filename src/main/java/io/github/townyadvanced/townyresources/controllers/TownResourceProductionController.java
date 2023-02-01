package io.github.townyadvanced.townyresources.controllers;

import com.gmail.goosius.siegewar.TownOccupationController;
import com.palmergames.bukkit.towny.TownySettings;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Government;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.Translatable;
import io.github.townyadvanced.townyresources.TownyResources;
import io.github.townyadvanced.townyresources.metadata.TownyResourcesGovernmentMetaDataController;
import io.github.townyadvanced.townyresources.objects.ResourceOfferCategory;
import io.github.townyadvanced.townyresources.settings.TownyResourcesSettings;
import io.github.townyadvanced.townyresources.util.TownyResourcesMessagingUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TownResourceProductionController {

    /**
     * Recalculate production for all towns and nations
     */
    public static void recalculateAllProduction() {
        recalculateProductionForAllTowns();
        recalculateProductionForAllNations();
    }

    /**
     * Recalculate production for all towns
     * 
     * Note: This method does not recalculate production for any nations
     */
    private static void recalculateProductionForAllTowns() {
        for(Town town: TownyUniverse.getInstance().getTowns()) {           
            recalculateProductionForOneTown(town);
        }
    }

    /**
     * Recalculate production for a single town
     * 
     * Note: This method does not recalculate the nation production
     *         
     * @param town the town to recalculate production for
     */
    static void recalculateProductionForOneTown(Town town) {
        try {
            //Get discovered resources
            List<String> discoveredResources = new ArrayList<>(TownyResourcesGovernmentMetaDataController.getDiscoveredAsList(town));
    
            //Remove any discovered resources which are no longer on offer
            Map<String, ResourceOfferCategory> allOffers = TownResourceOffersController.getMaterialToResourceOfferCategoryMap();
            List<String> resourcesToRemove = new ArrayList<>();
            for(String resource: discoveredResources) {
                if(!allOffers.containsKey(resource)) {
                    resourcesToRemove.add(resource);
                }
            }
            if(!resourcesToRemove.isEmpty()) {
                discoveredResources.removeAll(resourcesToRemove);
                TownyResourcesGovernmentMetaDataController.setDiscovered(town, discoveredResources);
                town.save();
            }

            //Determine town cut
            double townCutNormalized = calculateTownCutNormalized(town);

            //Build the town production map
            Map<String, Integer> townProduction = calculateProduction(town, townCutNormalized);
    
            //Save data
            TownyResourcesGovernmentMetaDataController.setDailyProduction(town, townProduction);    
            town.save();            
        } catch (Exception e) {
            TownyResources.severe("Problem recalculating production for town" + town.getName());
            e.printStackTrace();
        }
    }

    private static double calculateTownCutNormalized(Town town) {
        if(TownyResources.getPlugin().isSiegeWarInstalled()
            && TownOccupationController.isTownOccupied(town)) {
            return 1 - TownyResourcesSettings.getTownResourcesProductionOccupyingNationTaxNormalized();
        } else if (town.hasNation()) {
            return 1 - TownyResourcesSettings.getTownResourcesProductionNationTaxNormalized();
        } else {
            return 1;
        }
    }

    /**
     * Recalculate production for all nations
     * 
     * Note: This method does not recalculate production for any towns
     */
    private static void recalculateProductionForAllNations() {
        for(Nation nation: TownyUniverse.getInstance().getNations()) {
            recalculateProductionForOneNation(nation);
        }
    }

    /**
     * Recalculate production for a single nation
     *         
     * Note: This method does not recalculate production for any towns

     * @param nation the nation to recalculate production for
     */
    static void recalculateProductionForOneNation(Nation nation) {
        //Setup Variables
        Map<String,Integer> nationProduction = new HashMap<>();
        double nationCutNormalized;
        Map<String,Integer> resourcesTakenFromTown;
        String takenResource;
        int takenQuantity;

        //1. Take resources from natural towns, and give to nation
        for(Town town: nation.getTowns()) {
            //Calculate Nation Cut
            if(TownyResources.getPlugin().isSiegeWarInstalled()
               && TownOccupationController.isTownOccupied(town)) {
                if(TownOccupationController.getTownOccupier(town) == nation) {
                    //Town occupied by nation
                    nationCutNormalized = TownyResourcesSettings.getTownResourcesProductionOccupyingNationTaxNormalized();
                } else {
                    //Town occupied by foreign nation
                    continue;
                }
            } else {
                //Town not occupied
                nationCutNormalized = TownyResourcesSettings.getTownResourcesProductionNationTaxNormalized();
            }

            //Take resources from town
            resourcesTakenFromTown = calculateProduction(town, nationCutNormalized);
            //Add resources to nation
            for(Map.Entry<String, Integer> resourceTakenFromTown: resourcesTakenFromTown.entrySet()) {
                takenResource = resourceTakenFromTown.getKey();
                takenQuantity = resourceTakenFromTown.getValue();
                if(takenQuantity == 0)
                    continue;
                if(nationProduction.containsKey(takenResource)) {
                    nationProduction.put(takenResource, nationProduction.get(takenResource) + takenQuantity);
                } else {
                    nationProduction.put(takenResource, takenQuantity);
                }
            }
        }

        //2. Take resources from occupied towns, and give to nation
        if(TownyResources.getPlugin().isSiegeWarInstalled()) {
            nationCutNormalized = TownyResourcesSettings.getTownResourcesProductionOccupyingNationTaxNormalized();
            for(Town town: TownOccupationController.getOccupiedForeignTowns(nation)) {
                //Take resources from town
                resourcesTakenFromTown = calculateProduction(town, nationCutNormalized);
                //Add resources to nation
                for(Map.Entry<String, Integer> resourceTakenFromTown: resourcesTakenFromTown.entrySet()) {
                    takenResource = resourceTakenFromTown.getKey();
                    takenQuantity = resourceTakenFromTown.getValue();
                    if(takenQuantity == 0)
                        continue;
                    if(nationProduction.containsKey(takenResource)) {
                        nationProduction.put(takenResource, nationProduction.get(takenResource) + takenQuantity);
                    } else {
                        nationProduction.put(takenResource, takenQuantity);
                    }
                }
            }
        }
        
        //3. Set nation production & save
        TownyResourcesGovernmentMetaDataController.setDailyProduction(nation, nationProduction);
        nation.save();
    }


    /**
     * Utility Method 
     * Calculate Production
     * This can be used for a town or nation
     * 
     * @param town the town producing the resource
     * @param cutNormalized the cut of the resource to return
     * @return the production as a map, with each value multiplied by the given cutNormalized value
     */
    private static Map<String, Integer> calculateProduction(Town town, double cutNormalized) {        
        //Get all offers
        Map<String, ResourceOfferCategory> allOffers = TownResourceOffersController.getMaterialToResourceOfferCategoryMap();
        
        //Get discovered resources
        List<String> discoveredResources = new ArrayList<>(TownyResourcesGovernmentMetaDataController.getDiscoveredAsList(town));

        //Get configured resource level bonuses
        List<Double> normalizedBonusesPerResourceLevel = TownyResourcesSettings.getNormalizedProductionBonusesPerResourceLevel();

        //Calculate the production
        Map<String, Integer> production = new HashMap<>();
        String material;
        double baseProducedAmount;
        double bonusesPerResourceLevel;
        int finalProducedAmount;

        for(int i = 0; i < discoveredResources.size(); i++) {
            material = discoveredResources.get(i);
            //If town does not meet the min level, produced amt is zero
            if(town.getLevel() < TownyResourcesSettings.getProductionTownLevelRequirementPerResourceLevel().get(i)) {
                finalProducedAmount = 0;
            } else {
                baseProducedAmount = allOffers.get(material).getBaseAmountItems();
                bonusesPerResourceLevel = TownyResourcesSettings.isNonDynamicAmountMaterial(material) ? 1.0 : normalizedBonusesPerResourceLevel.get(i);
                finalProducedAmount = (int)((baseProducedAmount * bonusesPerResourceLevel * cutNormalized) + 0.5);
            }
            production.put(material, finalProducedAmount);
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
            if(produceResourcesForOneGovernment(town))
                numProducingTowns++;
        }
        TownyResourcesMessagingUtil.sendGlobalMessage(Translatable.of("townyresources.production.message", numProducingTowns));        
    }

    /**
     * Produce resources of all nations
     */
    private static void produceAllResourcesForAllNations() {
        for(Nation nation: TownyUniverse.getInstance().getNations()) {
            produceResourcesForOneGovernment(nation);
        }
    }

    /**
     * Utility Method 
     * Produce resources for just one government (i.e either a town or nation)
     * 
     * @param government the government to produce resources
     * @return true if any resources were produced
     */
    private static boolean produceResourcesForOneGovernment(Government government) {
        try {
            //Get daily production
            Map<String, Integer> townDailyProduction = TownyResourcesGovernmentMetaDataController.getDailyProductionAsMap(government);
    
            if(townDailyProduction.isEmpty())
                return false;
                
            //Get the list of resources which are already available for collection
            Map<String,Integer> availableResources = TownyResourcesGovernmentMetaDataController.getAvailableForCollectionAsMap(government);
    
            //Get storage Limit modifier
            int storageLimitModifier = TownyResourcesSettings.getStorageLimitModifier();
            
            //Produce resources
            String resource;
            int quantityToProduce;
            int currentQuantity;
            double townLevelModifier;
            int storageLimit;
            for(Map.Entry<String, Integer> townProductionEntry: townDailyProduction.entrySet()) {
                resource = townProductionEntry.getKey();
                townLevelModifier = government instanceof Town town ? TownySettings.getTownLevel(town).resourceProductionModifier() : 1.0;
				if (TownyResourcesSettings.isNonDynamicAmountMaterial(resource))
					townLevelModifier = 1.0;
                quantityToProduce = (int) (townProductionEntry.getValue() * townLevelModifier);
                if(quantityToProduce == 0)
                    continue;
                if(availableResources.containsKey(resource)) {
                    //Don't go over limit
                    currentQuantity = availableResources.get(resource);
                    storageLimit = quantityToProduce * storageLimitModifier;
                    if(currentQuantity == storageLimit) {
                        continue; //Already at limit
                    } else if (currentQuantity + quantityToProduce > storageLimit) {
                        quantityToProduce = storageLimit - currentQuantity; 
                    }                       
                    //Add to existing available resources
                    availableResources.put(resource, currentQuantity + quantityToProduce);
                } else {
                    //Add new available resource
                    availableResources.put(resource, quantityToProduce);
                }
            }
    
            //Set the list of available resources
            TownyResourcesGovernmentMetaDataController.setAvailableForCollection(government, availableResources);    
            
            //Save government
            government.save();        
                           
            } catch (Exception e) {
                TownyResources.severe("Problem producing resources for government " + government.getName());
                e.printStackTrace();
                return false;
            }

        //Some resources were produced. Return true;
        return true;
    }
}
