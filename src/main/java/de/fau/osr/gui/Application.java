package de.fau.osr.gui;


import de.fau.osr.core.db.HibernateUtil;

public class Application {
    static GuiController guiController;

    public static void main(String[] args) {
        guiController = new GuiController();
        HibernateUtil.shutdown();
    }
}
