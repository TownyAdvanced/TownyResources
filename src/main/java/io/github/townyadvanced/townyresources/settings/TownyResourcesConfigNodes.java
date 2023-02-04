package io.github.townyadvanced.townyresources.settings;

public enum TownyResourcesConfigNodes {
	
	VERSION_HEADER("version", "", ""),
	VERSION(
			"version.version",
			"",
			"# This is the current version.  Please do not edit."),
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
			"false",
			"",
			"# If true, then daily player resource extraction limits are enabled.",
			"# If false, then daily player resource extraction limits are disabled."),
	RESOURCE_EXTRACTION_LIMITS_TOGGLES(
			"resource_extraction_limits.toggles",
			"",
			"",
			"# Individual toggles for daily resource extraction limits.",
			"# All of these toggles will disable if the above is set to false."),
	RESOURCE_EXTRACTION_LIMITS_BLOCKS(
			"resource_extraction_limits.toggles.blocks",
			"true",
			"",
			"# If true, then daily player resource extraction limits for mining blocks are enabled.",
			"# If false, then daily player resource extraction limits for mining blocks are disabled."),
	RESOURCE_EXTRACTION_LIMITS_DROPS(
			"resource_extraction_limits.toggles.drops",
			"true",
			"",
			"# If true, then daily player resource extraction limits that apply to entity drops are enabled. When enabled, players must interact with an entity on death to receive item drops.",
			"# If false, then daily player resource extraction limits that apply to entity drops are disabled. When disabled, automatic entity killing farms will function."),
	RESOURCE_EXTRACTION_LIMITS_SHEARING(
			"resource_extraction_limits.toggles.shearing",
			"true",
			"",
			"# If true, then daily player resource extraction limits for sheep shearing are enabled.",
			"# If false, then daily player resource extraction limits for sheep shearing are disabled. When disabled, automatic redstone farms will function."),
	RESOURCE_EXTRACTION_LIMITS_FISHING(
			"resource_extraction_limits.toggles.fishing",
			"true",
			"",
			"# If true, then daily player resource extraction limits for fishing are enabled.",
			"# If false, then daily player resource extraction limits for fishing are disabled."),
	RESOURCE_EXTRACTION_LIMITS_COOLDOWN_AFTER_DAILY_LIMIT_WARNING_MESSAGE_MILLIS(
			"resource_extraction_limits.cooldown_after_daily_limit_warning_message_millis",
			"5000",
			"",
			"# The cooldown after a player receives a daily-limit warning message.",
			"# During this time they cannot receive another daily-limit warning message for the same material."),
	RESOURCE_EXTRACTION_LIMITS_CATEGORIES(			
			"resource_extraction_limits.categories",
			"" +
			"{common_dirt, 3, DIRT, FARMLAND}," +
			"{uncommon_dirt, 1, PODZOL, ROOTED_DIRT, MYCELIUM}," +
			"{rare_dirt, 1, SOUL_SOIL}," +
			"{gravel, 1, GRAVEL}," +			
			"{sand, 3, SAND}," +								
			"{common_rocks, 2.5, STONE, COBBLESTONE}," +
			"{uncommon_rocks, 1.5, DIORITE, ANDESITE, GRANITE, MOSSY_COBBLESTONE, MAGMA_BLOCK, BASALT}," +
			"{terracotta, 1.5, TERRACOTTA, WHITE_TERRACOTTA, ORANGE_TERRACOTTA, MAGENTA_TERRACOTTA, LIGHT_BLUE_TERRACOTTA, YELLOW_TERRACOTTA," +
			"LIME_TERRACOTTA, PINK_TERRACOTTA, GRAY_TERRACOTTA, LIGHT_GRAY_TERRACOTTA, CYAN_TERRACOTTA," +
			"PURPLE_TERRACOTTA, BLUE_TERRACOTTA, BROWN_TERRACOTTA, GREEN_TERRACOTTA, RED_TERRACOTTA,BLACK_TERRACOTTA}," +
			"{quartz, 0.25, QUARTZ_BLOCK}," +
			"{prismarine, 0.125, PRISMARINE}," +
			"{obsidian, 0.125, OBSIDIAN}," +
			"{clay, 1, CLAY_BALL, CLAY}," +
			"{ice, 1, ICE, PACKED_ICE, BLUE_ICE}," +
			"{snow, 1, SNOW, SNOWBALL, SNOW_BLOCK}," +
			"{coal, 1, COAL, COAL_BLOCK}," +
			"{iron, 1, IRON_ORE, RAW_IRON}," +
			"{gold, 0.25, GOLD_ORE, DEEPSLATE_GOLD_ORE, RAW_GOLD}," +
			"{gold_nuggets, 0.25, GOLD_NUGGET, NETHER_GOLD_ORE}," +
			"{copper, 0.25, COPPER_ORE, RAW_COPPER}," +			
			"{emeralds, 0.25, EMERALD_ORE, EMERALD}," +
			"{lapis_Lazuli, 0.25, LAPIS_LAZULI, LAPIS_BLOCK, LAPIS_ORE}," +			
			"{diamonds, 0.125, DIAMOND_ORE, DIAMOND}," +
			"{redstone, 1, REDSTONE, REDSTONE_BLOCK}," +
			"{netherrack, 4, NETHERRACK}," +
			"{nether_quartz, 1, NETHER_QUARTZ_ORE, QUARTZ}," +
			"{glowstone, 0.25, GLOWSTONE_DUST, GLOWSTONE}," +
			"{end_Stone, 4, END_STONE}," +
			"{ancient_debris, 0.03125, ANCIENT_DEBRIS}," +
			"{wood, 3.5, OAK_LOG, SPRUCE_LOG, BIRCH_LOG, JUNGLE_LOG, ACACIA_LOG, DARK_OAK_LOG," +
			"CRIMSON_STEM, WARPED_STEM, OAK_WOOD, SPRUCE_WOOD, BIRCH_WOOD, JUNGLE_WOOD," +
			"ACACIA_WOOD, DARK_OAK_WOOD, CRIMSON_HYPHAE, WARPED_HYPHAE}" +
			"{wool, 0.5, WHITE_WOOL, ORANGE_WOOL, MAGENTA_WOOL, LIGHT_BLUE_WOOL, YELLOW_WOOL," +
			"LIME_WOOL, PINK_WOOL, GRAY_WOOL, LIGHT_GRAY_WOOL, CYAN_WOOL," +
			"PURPLE_WOOL, BLUE_WOOL, BROWN_WOOL, GREEN_WOOL, RED_WOOL,BLACK_WOOL}," +
			"{Coral, 1, TUBE_CORAL, BRAIN_CORAL, BUBBLE_CORAL, FIRE_CORAL, HORN_CORAL," +
			"TUBE_CORAL_BLOCK, BRAIN_CORAL_BLOCK, BUBBLE_CORAL_BLOCK, FIRE_CORAL_BLOCK, HORN_CORAL_BLOCK," +
			"DEAD_TUBE_CORAL_BLOCK, DEAD_BRAIN_CORAL_BLOCK, DEAD_BUBBLE_CORAL_BLOCK, DEAD_FIRE_CORAL_BLOCK, DEAD_HORN_CORAL_BLOCK," +
			"TUBE_CORAL_FAN, BRAIN_CORAL_FAN, BUBBLE_CORAL_FAN, FIRE_CORAL_FAN, HORN_CORAL_FAN," +
			"DEAD_TUBE_CORAL_FAN, DEAD_BRAIN_CORAL_FAN, DEAD_BUBBLE_CORAL_FAN, DEAD_FIRE_CORAL_FAN,DEAD_HORN_CORAL_FAN}," +
			"{wheat, 1, WHEAT}," +
			"{carrots, 1, CARROT}," +
			"{potatoes, 1, POTATO}," +
			"{beetroot, 1, BEETROOT}," +
			"{melon_slices, 1, MELON_SLICE}," +
			"{cocoa_beans, 1, COCOA_BEANS}," +
			"{pumpkin_blocks, 0.125, PUMPKIN}," +
			"{melon_blocks, 0.125, MELON}," +
			"{cocoa_pods, 0.125, COCOA}," +
			"{kelp, 1, KELP}," +
			"{bamboo, 1, BAMBOO}," +
			"{cactus, 1, CACTUS}," +
			"{mushrooms, 1, BROWN_MUSHROOM, RED_MUSHROOM}," +
			"{apples, 0.125, APPLE}," +
			"{vines, 0.25, VINE}," +
			"{nether_wart, 1, NETHER_WART}," +
			"{chorus_fruit, 1, CHORUS_FRUIT}," +		
			"{fish, 0.5, COD, SALMON, PUFFERFISH, TROPICAL_FISH}," +
			"{gunpowder, 0.25, GUNPOWDER}," +
			"{string, 0.25, STRING}," +
			"{spider_eyes, 0.25, SPIDER_EYE}," +
			"{slime, 0.25, SLIME_BALL, SLIME_BLOCK}," +
			"{feathers, 0.25, FEATHER}," +
			"{leather, 0.25, LEATHER}," +
			"{rabbit_hides, 0.25, RABBIT_HIDE}," +
			"{rotten_flesh, 0.5, ROTTEN_FLESH}," +
			"{bones, 0.5, BONE, BONE_BLOCK}," +
			"{pork, 0.25, PORKCHOP, COOKED_PORKCHOP}," +
			"{mutton, 0.25, MUTTON, COOKED_MUTTON}," +
			"{beef, 0.25, BEEF, COOKED_BEEF}," +
			"{chicken, 0.25, CHICKEN, COOKED_CHICKEN}," +
			"{chorus_fruits, 0.25, CHORUS_FRUIT}," +
			"{ender_pearls, 0.125, ENDER_PEARL}",
			"",
			"# This list shows the resource extraction categories.",
			"# ",
			"# Each category is enclosed in curly brackets, and has 3 parts:",
			"# 1 - The name of the category (used for messaging)",
			"# 2-  The daily limit per player (in stacks)",
			"# 3-  The list of materials in the category"),
	RESOURCE_EXTRACTION_LIMITS_UNBREAKABLES(
			"resource_extraction_limits.unbreakables",
			"ANCIENT_DEBRIS",
			"",
			"# A list of blocks which cannot be broken when the player hits the extraction limit for this type.",
			"# Ex: unbreakables: ANCIENT_DEBRIS,DIAMOND_BLOCK"),
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
			"2, 4, 6, 8",
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
			"# The nation of a town gets this percentage of town production, as long as the town is not occupied.",
			"# The town gets the rest."),
	TOWN_RESOURCES_PRODUCTION_OCCUPYING_NATION_TAX_PERCENTAGE(
			"town_resources.production.occupying_nation_tax_percentage",
			"50",
			"",
			"# If a town is occupied, the occupying nation gets this percentage of town production.",
			"# The town gets the rest."),
	TOWN_RESOURCES_OFFERS(
			"town_resources.offers",
			"",
			"",
			""),
	TOWN_RESOURCES_OFFERS_CATEGORIES(
			"town_resources.offers.categories",
			"" +
			"{common_dirt, 25, 2, DIRT}," +
			"{gravel, 100, 1, GRAVEL}," +			
			"{sand, 100, 2, SAND}," +								
			"{common_rocks, 100, 2, STONE, COBBLESTONE}," +
			"{uncommon_rocks, 100, 1, DIORITE, ANDESITE, GRANITE}," +
			"{terracotta, 100, 0.5, TERRACOTTA}," +
			"{quartz, 100, 0.25, QUARTZ_BLOCK}," +
			"{prismarine, 100, 0.125, PRISMARINE}," +
			"{obsidian, 100, 0.125, OBSIDIAN}," +
			"{clay, 100, 1, CLAY_BALL}," +
			"{ice, 25, 1, ICE}," +
			"{snow, 25, 1, SNOWBALL}," +
			"{coal, 200, 1, COAL}," +
			"{iron, 200, 0.5, RAW_IRON}," +
			"{gold, 200, 0.25, RAW_GOLD}," +
			"{copper, 100, 0.25, RAW_COPPER}," +			
			"{emeralds, 200, 0.25, EMERALD}," +
			"{diamonds, 200, 0.125, DIAMOND}," +
			"{redstone, 100, 1, REDSTONE}," +
			"{ancient_debris, 200, 0.03125, ANCIENT_DEBRIS}," +
			"{wood, 200, 2, OAK_LOG, SPRUCE_LOG, BIRCH_LOG, JUNGLE_LOG, ACACIA_LOG, DARK_OAK_LOG, " +
			"OAK_WOOD, SPRUCE_WOOD, BIRCH_WOOD, JUNGLE_WOOD, ACACIA_WOOD, DARK_OAK_WOOD}," +
			"{wheat, 100, 1, WHEAT}," +
			"{carrots, 100, 1, CARROT}," +
			"{potatoes, 100, 1, POTATO}," +
			"{beetroots, 100, 1, BEETROOT}," +
			"{pumpkins, 100, 1, PUMPKIN_SEEDS}," +
			"{melon_slices, 100, 1, MELON_SLICE}," +
			"{cocoa_plants, 100, 1, COCOA_BEANS}," +
			"{kelp, 100, 1, KELP}," +
			"{bamboo, 100, 1, BAMBOO}," +
			"{cactus, 100, 1, CACTUS}," +
			"{mushrooms, 100, 1, BROWN_MUSHROOM, RED_MUSHROOM}," +
			"{apple_trees, 100, 0.125, APPLE}," +
			"{vines, 100, 0.25, VINE}," +
			"{fish, 100, 1, COD, SALMON, PUFFERFISH, TROPICAL_FISH}," +
			"{wool, 100, 0.5, WHITE_WOOL}," +			
			"{gunpowder, 200, 0.25, GUNPOWDER}," +
			"{string, 100, 0.25, STRING}," +
			"{spider_eyes, 100, 0.25, SPIDER_EYE}," +
			"{slime, 100, 0.25, SLIME_BALL}," +
			"{feathers, 100, 0.25, FEATHER}," +
			"{leather, 100, 0.25, LEATHER}," +
			"{rabbit_hide, 100, 0.25, RABBIT_HIDE}," +
			"{rotten_flesh, 100, 1, ROTTEN_FLESH}" +
			"{bones, 100, 0.5, BONE}," +
			"{pork, 100, 0.25, PORKCHOP}," +
			"{mutton, 100, 0.25, MUTTON}," +
			"{beef, 100, 0.25, BEEF}," +
			"{chickens, 100, 0.25, CHICKEN}," +
			"{ender_pearls, 100, 0.125, ENDER_PEARL}",
		   "",
			"# This list shows the survey offer categories.",
			"# ",
			"# Each category is enclosed in curly brackets, and has 4 parts:",
			"# 1 - The name of the category (used for messaging)",
			"# 2 - The discovery weight of the category (used during discovery)",
			"# 3-  The base amount of the offer (in stacks)",
			"# 4-  The list of materials in the category",
			"# ",
			"# The default values give a weight of 200 to strategic resources (coal, iron etc.), and 100 to most non-strategic resources. (wheat, quartz etc.)",
			"# This gives each survey approx. 30% chance to discover a strategic resources, and a 70% to discover a non-strategic resource.",
			"# ",
			"# This list supports Slimefun, MythicMobs and MMOItems items as well. When entering an MMOItem use the TYPE:ID format,",
			"# ie: SWORD:CUTLASS where TownyResources expects a material."),
	TOWN_RESOURCES_OFFERS_MATERIALS_WITH_NON_DYNAMIC_AMMOUNTS(
			"town_resources.offers.categories_with_fixed_amounts",
			"somematerial,someitem",
			"",
			"# This list is used for Offers materials which you do not want to have dynamic amounts set.",
			"# Ie: the amounts that are set in the above offers.categories sections are exactly how many each town will receive, every time.",
			"# Materials put into this list, will not have their amounts modified by the Town's Town_Level resourceProductionModifier (in the Towny config,)",
			"# or by the productivity_percentage_per_resource_level settings in this config.",
			"# You may find it useful for controlling very valuable materials or custom items from Slimefun, MythicMobs or MMOItems."),
	TOWN_RESOURCES_LANGUAGE(
			"town_resources.language",
			"",
			"",
			""),			
	TOWN_RESOURCES_LANGUAGE_MATERIALS_DISPLAY_LANGUAGE(
			"town_resources.language.materials_display_language",
			"zh_cn",
			"",
			"# If you have the LanguageUtils plugin installed, materials will be automatically translated into this locale/language.");
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
