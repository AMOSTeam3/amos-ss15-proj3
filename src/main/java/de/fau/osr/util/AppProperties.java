package de.fau.osr.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
/**
 * @author Gayathery
 * @desc class to query property pages
 *
 */
public class AppProperties {

	/**
	 * @param key
	 * @desc This method returns the values of properties represented by the param
	 * @author Gayathery
	 */
	public static String GetValue(String key)
	{
		Properties properties = new Properties();
		try {
		  properties.load(AppProperties.class.getResourceAsStream("/prop.properties"));
		  return properties.getProperty(key);
		} catch (IOException e)  {
		  e.printStackTrace();
		}
		 return null;
	}
}
