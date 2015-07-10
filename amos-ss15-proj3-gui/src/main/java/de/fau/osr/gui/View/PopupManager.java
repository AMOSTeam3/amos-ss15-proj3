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
package de.fau.osr.gui.View;

import de.fau.osr.gui.Authentication.LoginDialog;

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
        return JOptionPane.showOptionDialog(null, "Choose to Configure", "ReqTracker Configuration", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
    }
    
    /**
     * Method to open a option dialog to get acceptance from user for saving configuration details
     * @return integer containing the option selected (yes-0, no-1)
     */
    public int Configuration_Persist_OptionDialog(){
        Object[] options = { "Change Repository", "Change Requirement Pattern"};
        int retVal =  JOptionPane.showOptionDialog(null, "Do you want the application to remember the configuration details for the next sessions?", "Remember Configuration", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        return retVal;
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


    /**
     * authentication for database
     * @return true if success
     */
    public boolean Authentication(){
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