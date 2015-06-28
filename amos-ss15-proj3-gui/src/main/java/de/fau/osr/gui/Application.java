package de.fau.osr.gui;


import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import de.fau.osr.gui.Controller.GuiController;

public class Application {
    static GuiController guiController;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        guiController = new GuiController();
        
    }
}
