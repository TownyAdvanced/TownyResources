package io.github.townyadvanced.townyresources.metadata;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.metadata.StringDataField;
import com.palmergames.bukkit.towny.utils.MetaDataUtil;

import io.github.townyadvanced.townyresources.TownyResources;
import io.github.townyadvanced.townyresources.controllers.PlayerExtractionLimitsController;
import io.github.townyadvanced.townyresources.objects.CategoryExtractionRecord;
import io.github.townyadvanced.townyresources.objects.ResourceExtractionCategory;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * 
 * @author Goosius
 *
 */
public class TownyResourcesResidentMetaDataController {


	@SuppressWarnings("unused")
	private TownyResources plugin;

	public TownyResourcesResidentMetaDataController(TownyResources plugin) {
		this.plugin = plugin;
	}

    private final static String
        extractionRecordMetadataKey = "townyresources_extractionRecord";  //e.g.   "25-common_rocks, 64-wheat"
    private final static StringDataField extractionRecordSDF = new StringDataField(extractionRecordMetadataKey, ""); 


    public static Map<Material, CategoryExtractionRecord> getPlayerExtractionRecord(Player player) {
        //Ensure the player is a resident
        Resident resident = TownyUniverse.getInstance().getResident(player.getUniqueId());
        if(resident == null)
            return new HashMap<>();
        //Read player extraction record
        String recordAsString = MetaDataUtil.getString(resident, extractionRecordSDF); 
        if(recordAsString.isEmpty())
            return new HashMap<>();       
        Map<Material, CategoryExtractionRecord> result = new HashMap<>();
        CategoryExtractionRecord categoryExtractionRecord;
        String[] categoryArray;
        String categoryName;
        int amountExtracted;
        String[] categories = recordAsString.replaceAll(" ", "").split(",");
        for(String category: categories) {
            categoryArray = category.split("-");
            amountExtracted = Integer.parseInt(categoryArray[0]);
            categoryName = categoryArray[1];
            ResourceExtractionCategory resourceExtractionCategory = PlayerExtractionLimitsController.getResourceExtractionCategory(categoryName);
            if(resourceExtractionCategory == null)
                continue; //Unknown category            
            categoryExtractionRecord = new CategoryExtractionRecord(resourceExtractionCategory, amountExtracted);
            for(Material material: categoryExtractionRecord.getResourceExtractionCategory().getMaterialsInCategory()) {
                result.put(material, categoryExtractionRecord);                
            }
        }
        //Return the result
        return result;
    }


    public static void setPlayerExtractionRecord(Resident resident, Map<Material, CategoryExtractionRecord> playerExtractionRecord) {
        //Save the players's extraction record
        Set<CategoryExtractionRecord> categoryExtractionRecords = new HashSet<>(playerExtractionRecord.values());
        StringBuilder recordAsString = new StringBuilder();
        boolean firstEntry = true;
        for(CategoryExtractionRecord categoryExtractionRecord: categoryExtractionRecords) {
            if(firstEntry) {
               firstEntry = false; 
            } else {
                recordAsString.append(", ");
            }
            recordAsString
                .append(categoryExtractionRecord.getAmountAlreadyExtracted())
                .append("-")
                .append(categoryExtractionRecord.getResourceExtractionCategory().getName());
        }
        if(!recordAsString.toString().isEmpty()) {
            //Set the string into metadata
            MetaDataUtil.setString(resident, extractionRecordSDF, recordAsString.toString(), false);
        }
    }
    
    public static void removePlayerExtractionRecord(Resident resident) {
        resident.removeMetaData(extractionRecordSDF);
    }
}