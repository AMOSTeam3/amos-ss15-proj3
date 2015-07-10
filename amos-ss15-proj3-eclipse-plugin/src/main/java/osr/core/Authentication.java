/*
 * This file is part of ReqTracker.
 *
 * Copyright (C) 2015 Taleh Didover, Florian Gerdes, Dmitry Gorelenkov,
 *     Rajab Hassan Kaoneka, Katsiaryna Krauchanka, Tobias Polzer,
 *     Gayathery Sathya, Lukas Tajak
 *
 * ReqTracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ReqTracker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with ReqTracker.  If not, see <http://www.gnu.org/licenses/>.
 */
package osr.core;

import javax.swing.JFrame;

import de.fau.osr.gui.Authentication.LoginDialog;

public class Authentication {

    public boolean Perform(){
        final JFrame frame = new JFrame("ReqTracker Database Authentication : Login");
        LoginDialog loginDlg = new LoginDialog(frame,"ReqTracker Database Authentication : Login");
        loginDlg.setVisible(true);
        if(loginDlg.isSucceeded()){
            return true;
        }
        else{
            return false;
        }
    }
}
