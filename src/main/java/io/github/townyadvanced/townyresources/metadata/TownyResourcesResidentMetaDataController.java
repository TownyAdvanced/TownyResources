package io.github.townyadvanced.townyresources.metadata;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Resident;
import io.github.townyadvanced.townyresources.TownyResources;
import io.github.townyadvanced.townyresources.objects.CategoryExtractionRecord;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

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
        extractionRecordMetadataKey = "townyresources_extractionRecord";  //e.g.   "25-common_rocks,64-wheat"


    public static Map<Material, CategoryExtractionRecord> getPlayerExtractionRecord(Player player) {
        //Ensure the player is a resident
        Resident resident = TownyUniverse.getInstance().getResident(player.getUniqueId());
        if(resident == null)
            return new HashMap<>();
        //Read player extraction record
        String recordAsString = MetaDataUtil.getSdf(resident, extractionRecordMetadataKey); 
        if(recordAsString.isEmpty())
            return new HashMap<>();       
        Map<Material, CategoryExtractionRecord> result = new HashMap<>();
        CategoryExtractionRecord categoryExtractionRecord;
        String[] categoryArray;
        String categoryName;
        int amountExtracted;
        String[] categories = recordAsString.replaceAll(" ", "").split(",");
        for(String category: categories) {
            categoryArray = category.split(",");
            amountExtracted = Integer.parseInt(categoryArray[0]);
            categoryName = categoryArray[1];
            categoryExtractionRecord = new CategoryExtractionRecord(categoryName, amountExtracted);
            for(Material material: categoryExtractionRecord.getResourceExtractionCategory().getMaterialsInCategory()) {
                result.put(material, categoryExtractionRecord);                
            }
        }
        //Return the result
        return result;
    }



}