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
	DAILY_PLAYER_EXTRACTION_LIMITS(
			"daily_player_extraction_limits",
			"",
			"",
			"",
			"############################################################",
			"# +------------------------------------------------------+ #",
			"# |           Daily Player Extraction Limits             | #",
			"# +------------------------------------------------------+ #",
			"############################################################",
			""),
	DAILY_PLAYER_EXTRACTION_LIMITS_ENABLED(
			"daily_player_extraction_limits.enabled",
			"true",
			"",
			"# If true, then daily player extraction limits are enabled.",
			"# If false, then daily player extraction limits are disabled."),
			
			//TODO ------- Add machine-limiting stuff here e.g. for cactuses and sugar
			
	DAILY_PLAYER_EXTRACTION_LIMITS_ORES(
			"daily_player_extraction_limits.ores",
			"Ancient_Debris-Ancient debris-0.03125, Diamond_Ore-Diamonds-0.125, Emerald-Emeralds-0.25, Gold_Ore-Gold-0.25",
			"",
			"# This list determines the daily extraction limits for players for 'ores' resources.",
			"# Each entry on the list has 3 values:",
			"# 1 - Source block material",
			"# 2 - Resource to display",
			"# 2 - Daily Limit, in stacks"),
	DAILY_PLAYER_EXTRACTION_LIMITS_TREES(
			"daily_player_extraction_limits.trees",
			"Oak_Log-Oak logs-1",
			"",
			"# This list determines the daily extraction limits for players for 'trees' resources.",
			"# Each entry on the list has 3 values:",
			"# 1 - Source block material",
			"# 2 - Resource to display",
			"# 2 - Daily Limit, in stacks"),
	DAILY_PLAYER_EXTRACTION_LIMITS_CROPS(
			"daily_player_extraction_limits.crops",
			"Wheat-Wheat-1",
			"",
			"# This list determines the daily extraction limits for players for 'crops' resources.",
			"# Each entry on the list has 3 values:",
			"# 1 - Source block material",
			"# 2 - Resource to display",
			"# 2 - Daily Limit, in stacks"),
	DAILY_PLAYER_EXTRACTION_LIMITS_ANIMALS(
			"daily_player_extraction_limits.animals",
			"Beef-Beef-0.25",
			"",
			"# This list determines the daily extraction limits for players for 'animals' resources.",
			"# Each entry on the list has 2 values:",
			"# 1 - Source material",
			"# 2 - Resource to display",
			"# 2 - Daily Limit, in stacks"),
	DAILY_PLAYER_EXTRACTION_LIMITS_MONSTERS(
			"daily_player_extraction_limits.monsters",
			"",
			"",
			"# This list determines the daily extraction limits for players for 'monster' resources.",
			"# Each entry on the list has 2 values:",
			"# 1 - Source material",
			"# 2 - Resource to display",
			"# 2 - Daily Limit, in stacks"),
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
			"3",
			"",
			"# This value determines the limit of how many resources of each type can be stored for collection.",
			"# Example: If this value 3, and the daily production amount is 32 ..... then the storage limit is 96."),
	TOWN_RESOURCES_PRODUCTION_NATION_TAX_PERCENTAGE(
			"town_resources.production.nation_tax_percentage",
			"75",
			"",
			"# The owner nation of a town gets this percentage of town production.",
			"# The town gets the rest."),
	TOWN_RESOURCES_OFFERS(
			"town_resources.offers",
			"",
			"",
			""),
	TOWN_RESOURCES_OFFERS_ORES(
			"town_resources.offers.ores",
			"Ancient_Debris-0.0625-10, Diamond-0.25-20, Emerald-0.5-20, Gold_Ore-0.5-20",
			"",
			"# This list determines the offers of 'ores' resources which each town can gain.",
			"# Each entry on the list has 3 values:",
			"# 1 - Name of resource",
			"# 2 - Base daily production, in stacks",
			"# 3 - Discovery chance weight (the higher this is, the more likely its discovery"),
	TOWN_RESOURCES_OFFERS_TREES(
			"town_resources.offers.trees",
			"Oak_Log-2-5",
			"",
			"# This list determines the offers of 'trees' resources which each town can gain.",
			"# Each entry on the list has 3 values:",
			"# 1 - Name of resource",
			"# 2 - Base daily production, in stacks",
			"# 3 - Discovery chance weight (the higher this is, the more likely its discovery"),
	TOWN_RESOURCES_OFFERS_CROPS(
			"town_resources.offers.crops",
			"Wheat-2-5",
			"",
			"# This list determines the offers of 'crops' resources which each town can gain.",
			"# Each entry on the list has 3 values:",
			"# 1 - Name of resource",
			"# 2 - Base daily production, in stacks",
			"# 3 - Discovery chance weight (the higher this is, the more likely its discovery"),
	TOWN_RESOURCES_OFFERS_ANIMALS(
			"town_resources.offers.animals",
			"Beef-0.5-5",
			"",
			"# This list determines the offers of 'animals' resources which each town can gain.",
			"# Each entry on the list has 3 values:",
			"# 1 - Name of resource",
			"# 2 - Base daily production, in stacks",
			"# 3 - Discovery chance weight (the higher this is, the more likely its discovery"),
	TOWN_RESOURCES_OFFERS_MONSTERS(
			"town_resources.offers.monsters",
			"",
			"",
			"# This list determines the offers of  monsters' resources which each town can gain.",
			"# Each entry on the list has 3 values:",
			"# 1 - Name of resource",
			"# 2 - Base daily production, in stacks",
			"# 3 - Discovery chance weight (the higher this is, the more likely its discovery");

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
