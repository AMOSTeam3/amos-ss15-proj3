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
package osr.adapter;


import de.fau.osr.gui.Authentication.Login;

/**
 * @author Gayathery
 *
 */
public class PluginSPICEAuthenticationAdaptor {

    /**
     * This method sets the username and password for the database using the Login class.
     * @param username
     * @param password
     * @return
     */
    public boolean authenticate(String username, String password){
      try{
        if(Login.authenticate(username, password))
            return true;
       
      }catch(Exception e){
          e.printStackTrace();
      }
      return false;
    }
}
