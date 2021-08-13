package io.github.townyadvanced.blankplugin.settings;

public enum ConfigNodes {
	
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
   		    "# If town resources AND player extraction restrictions are enabled together, the following benefits are gained:",
			"# ",
			"# 1. A natural and strategic 'reason to attack' is established for each town,",
			"#    encouraging healthier and less-toxic warfare.",
			"# 2. Each town naturally becomes a 'trade-center' for a handful of resources,",
		    "#    encouraging healthier player-trading patterns.",
		    "#,",
		    "# WARNING: It is not recommended to enable town_resources without also enabling player_extraction_restrictions",
		    "#          because industrial resource harvesting by players will degrade the value",
		    "#          of many town resources, thus nullifying the trading/strategic benefits of those resources.",
		    "# ",
		    "#          With this setup, mitigation could be attempt by regularly modifying this file ",
		    "#			to increase the value of affected resource offers.",
		    "#          However this is both error-prone, extra staff work, and largely pointless.",
		    "# ",
		    "#          EXAMPLE: If a town discovers a wheat resource of 100/day,",
		    "#                   and industrial food production later floods the market with wheat, causing it to be almost free,",
		    "#                   then increasing the resources's productivity by even something large like +500",
		    "#                   will simply flood the market further, and do little to restore the town's value."),
	TOWN_RESOURCES_NATION_TAX_PERCENTAGE(
			"town_resources.nation_tax_percentage",
			"50",
			"",
			"# The owner nation of a town gets this percentage of the town resources.",
			"# The town gets the rest."),
	TOWN_RESOURCES_DISCOVERY_DAYS_REQUIREMENT_PER_RESOURCE_LEVEL(
			"town_resources.discovery_days_requirement_per_resource_level",
			"10, 10, 10, 10",
			"",
			"# This list of values determines how many days it takes for a town to discover its resources.",
			"# Example: If the first value is 10, then it will take the town 10 days, at town level 1 or above,",
			"# to discover its level 1 resource."),
	TOWN_RESOURCES_TOWN_LEVEL_REQUIREMENT_PER_RESOURCE_LEVEL(
			"town_resources.town_level_requirement_per_resource_level",
			"1, 3, 5, 7",
			"",
			"# This list of values determines the level a town needs to be, to discover its resources.",
			"# Example: If the first value is 1, then the town has to be level 1 to discover its level 1 resource."),
	TOWN_RESOURCES_PRODUCTIVITY_PERCENTAGE_PER_RESOURCE_LEVEL(
			"town_resources.productivity_percentage_per_resource_level",
			"100, 150, 300, 500",
			"",
			"# This list of values determines the productivity of each level of resource.",
			"# Example: If the second value is 200, then the level 2 resource of a town will be 200% productive."),
	TOWN_RESOURCES_OFFERS(
			"towny.resources_offers",
			"",
			"",
			"",
			"############################################################",
			"# +------------------------------------------------------+ #",
			"# |                       OFFERS                         | #",
			"# +------------------------------------------------------+ #",
			"############################################################",
			""),
	TOWN_RESOURCES_OFFERS_DEPOSITS(
			"town_resources.offers.deposits",
			"{GOLD_ORE, 8, 50}, {NETHERITE_ORE, 2, 10}",
			"",
			"# This list determines the offers of 'deposits' resources which each town can gain.",
			"# Each entry on the list has 3 values:",
			"# 1 - Material Name of resource",
			"# 2 - Base resource amount",
			"# 3 - Discovery weight (the higher this is, the more likely its discovery"),
	TOWN_RESOURCES_OFFERS_FORESTS(
			"town_resources.offers.forests",
			"{OAK_LOG, 200, 10}",
			"",
			"# This list determines the offers of 'forests' resources which each town can gain.",
			"# Each entry on the list has 3 values:",
			"# 1 - Material Name of resource",
			"# 2 - Base resource amount",
			"# 3 - Discovery weight (the higher this is, the more likely its discovery"),
	TOWN_RESOURCES_OFFERS_FERTILE_SOIL(
			"town_resources.offers.fertile_soil",
			"{WHEAT, 100 , 100}",
			"",
			"# This list determines the offers of 'fertile soil' resources which each town can gain.",
			"# Each entry on the list has 3 values:",
			"# 1 - Material Name of resource",
			"# 2 - Base resource amount",
			"# 3 - Discovery weight (the higher this is, the more likely its discovery"),
	TOWN_RESOURCES_OFFERS_HERD_OF_ANIMALS(
			"town_resources.offers.herd_of_animals",
			"",
			"{BEEF, 20 , 100}",
			"# This list determines the offers of 'herd of animals' resources which each town can gain.",
			"# Each entry on the list has 3 values:",
			"# 1 - Material Name of resource",
			"# 2 - Base resource amount",
			"# 3 - Discovery weight (the higher this is, the more likely its discovery"),
	TOWN_RESOURCES_OFFERS_HERD_OF_MONSTERS(
			"town_resources.offers.herd_of_monsters",
			"",
			"",
			"# This list determines the offers of 'herd of monsters' resources which each town can gain.",
			"# Each entry on the list has 3 values:",
			"# 1 - Material Name of resource",
			"# 2 - Base resource amount",
			"# 3 - Discovery weight (the higher this is, the more likely its discovery"),			
	PLAYER_EXTRACTION_RESTRICTIONS(
			"player_extraction_restrictions",
			"",
			"",
			"",
			"############################################################",
			"# +------------------------------------------------------+ #",
			"# |            Player Extraction Restrictions            | #",
			"# +------------------------------------------------------+ #",
			"############################################################",
			""),
	PLAYER_EXTRACTION_RESTRICTIONS_ENABLED(
			"player_extraction_restrictions.enabled",
			"true",
			"",
			"# If true, then player extraction restrictions are enabled.",
			"# If false, then player extraction restrictions are disabled."),
			
			
			
Productivity Rate Per Resource Level
100
150   ..... i.e. the level 2 resource is +50%
30)0
500
			Town Level Requirement per resource level

			
			
			Discovery Days Requirement per resource level
10  ......... takes 10 days AT the required level or more to get the lev 1 resource
10   ........ takes 10 MORE days at the required level or more to get the lev 2 resource
10
10
	
	private final String Root;
	private final String Default;
	private String[] comments;

	ConfigNodes(String root, String def, String... comments) {

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
