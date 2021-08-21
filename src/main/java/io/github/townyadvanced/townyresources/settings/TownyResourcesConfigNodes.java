package io.github.townyadvanced.townyresources.settings;

public enum TownyResourcesConfigNodes {
	
	VERSION_HEADER("version", "", ""),
	VERSION(
			"version.version",
			"",
			"# This is the current version.  Please do not edit."),
	LANGUAGE(
			"language",
			"english.yml",
			"# The language file you wish to use"),
	ENABLED(
			"enabled",
			"true",
			"",
			"# If true, the TownyResources system is enabled.",
			"# if false, the TownyResources system is disabled."),		
	RESOURCE_EXTRACTION_LIMITS(
			"resource_extraction_limits",
			"",
			"",
			"",
			"############################################################",
			"# +------------------------------------------------------+ #",
			"# |             Resource Extraction Limits               | #",
			"# +------------------------------------------------------+ #",
			"############################################################",
			""),
	RESOURCE_EXTRACTION_LIMITS_ENABLED(
			"resource_extraction_limits.enabled",
			"true",
			"",
			"# If true, then daily player resource extraction limits are enabled.",
			"# If false, then daily player resource extraction limits are disabled."),
			
			//TODO ------- Add machine-limiting stuff here e.g. for cactuses and sugar.  Maybe???. Could b already covered
	RESOURCE_EXTRACTION_LIMITS_CATEGORIES(			
			"resource_extraction_limits.categories",
			"" +
			"{Common Rocks, 6, STONE, COBBLESTONE}," +
			"{Uncommon Rocks, 2, DIORITE, ANDESITE, GRANITE, MOSSY_COBBLESTONE, MAGMA_BLOCK, BASALT}," +
			"{Terracotta, 2, TERRACOTTA, WHITE_TERRACOTTA, ORANGE_TERRACOTTA, MAGENTA_TERRACOTTA, LIGHT_BLUE_TERRACOTTA, YELLOW_TERRACOTTA," +
			"LIME_TERRACOTTA, PINK_TERRACOTTA, GRAY_TERRACOTTA, LIGHT_GRAY_TERRACOTTA, CYAN_TERRACOTTA," +
			"PURPLE_TERRACOTTA, BLUE_TERRACOTTA, BROWN_TERRACOTTA, GREEN_TERRACOTTA, RED_TERRACOTTA,BLACK_TERRACOTTA}," +
			"{Quartz, 0.25, QUARTZ_BLOCK}," +
			"{Prismarine, 0.125, PRISMARINE}," +
			"{Obsidian, 0.125, OBSIDIAN}," +
			"{Clay, 1, CLAY_BALL, CLAY}," +
			"{Ice, 1, ICE, PACKED_ICE, BLUE_ICE}," +
			"{Snow, 1, SNOW, SNOWBALL, SNOW_BLOCK}," +
			"{Coal, 1, COAL, COAL_BLOCK}," +
			"{Iron, 1, IRON_ORE, RAW_IRON}," +
			"{Gold, 0.25, GOLD_ORE, DEEPSLATE_GOLD_ORE, RAW_GOLD}," +
			"{Gold Nugget, 0.25, GOLD_NUGGET, NETHER_GOLD_ORE}," +
			"{Emeralds, 0.25, EMERALD_ORE, EMERALD}," +
			"{Lapis Lazuli, 0.25, LAPIS_LAZULI, LAPIS_BLOCK, LAPIS_ORE}," +			
			"{Diamonds, 0.125, DIAMOND_ORE, DIAMOND}," +
			"{Redstone, 1, REDSTONE, REDSTONE_BLOCK}," +
			"{Netherrack, 4, NETHERRACK}," +
			"{Nether Quartz, 1, NETHER_QUARTZ_ORE, QUARTZ}," +
			"{Glowstone, 0.25, GLOWSTONE_DUST, GLOWSTONE}," +
			"{End Stone, 4, END_STONE}," +
			"{Ancient_Debris, 0.0625, ANCIENT_DEBRIS}," +
			"{Wool, 0.5, WHITE_WOOL, ORANGE_WOOL, MAGENTA_WOOL, LIGHT_BLUE_WOOL, YELLOW_WOOL," +
			"LIME_WOOL, PINK_WOOL, GRAY_WOOL, LIGHT_GRAY_WOOL, CYAN_WOOL," +
			"PURPLE_WOOL, BLUE_WOOL, BROWN_WOOL, GREEN_WOOL, RED_WOOL,BLACK_WOOL}," +
			"{Coral, 1, TUBE_CORAL, BRAIN_CORAL, BUBBLE_CORAL, FIRE_CORAL, HORN_CORAL," +
			"TUBE_CORAL_BLOCK, BRAIN_CORAL_BLOCK, BUBBLE_CORAL_BLOCK, FIRE_CORAL_BLOCK, HORN_CORAL_BLOCK," +
			"DEAD_TUBE_CORAL_BLOCK, DEAD_BRAIN_CORAL_BLOCK, DEAD_BUBBLE_CORAL_BLOCK, DEAD_FIRE_CORAL_BLOCK, DEAD_HORN_CORAL_BLOCK," +
			"TUBE_CORAL_FAN, BRAIN_CORAL_FAN, BUBBLE_CORAL_FAN, FIRE_CORAL_FAN, HORN_CORAL_FAN," +
			"DEAD_TUBE_CORAL_FAN, DEAD_BRAIN_CORAL_FAN, DEAD_BUBBLE_CORAL_FAN, DEAD_FIRE_CORAL_FAN,DEAD_HORN_CORAL_FAN}," +
			"{Wheat, 1, WHEAT}," +
			"{Carrots, 1, CARROTS}," +
			"{Potatoes, 1, POTATOES}," +
			"{Beetroot, 1, BEETROOT}," +
			"{Pumpkin Seeds, 1, PUMPKIN_SEEDS}," +
			"{Melon Slices, 1, MELON_SLICE}," +
			"{Cocoa Beans, 1, COCOA_BEANS}," +
			"{Pumpkin Blocks, 0.125, PUMPKIN}," +
			"{Melon Blocks, 0.125, MELON}," +
			"{Cocoa Pods, 0.125, COCOA}," +
			"{Kelp, 1, KELP}," +
			"{Bamboo, 1, BAMBOO}," +
			"{Cactus, 1, CACTUS}," +
			"{Mushrooms, 1, BROWN_MUSHROOM, RED_MUSHROOM}," +
			"{Apples, 0.125, APPLE}," +
			"{Vines, 0.25, VINE}," +
			"{Fish, 1, COD, SALMON, PUFFERFISH, TROPICAL_FISH}," +
			"{Gunpowder, 0.25, GUNPOWDER}," +
			"{String, 0.25, STRING}," +
			"{Spider eyes, 0.25, SPIDER_EYE}," +
			"{Feathers, 0.25, FEATHER}," +
			"{Leather, 0.25, LEATHER}," +
			"{Rabbit Hides, 0.25, RABBIT_HIDE}," +
			"{Rotten Flesh, 1, ROTTEN_FLESH}," +
			"{Bones, 0.5, BONE, BONE_BLOCK}," +
			"{Pork, 0.25, PORKCHOP, COOKED_PORKCHOP}," +
			"{Mutton, 0.25, MUTTON, COOKED_MUTTON}," +
			"{Beef, 0.25, BEEF, COOKED_BEEF}," +
			"{Chicken, 0.25, CHICKEN, COOKED_CHICKEN}," +
			"{Eggs, 0.25, EGG}," +			
			"{Chorus Fruits, 0.25, CHORUS_FRUIT}," +
			"{Ender Pearls, 0.125, ENDER_PEARL}",
			"",
			"# This list shows the resource extraction categories.",
			"# ",
			"# Each category is enclosed in curly brackets, and has 3 parts:",
			"# 1 - The name of the category (used for messaging)",
			"# 2-  The daily limit per player (in stacks)",
			"# 3-  The list of materials in the category"),
	TOWN_RESOURCES(
			"town_resources",
			"",
			"",
			"",
			"############################################################",
			"# +------------------------------------------------------+ #",
			"# |                  Town Resources                      | #",
			"# +------------------------------------------------------+ #",
			"############################################################",
			""),
	TOWN_RESOURCES_SURVEYS(
			"town_resources.surveys",
			"",
			"",
			""),				
	TOWN_RESOURCES_SURVEYS_ENABLED(
			"town_resources.surveys.enabled",
			"true",
			"",
			"# If true, players can do surveys.",
			"# if false, they cannot."),
	TOWN_RESOURCES_SURVEYS_COST_PER_RESOURCE_LEVEL(
			"town_resources.surveys.cost_per_resource_level",
			"250, 1000, 5000, 20000",
			"",
			"# This list of values determines how much it costs to do a survey for each resource level.",
			"# Example: If the first value is 250, then it will cost 250 to survey and discover the level 1 resource."),
	TOWN_RESOURCES_SURVEYS_NUM_TOWNBLOCKS_REQUIREMENT_PER_RESOURCE_LEVEL(
			"town_resources.surveys.num_townblocks_requirement_per_resource_level",
			"10, 50, 100, 200",
			"",
			"# This list of values determines how much many townblocks a town must have to DISCOVER each level of resource.",
			"# Example: If the first value is 10, then the town must have 10 townblocks to survey and discover the level 1 resource."),
	TOWN_RESOURCES_PRODUCTION(
			"town_resources.production",
			"",
			"",
			""),			
	TOWN_RESOURCES_PRODUCTION_ENABLED(
			"town_resources.production.enabled",
			"true",
			"",
			"# If true, towns produce resources.",
			"# if false, towns do not produce resource."),
	TOWN_RESOURCES_PRODUCTION_TOWN_LEVEL_REQUIREMENT_PER_RESOURCE_LEVEL(
			"town_resources.production.town_level_requirement_per_resource_level",
			"1, 4, 6, 8",
			"",
			"# This list of values determines the level a town needs to be, to PRODUCE its resources.",
			"# Example: If the first value is 1, then the town has to be level 1 to produce its level 1 resource."),
	TOWN_RESOURCES_PRODUCTION_PRODUCTIVITY_PERCENTAGE_PER_RESOURCE_LEVEL(
			"town_resources.production.productivity_percentage_per_resource_level",
			"100, 200, 300, 400",
			"",
			"# This list of values determines the productivity of each level of resource.",
			"# Example: If the second value is 200, then the level 2 resource of a town will be 200% productive."),
	TOWN_RESOURCES_PRODUCTION_STORAGE_LIMIT_MODIFIER(
			"town_resources.production.storage_limit_modifier",
			"5",
			"",
			"# This value determines the limit of how many resources of each type can be stored for collection.",
			"# Example: If this value 3, and the daily production amount is 32 ..... then the storage limit is 96."),
	TOWN_RESOURCES_PRODUCTION_NATION_TAX_PERCENTAGE(
			"town_resources.production.nation_tax_percentage",
			"50",
			"",
			"# The owner nation of a town gets this percentage of town production.",
			"# The town gets the rest."),
	TOWN_RESOURCES_OFFERS(
			"town_resources.offers",
			"",
			"",
			""),
	TOWN_RESOURCES_OFFERS_CATEGORIES(
			"town_resources.offers.categories",
			"" +
			"{Common Dirt, 10, 2, DIRT}," +
			"{Wood, 100, 1, OAK_LOG, SPRUCE_LOG, BIRCH_LOG, JUNGLE_LOG, ACACIA_LOG, DARK_OAK_LOG, " +
			"OAK_WOOD, SPRUCE_WOOD, BIRCH_WOOD, JUNGLE_WOOD, ACACIA_WOOD, DARK_OAK_WOOD}," +
			"{Common Rocks, 100, 2, STONE, COBBLESTONE}," +
			"{Uncommon Rocks, 50, 1, DIORITE, ANDESITE, GRANITE}," +
			"{Terracotta, 25, 0.5, TERRACOTTA}," +
			"{Quartz, 25, 0.25, QUARTZ_BLOCK}," +
			"{Prismarine, 10, 0.125, PRISMARINE}," +
			"{Obsidian, 25, 0.125, OBSIDIAN}," +
			"{Clay, 100, 1, CLAY_BALL}," +
			"{Snow, 10, 1, SNOWBALL}," +
			"{Coal, 100, 1, COAL}," +
			"{Iron, 100, 0.5, RAW_IRON}," +
			"{Gold, 100, 0.25, RAW_GOLD}," +
			"{Emeralds, 100, 0.25, EMERALD}," +
			"{Diamonds, 100, 0.125, DIAMOND}," +
			"{Ancient Debris, 100, 0.0625, ANCIENT_DEBRIS}," +
			"{Redstone, 100, 1, REDSTONE}," +
			"{Ice, 25, 1, ICE}," +
			"{Wheat, 100, 1, WHEAT}," +
			"{Carrots, 100, 1, CARROTS}," +
			"{Potatoes, 100, 1, POTATOES}," +
			"{Beetroots, 100, 1, BEETROOT}," +
			"{Pumpkins, 100, 1, PUMPKIN_SEEDS}," +
			"{Melon Plants, 100, 1, MELON_SLICE}," +
			"{Cocoa Plants, 100, 1, COCOA_BEANS}," +
			"{Kelp, 50, 1, KELP}," +
			"{Bamboo, 50, 1, BAMBOO}," +
			"{Cactus, 50, 1, CACTUS}," +
			"{Mushrooms, 100, 1, BROWN_MUSHROOM, RED_MUSHROOM}," +
			"{Apple Trees, 100, 0.125, APPLE}," +
			"{Vines, 100, 0.25, VINE}," +
			"{Fish, 100, 1, COD, SALMON, PUFFERFISH, TROPICAL_FISH}," +
			"{A Flock of Sheep, 100, 0.5, WHITE_WOOL}," +
			"{A Nest of Creepers, 100, 0.25, GUNPOWDER}," +
			"{A Nest of Spiders, 100, 0.25, STRING}," +
			"{A Nest of Spiders, 100, 0.25, SPIDER_EYE}," +
			"{A Flock of Chickens, 100, 0.25, FEATHER}," +
			"{A Herd of Animals, 100, 0.25, LEATHER}," +
			"{A Herd of Rabbits, 100, 0.25, RABBIT_HIDE}," +
			"{A Hest of Zombies, 100, 1, ROTTEN_FLESH}" +
			"{A Hest of Skeletons, 100, 0.5, BONE}," +
			"{A Herd of Pigs, 100, 0.25, PORKCHOP}," +
			"{A Herd of Sheep, 100, 0.25, MUTTON}," +
			"{A Herd of Cows, 100, 0.25, BEEF}," +
			"{A Flock of Chickens, 100, 0.25, CHICKEN}," +	
			"{A Flock of Hens, 100, 0.25, EGG}," +						
			"{A Nest of Endermen, 100, 0.125, ENDER_PEARL}",
		   "",
			"# This list shows the survey offer categories.",
			"# ",
			"# Each category is enclosed in curly brackets, and has 4 parts:",
			"# 1 - The discovery name of the category (used for messaging)",
			"# 2 - The discovery weight of the category (used during discovery)",
			"# 3-  The base amount of the offer (in stacks)",
			"# 4-  The list of materials in the category");

	private final String Root;
	private final String Default;
	private String[] comments;

	TownyResourcesConfigNodes(String root, String def, String... comments) {

		this.Root = root;
		this.Default = def;
		this.comments = comments;
	}

	/**
	 * Retrieves the root for a config option
	 *
	 * @return The root for a config option
	 */
	public String getRoot() {

		return Root;
	}

	/**
	 * Retrieves the default value for a config path
	 *
	 * @return The default value for a config path
	 */
	public String getDefault() {

		return Default;
	}

	/**
	 * Retrieves the comment for a config path
	 *
	 * @return The comments for a config path
	 */
	public String[] getComments() {

		if (comments != null) {
			return comments;
		}

		String[] comments = new String[1];
		comments[0] = "";
		return comments;
	}

}
