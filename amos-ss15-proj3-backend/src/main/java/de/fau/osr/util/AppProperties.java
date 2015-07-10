/*
 * This file is part of Req-Tracker.
 *
 * Copyright (C) 2015 Taleh Didover, Florian Gerdes, Dmitry Gorelenkov,
 *     Rajab Hassan Kaoneka, Katsiaryna Krauchanka, Tobias Polzer,
 *     Gayathery Sathya, Lukas Tajak
 *
 * Req-Tracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Req-Tracker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Req-Tracker.  If not, see <http://www.gnu.org/licenses/>.
 */
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
