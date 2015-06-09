package osr.core;

import javax.swing.JFrame;

import de.fau.osr.gui.Authentication.LoginDialog;

public class Authentication {

    public boolean Perform(){
        final JFrame frame = new JFrame("SPICE Traceability Database Authentication : Login");
        LoginDialog loginDlg = new LoginDialog(frame,"SPICE Traceability Database Authentication : Login");
        loginDlg.setVisible(true);
        if(loginDlg.isSucceeded()){
            return true;
        }
        else{
            return false;
        }
    }
}
