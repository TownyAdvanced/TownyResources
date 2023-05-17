package io.github.townyadvanced.townyresources.util;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.bukkit.World;
import org.bukkit.block.Biome;
import com.palmergames.bukkit.towny.TownySettings;
import com.palmergames.bukkit.towny.object.WorldCoord;

public class BiomeUtil {
	private final static List<String> overWorldBiomeNames = Arrays.asList("badlands", "bamboo_jungle", "beach", "birch_forest",
			"cold_ocean", "dark_forest", "deep_cold_ocean", "deep_dark", "deep_frozen_ocean", "deep_lukewarm_ocean", "deep_ocean",
			"desert", "dripstone_caves", "eroded_badlands", "flower_forest", "forest", "frozen_ocean", "frozen_peaks", "frozen_river",
			"grove", "ice_spikes", "jagged_peaks", "jungle", "lukewarm_ocean", "lush_caves", "mangrove_swamp", "meadow", "mushroom_fields",
			"ocean", "old_growth_birch_forest", "old_growth_pine_taiga", "old_growth_spruce_taiga", "plains", "river", "savanna", "savanna_plateau",
			"snowy_beach", "snowy_plains", "snowy_slopes", "snowy_taiga", "sparse_jungle", "stony_peaks", "stony_shore", "sunflower_plains",
			"swamp", "taiga", "warm_ocean", "windswept_forest", "windswept_gravelly_hills", "windswept_hills", "windswept_savanna", "wooded_badlands");
	private final static List<String> netherBiomeNames = Arrays.asList("nether_wastes", "basalt_deltas", "crimson_forest", "soul_sand_valley", 
			"hell", "warped_forest");
	private final static List<String> endBiomeNames = Arrays.asList("the_end", "end_barrens", "end_highlands", "end_midlands", "small_end_islands");
	private final static List<String> coldBiomeNames = Arrays.asList("old_growth_pine_taiga", "old_growth_spruce_taiga", "stony_shore", "taiga", 
			"windswept_forest", "windswept_gravelly_hills", "windswept_hills");
	private final static List<String> temperateBiomeNames = Arrays.asList("plains", "sunflower_plains", "forest", "flower_forest", "birch_forest",
			"old_growth_birch_forest", "dark_forest", "swamp", "mangrove_swamp", "jungle", "sparse_jungle", "bamboo_jungle", "beach", "mushroom_fields",
			"meadow", "stony_peaks", "cherry_grove");
	private final static List<String> warmBiomeNames = Arrays.asList("desert", "savanna", "savanna_plateau", "windswept_savanna", "badlands",
			"wooded_badlands", "eroded_badlands");

	public static Biome getWorldCoordBiome(WorldCoord worldCoord) {
		Map<Biome, Integer> biomeMap = new HashMap<>();
		World world = worldCoord.getBukkitWorld();
		int plotSize = TownySettings.getTownBlockSize();
		int worldX = worldCoord.getX() * plotSize, worldZ = worldCoord.getZ() * plotSize;
		for (int z = 0; z < plotSize; z++) {
			for (int x = 0; x < plotSize; x++) {
				Biome blockBiome = world.getHighestBlockAt(worldX + x, worldZ + z).getBiome();
				biomeMap.put(blockBiome, biomeMap.getOrDefault(blockBiome, 0) + 1);
			}
		}
		return biomeMap.entrySet().stream()
				.max(Comparator.comparing(Map.Entry::getValue))
				.get().getKey();
	}
	
	public static boolean isCatchAll(Biome biome, List<String> allowedBiomes) {
		return isAllowedOverWorld(biome, allowedBiomes)
				|| isAllowedNether(biome, allowedBiomes)
				|| isAllowedEnd(biome, allowedBiomes)
				|| isAllowedOcean(biome, allowedBiomes)
				|| isAllowedSnowy(biome, allowedBiomes)
				|| isAllowedCold(biome, allowedBiomes)
				|| isAllowedTemperate(biome, allowedBiomes)
				|| isAllowedWarm(biome, allowedBiomes)
				|| isAllowedAquatic(biome, allowedBiomes);
	}

	private static boolean isAllowedAquatic(Biome biome, List<String> allowedBiomes) {
		return allowedBiomes.contains("aquatic") && BiomeUtil.isAquatic(biome);
	}
	
	private static boolean isAllowedWarm(Biome biome, List<String> allowedBiomes) {
		return allowedBiomes.contains("warm") && BiomeUtil.isWarm(biome);
	}

	private static boolean isAllowedTemperate(Biome biome, List<String> allowedBiomes) {
		return allowedBiomes.contains("temperate") && BiomeUtil.isTemperate(biome);
	}

	private static boolean isAllowedCold(Biome biome, List<String> allowedBiomes) {
		return allowedBiomes.contains("cold") && BiomeUtil.isCold(biome);
	}

	private static boolean isAllowedEnd(Biome biome, List<String> allowedBiomes) {
		return allowedBiomes.contains("end") && BiomeUtil.isEnd(biome);
	}

	private static boolean isAllowedSnowy(Biome biome, List<String> allowedBiomes) {
		return allowedBiomes.contains("snowy") && BiomeUtil.isSnowy(biome);
	}

	private static boolean isAllowedNether(Biome biome, List<String> allowedBiomes) {
		return allowedBiomes.contains("nether") && BiomeUtil.isNether(biome);
	}

	private static boolean isAllowedOcean(Biome biome, List<String> allowedBiomes) {
		return allowedBiomes.contains("oceanbiomes") && BiomeUtil.isOcean(biome);
	}

	private static boolean isAllowedOverWorld(Biome biome, List<String> allowedBiomes) {
		return allowedBiomes.contains("overworld") && BiomeUtil.isOverWorld(biome);
	}

	public static boolean isOverWorld(Biome biome) {
		return overWorldBiomeNames.contains(toLowerCase(biome)); 
	}

	public static boolean isNether(Biome biome) {
		return netherBiomeNames.contains(toLowerCase(biome)); 
	}

	public static boolean isEnd(Biome biome) {
		return endBiomeNames.contains(toLowerCase(biome)); 
	}

	public static boolean isOcean(Biome biome) {
		return biome.name().contains("OCEAN");
	}

	public static boolean isSnowy(Biome biome) {
		return biome.name().contains("SNOW") 
				|| toLowerCase(biome).equals("ice_spikes") 
				|| toLowerCase(biome).equals("grove") 
				|| toLowerCase(biome).equals("jagged_peaks") 
				|| toLowerCase(biome).equals("frozen_peaks") ;
	}
	
	public static boolean isCold(Biome biome) {
		return coldBiomeNames.contains(toLowerCase(biome));
	}

	public static boolean isTemperate(Biome biome) {
		return temperateBiomeNames.contains(toLowerCase(biome));
	}

	public static boolean isWarm(Biome biome) {
		return warmBiomeNames.contains(toLowerCase(biome));
	}

	public static boolean isAquatic(Biome biome) {
		return toLowerCase(biome).contains("ocean") || toLowerCase(biome).contains("river");
	}

	private static String toLowerCase(Biome biome) {
		return biome.name().toLowerCase(Locale.ROOT); 
	}

}
