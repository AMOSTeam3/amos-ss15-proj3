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
package de.fau.osr.gui.View.ElementHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuHandler {
    private JMenuBar menuBar = new JMenuBar();
    private JMenu menu_Tools = new JMenu("Tools");
    private JMenu submenu_TraceabilityMatrix = new JMenu("TraceabilityMatrix");

    private JMenuItem menuItem_Configure = new JMenuItem("Configure");
    private JMenuItem menuItem_Impact = new JMenuItem("Traceability Matrix By Impact");
    //DONOTREMOVE private JMenuItem menuItem_ByOtherData = new JMenuItem("Traceability Matrix By Other Data");
    
    public MenuHandler(){
        menuBar.add(menu_Tools);
        
        menu_Tools.add(menuItem_Configure);
        menu_Tools.add(submenu_TraceabilityMatrix);        
        menu_Tools.add(menuItem_Impact);
        //menu_Tools.add(menuItem_ByOtherData);
        
        submenu_TraceabilityMatrix.add(menuItem_Impact);
        //submenu_TraceabilityMatrix.add(menuItem_ByOtherData);
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }

    public void setConfigureAction(Runnable action) {
        menuItem_Configure.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                action.run();
            }
        });
    }
    
    public void setImpactAction(Runnable action) {
        if(menuItem_Impact.getActionListeners().length == 0){
        menuItem_Impact.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Thread tr = new Thread(action);
                tr.start();
            }
        });
        }
    }
    
    /*DONOTREMOVE
    public void setByOtherDataAction(Runnable action) {
        menuItem_ByOtherData.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Thread tr = new Thread(action);
                tr.start();
            }
        });
    }
    */
}
