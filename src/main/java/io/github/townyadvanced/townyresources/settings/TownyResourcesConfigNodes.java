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
	PLAYER_EXTRACTION_LIMITS(
			"player_extraction_limits",
			"",
			"",
			"",
			"############################################################",
			"# +------------------------------------------------------+ #",
			"# |               Player Extraction Limits               | #",
			"# +------------------------------------------------------+ #",
			"############################################################",
			""),
	PLAYER_EXTRACTION_LIMITS_ENABLED(
			"player_extraction_limits.enabled",
			"true",
			"",
			"# If true, then player extraction limits are enabled.",
			"# If false, then player extraction limits are disabled."),
			
			//TODO ------- Add machine-limiting stuff here e.g. for cactuses and sugar
			
	PLAYER_EXTRACTION_LIMITS_DAILY_LIMITS(
			"player_extraction_limits.daily_limits",
			"",
			"",
			"",
			"############################################################",
			"# +------------------------------------------------------+ #",
			"# |                    DAILY LIMITS                        | #",
			"# +------------------------------------------------------+ #",
			"############################################################",
			""),
	PLAYER_EXTRACTION_LIMITS_DAILY_LIMITS_ORES(
			"player_extraction_limits.daily_limits.ores",
			"ANCIENT_DEBRIS-0.03125-10, DIAMOND-0.125-20, EMERALD-0.25-20, GOLD_ORE-0.25-20",
			"",
			"# This list determines the daily extraction limits for players for 'ores' resources.",
			"# Each entry on the list has 2 values:",
			"# 1 - Name of resource",
			"# 2 - Daily Limit, in stacks"),
	PLAYER_EXTRACTION_LIMITS_DAILY_LIMITS_TREES(
			"player_extraction_limits.daily_limits.trees",
			"OAK_LOG-1",
			"",
			"# This list determines the daily extraction limits for players for 'trees' resources.",
			"# Each entry on the list has 2 values:",
			"# 1 - Name of resource",
			"# 2 - Daily Limit, in stacks"),
	PLAYER_EXTRACTION_LIMITS_DAILY_LIMITS_CROPS(
			"player_extraction_limits.daily_limits.crops",
			"WHEAT-1",
			"",
			"# This list determines the daily extraction limits for players for 'crops' resources.",
			"# Each entry on the list has 2 values:",
			"# 1 - Name of resource",
			"# 2 - Daily Limit, in stacks"),
	PLAYER_EXTRACTION_LIMITS_DAILY_LIMITS_ANIMALS(
			"player_extraction_limits.daily_limits.animals",
			"BEEF-0.25",
			"",
			"# This list determines the daily extraction limits for players for 'animals' resources.",
			"# Each entry on the list has 2 values:",
			"# 1 - Name of resource",
			"# 2 - Daily Limit, in stacks"),
	PLAYER_EXTRACTION_LIMITS_DAILY_LIMITS_MONSTERS(
			"player_extraction_limits.daily_limits.monsters",
			"",
			"",
			"# This list determines the daily extraction limits for players for 'monster' resources.",
			"# Each entry on the list has 2 values:",
			"# 1 - Name of resource",
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
	TOWN_RESOURCES_ENABLED(
			"town_resources.enabled",
			"true",
			"",
			"# If true, tows get resources.",
			"# if false, towns do not get resource.",
			"# ",
   		    "# If town resources AND player extraction limits are enabled together, the following benefits are gained:",
			"# ",
			"# 1. A natural and strategic 'reason to attack' is established for each town,",
			"#    encouraging healthier and less-toxic warfare.",
			"# 2. Each town naturally becomes a 'trade-center' for a handful of resources,",
		    "#    encouraging healthier player-trading patterns.",
		    "#,",
		    "# WARNING: It is not recommended to enable town_resources without also enabling player_extraction_limits",
		    "#          because industrial resource harvesting by players will degrade the value",
		    "#          of many town resources, thus nullifying the trading/strategic benefits of those resources.",
		    "# ",
		    "#          With this setup, mitigation of industrial harvesting could be attempt by continually modifying this file ",
		    "#			to increase the value of affected resource offers.",
		    "#          However this is extra staff work, error-prone, and largely pointless.",
		    "# ",
		    "#          EXAMPLE: If a town discovers a wheat resource of 100/day,",
		    "#                   and industrial food production later floods the market with wheat, causing it to be almost free,",
		    "#                   then increasing the resources's productivity by even something large like +500",
		    "#                   will simply flood the market further, and do little to restore the town's value."),
	TOWN_RESOURCES_SURVEY_COST_PER_RESOURCE_LEVEL(
			"town_resources.survey_cost_per_resource_level",
			"250, 1000, 5000, 20000",
			"",
			"# This list of values determines how much it costs to do a survey for each resource level.",
			"# Example: If the first value is 250, then it will cost 250 to survey and discover the level 1 resource."),
	TOWN_RESOURCES_NUM_TOWNBLOCKS_REQUIREMENT_PER_RESOURCE_LEVEL(
			"town_resources.num_townblocks_requirement_per_resource_level",
			"10, 50, 100, 200",
			"",
			"# This list of values determines how much many townblocks a town must have to DISCOVER each level of resource.",
			"# Example: If the first value is 10, then the town must have 10 townblocks to survey and discover the level 1 resource."),
	TOWN_RESOURCES_TOWN_LEVEL_REQUIREMENT_PER_RESOURCE_LEVEL(
			"town_resources.town_level_requirement_per_resource_level",
			"1, 4, 6, 8",
			"",
			"# This list of values determines the level a town needs to be, to USE its resources.",
			"# Example: If the first value is 1, then the town has to be level 1 to use its level 1 resource."),
	TOWN_RESOURCES_PRODUCTIVITY_PERCENTAGE_PER_RESOURCE_LEVEL(
			"town_resources.productivity_percentage_per_resource_level",
			"100, 200, 300, 400",
			"",
			"# This list of values determines the productivity of each level of resource.",
			"# Example: If the second value is 200, then the level 2 resource of a town will be 200% productive."),
	TOWN_RESOURCES_NATION_TAX_PERCENTAGE(
			"town_resources.nation_tax_percentage",
			"75",
			"",
			"# The owner nation of a town gets this percentage of the town resources.",
			"# The town gets the rest."),
	TOWN_RESOURCES_OFFERS(
			"town_resources.offers",
			"",
			"",
			"",
			"############################################################",
			"# +------------------------------------------------------+ #",
			"# |                       OFFERS                         | #",
			"# +------------------------------------------------------+ #",
			"############################################################",
			""),
	TOWN_RESOURCES_OFFERS_ORES(
			"town_resources.offers.ores",
			"ANCIENT_DEBRIS-0.0625-10, DIAMOND-0.25-20, EMERALD-0.5-20, GOLD_ORE-0.5-20",
			"",
			"# This list determines the offers of 'ores' resources which each town can gain.",
			"# Each entry on the list has 3 values:",
			"# 1 - Name of resource",
			"# 2 - Base daily production, in stacks",
			"# 3 - Discovery weight (the higher this is, the more likely its discovery"),
	TOWN_RESOURCES_OFFERS_TREES(
			"town_resources.offers.trees",
			"OAK_LOG-2-5",
			"",
			"# This list determines the offers of 'trees' resources which each town can gain.",
			"# Each entry on the list has 3 values:",
			"# 1 - Name of resource",
			"# 2 - Base daily production, in stacks",
			"# 3 - Discovery weight (the higher this is, the more likely its discovery"),
	TOWN_RESOURCES_OFFERS_CROPS(
			"town_resources.offers.crops",
			"WHEAT-2-5",
			"",
			"# This list determines the offers of 'crops' resources which each town can gain.",
			"# Each entry on the list has 3 values:",
			"# 1 - Name of resource",
			"# 2 - Base daily production, in stacks",
			"# 3 - Discovery weight (the higher this is, the more likely its discovery"),
	TOWN_RESOURCES_OFFERS_ANIMALS(
			"town_resources.offers.animals",
			"BEEF-0.5-5",
			"",
			"# This list determines the offers of 'animals' resources which each town can gain.",
			"# Each entry on the list has 3 values:",
			"# 1 - Name of resource",
			"# 2 - Base daily production, in stacks",
			"# 3 - Discovery weight (the higher this is, the more likely its discovery"),
	TOWN_RESOURCES_OFFERS_MONSTERS(
			"town_resources.offers.monsters",
			"",
			"",
			"# This list determines the offers of  monsters' resources which each town can gain.",
			"# Each entry on the list has 3 values:",
			"# 1 - Name of resource",
			"# 2 - Base daily production, in stacks",
			"# 3 - Discovery weight (the higher this is, the more likely its discovery");

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
