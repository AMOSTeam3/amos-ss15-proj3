package de.fau.osr.gui.View.ElementHandler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

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
        menuItem_Impact.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Thread tr = new Thread(action);
                tr.start();
            }
        });
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
