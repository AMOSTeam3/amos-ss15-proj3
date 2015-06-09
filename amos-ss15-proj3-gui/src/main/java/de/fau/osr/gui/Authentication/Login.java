package de.fau.osr.gui.Authentication;

import de.fau.osr.core.db.HibernateUtil;

/**
 * class to set Login for Hibernate session
 * @author Gayathery * 
 *
 */
public class Login {
    
    public static boolean authenticate(String username, String password) {

        if(username.isEmpty() || password.isEmpty())
            return false;
        HibernateUtil.setDBUsername(username);
        HibernateUtil.setDBPassword(password);
       return true;
    }
}
