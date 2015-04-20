package de.fau.osr.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AppProperties {

	public static String GetValue(String key)
	{
		Properties properties = new Properties();
		try {
		  properties.load(new FileInputStream("prop.properties"));
		  return properties.getProperty(key);
		} catch (IOException e)  {
		  e.printStackTrace();
		}
		 return null;
	}
}
