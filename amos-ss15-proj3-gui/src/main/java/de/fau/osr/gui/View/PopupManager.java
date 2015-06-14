package de.fau.osr.gui.View;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class PopupManager {

    /**
     * Setting up the initial Dialog to choose the Repository.
     * @return File to the chosen directory. Keep in mind, that the file is not checked yet.
     * @throws IOException whenever the user cancels the Dialog.
     * (This can not be solved)
     */
    public File Repo_OpeningDialog() throws IOException{
        JFileChooser chooser = new JFileChooser("src/main");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileHidingEnabled(false);

        int returnValue = chooser.showDialog(null,"Auswahl des Repository");

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }else {
            throw new IOException("Fehler bei er Repository Auswahl");
        }
    }

    /**
     * Opening the initial Dialog to choose a Pattern for searching for requirements in commit messages
     * or external sources.
     * @return String containing the directly the user input. Not yet checked whether it's a proper pattern
     */
    public String Pattern_OpeningDialog(String currentPattern) {
        String msg = "Gebe \"Requirement-Pattern\" als RegExp ein.\n" +
                "Mehrere \"Requirement-Pattern\" müssen gemäß der RegExp-Syntax\n" +
                "mit | (Pipe) getrennt eingegeben werden.";
        return JOptionPane.showInputDialog(msg, currentPattern);
    }

    /**
     * Method to open a option dialog for configuration where user can select which
     * configuration needs to be modified
     * @return integer containing the option selected for configuration (TODO int to be converted to
     * a proper enum)
     */
    public int Configure_OptionDialog(){
        Object[] options = { "Change Repository", "Change Requirement Pattern"};
        return JOptionPane.showOptionDialog(null, "Choose to Configure", "SpiceTraceability Configuration", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
    }

    /**
     * Showing an Dialog to the User. Marked as Error dialog.
     * @param message to be presented to the user
     */
    public void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "Fehler", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Showing a dialog to the user. Marked as Information dialog
     * @param message to be presented to the user
     */
    public void showInformationDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }
}