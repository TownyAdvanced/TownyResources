package io.github.townyadvanced.townyresources.controllers;

import com.gmail.goosius.siegewar.TownOccupationController;
import com.palmergames.bukkit.towny.TownySettings;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Government;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import io.github.townyadvanced.townyresources.TownyResources;
import io.github.townyadvanced.townyresources.metadata.TownyResourcesGovernmentMetaDataController;
import io.github.townyadvanced.townyresources.objects.ResourceOffer;
import io.github.townyadvanced.townyresources.objects.ResourceOfferCategory;
import io.github.townyadvanced.townyresources.settings.TownyResourcesSettings;
import io.github.townyadvanced.townyresources.settings.TownyResourcesTranslation;
import io.github.townyadvanced.townyresources.util.TownyResourcesMessagingUtil;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TownResourceProductionController {

    /**
     * Recalculate production for all towns and nations
     */
    public static void recalculateAllProduction() {
        Map<String, ResourceOffer> allResourceOffers = TownyResourcesSettings.getAllResourceOffers();
        recalculateProductionForAllTowns();
        recalculateProductionForAllNations();
        TownyResources.info("All Production Recalculated");
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
            List<Material> discoveredResources = new ArrayList<>(TownyResourcesGovernmentMetaDataController.getDiscoveredAsList(town));
    
            //Remove any discovered resources which are no longer on offer
            Map<Material, ResourceOfferCategory> allOffers = TownResourceOffersController.getMaterialToResourceOfferCategoryMap();
            List<Material> resourcesToRemove = new ArrayList<>();
            for(Material resource: discoveredResources) {
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
            double townCutNormalized;
            if(town.hasNation() || TownOccupationController.isTownOccupied(town)) {
                townCutNormalized = 1 - TownyResourcesSettings.getTownResourcesProductionNationTaxNormalized();
            } else {
                townCutNormalized = 1;
            }
    
            //Build the town production map
            Map<Material, Integer> townProduction = calculateProduction(town, townCutNormalized);
    
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
        double nationCutNormalized = TownyResourcesSettings.getTownResourcesProductionNationTaxNormalized();
        List<Town> occupiedForeignTowns = TownOccupationController.getOccupiedForeignTowns(nation);
        List<Town> occupiedHomeTowns = TownOccupationController.getOccupiedHomeTowns(nation);
        Map<Material,Integer> nationProduction = new HashMap<>();
        List<Town> townsToTakeFrom = new ArrayList<>();
        Map<Material,Integer> resourcesTakenFromTown;
        Material takenResource;
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
            resourcesTakenFromTown = calculateProduction(town, nationCutNormalized);
            //Add resources to nation
            for(Map.Entry<Material, Integer> resourceTakenFromTown: resourcesTakenFromTown.entrySet()) {
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
     * @param town the town producing the resource
     * @param cutNormalized the cut of the resource to return
     * @return the production as a map, with each value multiplied by the given cutNormalized value
     */
    private static Map<Material, Integer> calculateProduction(Town town, double cutNormalized) {        
        //Get all offers
        Map<Material, ResourceOfferCategory> allOffers = TownResourceOffersController.getMaterialToResourceOfferCategoryMap();
        
        //Get discovered resources
        List<Material> discoveredResources = new ArrayList<>(TownyResourcesGovernmentMetaDataController.getDiscoveredAsList(town));

        //Get configured resource level bonuses
        List<Double> normalizedBonusesPerResourceLevel = TownyResourcesSettings.getNormalizedProductionBonusesPerResourceLevel();

        //Calculate the production
        Map<Material, Integer> production = new HashMap<>();
        Material material;
        double baseProducedAmount;
        int finalProducedAmount;

        for(int i = 0; i < discoveredResources.size(); i++) {
            //Ensure town meets the town level requirement to produce the resource
            if(TownySettings.calcTownLevel(town) <  TownyResourcesSettings.getProductionTownLevelRequirementPerResourceLevel().get(i)) 
                break;
            material = discoveredResources.get(i);
            baseProducedAmount = allOffers.get(material).getBaseAmountItems();
            finalProducedAmount = (int)((baseProducedAmount * normalizedBonusesPerResourceLevel.get(i) * cutNormalized) + 0.5);
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
        TownyResourcesMessagingUtil.sendGlobalMessage(TownyResourcesTranslation.of("production.message", numProducingTowns));        
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
            Map<Material, Integer> townDailyProduction = TownyResourcesGovernmentMetaDataController.getDailyProductionAsMap(government);
    
            if(townDailyProduction.isEmpty())
                return false;
                
            //Get the list of resources which are already available for collection
            Map<Material,Integer> availableResources = TownyResourcesGovernmentMetaDataController.getAvailableForCollectionAsMap(government);
    
            //Get storage Limit modifier
            int storageLimitModifier = TownyResourcesSettings.getStorageLimitModifier();
            
            //Produce resources
            Material resource;
            int quantityToProduce;
            int currentQuantity;
            int storageLimit;
            for(Map.Entry<Material, Integer> townProductionEntry: townDailyProduction.entrySet()) {
                resource = townProductionEntry.getKey();
                quantityToProduce =townProductionEntry.getValue();
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
