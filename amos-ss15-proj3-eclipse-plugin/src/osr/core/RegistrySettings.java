package osr.core;

import de.fau.osr.util.AppProperties;

public class RegistrySettings {

	public static String repoURL;
	//TODO:Need to get it from User at the startup.
	public static String requirementPattern=AppProperties.GetValue("RequirementPattern");
}
