package io.github.townyadvanced.townyresources.enums;

/**
 * 
 * @author Goosius
 *
 */
public enum TownyResourcesPermissionNodes {

	TOWNY_RESOURCES_COMMAND("townyresources.command.*"),
	TOWNY_RESOURCES_COMMAND_SURVEY("townyresources.command.survey"),  //Do a resource survey in a town
	TOWNY_RESOURCES_COMMAND_TOWN_COLLECT("townyresources.command.towncollect"),  //Collect your town's share of extracted resources
	TOWNY_RESOURCES_COMMAND_NATION_COLLECT("townyresources.command.nationcollect"), //Collect your nation's share of extracted resources
	TOWNY_RESOURCES_ADMIN_COMMAND("townyresources.admin.command.*"),
	TOWNY_RESOURCES_ADMIN_COMMAND_RELOAD("townyresources.admin.command.reload"),
	TOWNY_RESOURCES_ADMIN_COMMAND_BYPASS("townyresources.admin.command.bypass"),
	TOWNY_RESOURCES_BYPASS("townyresources.bypass");

	private String value;

	/**
	 * Constructor
	 * 
	 * @param permission - Permission.
	 */
	TownyResourcesPermissionNodes(String permission) {

		this.value = permission;
	}

	/**
	 * Retrieves the permission node
	 * 
	 * @return The permission node
	 */
	public String getNode() {

		return value;
	}

	/**
	 * Retrieves the permission node
	 * replacing the character *
	 * 
	 * @param replace - String
	 * @return The permission node
	 */
	public String getNode(String replace) {

		return value.replace("*", replace);
	}

	public String getNode(int replace) {

		return value.replace("*", replace + "");
	}

}
