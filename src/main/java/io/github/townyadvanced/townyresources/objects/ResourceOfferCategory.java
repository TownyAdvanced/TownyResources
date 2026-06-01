package io.github.townyadvanced.townyresources.objects;

import java.util.List;
import java.util.Locale;

import org.bukkit.block.Biome;

import io.github.townyadvanced.townyresources.util.BiomeUtil;

public class ResourceOfferCategory {
    private final String name;
    private final int discoveryWeight;
    private final int baseAmountItems;
    private final int requiredResourceLevel;
    private final List<String> materialsInCategory;
    private final List<String> allowedBiomes;

    public ResourceOfferCategory(String name, int discoveryWeight, int baseOfferItems, int requiredResourceLevel, List<String> materialsInCategory, List<String> allowedBiomes) {
        this.name = name;
        this.discoveryWeight = discoveryWeight;
        this.baseAmountItems = baseOfferItems;
        this.requiredResourceLevel = requiredResourceLevel;
        this.materialsInCategory = materialsInCategory;
        this.allowedBiomes = allowedBiomes;
    }

    public String getName() {
        return name;
    }

    public int getBaseAmountItems() {
        return baseAmountItems;
    }

    public int getRequiredResourceLevel() {
        return requiredResourceLevel;
    }

    public List<String> getMaterialsInCategory() {
        return materialsInCategory;
    }

    public int getDiscoveryWeight() {
        return discoveryWeight;
    }

	public boolean isAllowedInBiome(Biome biome) {
		return allowedBiomes.contains("all")
				|| allowedBiomes.contains(biome.name().toLowerCase(Locale.ROOT))
				|| BiomeUtil.isCatchAll(biome, allowedBiomes);
	}

}
