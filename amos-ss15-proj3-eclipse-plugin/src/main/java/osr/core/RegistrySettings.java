package osr.core;

import de.fau.osr.util.AppProperties;

/**
 * @author Gayathery
 * Class to hold values related to the session of the plugin
 *
 */
public class RegistrySettings {

	public static String repoURL;
	//TODO:Need to get it from User at the startup.
	public static String requirementPattern=AppProperties.GetValue("RequirementPattern");
}
