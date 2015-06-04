package de.fau.osr.util;

import java.io.IOException;
import java.util.Properties;
/**
 * class to query property pages
 * @author Gayathery
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

    /**
     * @param key
     * @desc This method returns the values of properties represented by the param as an integer
     * @author Gayathery
     */
    public static Integer GetValueAsInt(String key)
    {
        Properties properties = new Properties();
        try {
          properties.load(AppProperties.class.getResourceAsStream("/prop.properties"));
          return Integer.parseInt(properties.getProperty(key));
        } catch (IOException e)  {
          e.printStackTrace();
        }
         return null;
    }
}
