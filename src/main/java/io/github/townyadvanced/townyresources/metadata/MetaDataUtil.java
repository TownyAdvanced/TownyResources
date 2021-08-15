package io.github.townyadvanced.townyresources.metadata;

import com.palmergames.bukkit.towny.object.TownyObject;
import com.palmergames.bukkit.towny.object.metadata.CustomDataField;
import com.palmergames.bukkit.towny.object.metadata.StringDataField;

/**
 * 
 * @author Goosius
 *
 */
class MetaDataUtil {

    public static String getSdf(TownyObject townyObject, String key) {
        if (townyObject.hasMeta(key)) {
            CustomDataField<?> cdf = townyObject.getMetadata(key);
            if (cdf instanceof StringDataField)
                return ((StringDataField) cdf).getValue();
        }
        return "";
    }

    public static void setSdf(TownyObject townyObject, String key, String value) {
        if (townyObject.hasMeta(key)) {
            if (value == null)
                townyObject.removeMetaData(townyObject.getMetadata(key));
            else {
                CustomDataField<?> cdf = townyObject.getMetadata(key);
                if (cdf instanceof StringDataField) {
                    ((StringDataField) cdf).setValue(value);
                    townyObject.save();
                }
                return;
            }
        } else if (value != null) {
            townyObject.addMetaData(new StringDataField(key, value));
		}
    }
}
