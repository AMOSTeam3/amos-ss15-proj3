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
package de.fau.osr.core.db;

import de.fau.osr.core.db.domain.Commit;
import de.fau.osr.core.db.domain.Requirement;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;


/**
 * This class Create the SessionFactory from hibernate.cfg.xml
 * @author Gayathery
 *
 */
public class HibernateUtil {

    static Logger logger = LoggerFactory.getLogger(HibernateUtil.class);

    private static SessionFactory sessionFactory;
    private static String dbUserName;
    private static String dbPassword;

    /**
     * generates configuration from hibernate.cfg.xml and adds all persistent classes to it
     * @return generated configuration
     */
    public static Configuration getConfiguration() {
        Configuration configuration = new Configuration().configure();
        //add persistent classes
        for(Class<?> clazz : getPersistentClasses())
        {
            configuration.addAnnotatedClass(clazz);
        }
        return configuration;
    }

    /**
     * @return singleton session factory
     */
    public static SessionFactory getSessionFactory() {

        if (sessionFactory == null){
            logger.debug("Static call : getsession factory() start::");

            //generate configuration
            org.hibernate.cfg.Configuration configuration = getConfiguration();
            configuration.setProperty("hibernate.connection.username",HibernateUtil.dbUserName);
            configuration.setProperty("hibernate.connection.password",HibernateUtil.dbPassword);

            StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
            sessionFactory = configuration.buildSessionFactory(builder.build());
            logger.debug("Static call : getsession factory() end::");
        }



        return sessionFactory;
   }
   
    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
            sessionFactory = null;
        }
    }

    public static Session getSession() {
        return getSessionFactory().openSession();
    }

    public void closeSession(Session session) {
        session.clear();
        session.close();
    }
    
    public static void setDBUsername(String username){
        HibernateUtil.dbUserName = username;
    }
    
    public static void setDBPassword(String password){
        HibernateUtil.dbPassword = password;
    }

    /**
     * generates persistent classes set, for now hardcoded.
     * todo use reflection, or spring
     * @see <a href="http://stackoverflow.com/questions/1413190/hibernate-mapping-package">http://stackoverflow.com/questions/1413190/hibernate-mapping-package</a>
     * @return set of classes are persistent
     */
    static public Set<Class<?>> getPersistentClasses(){
        //reflection way
//        Reflections reflections = new Reflections("your_package");
//        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(javax.persistence.Entity.class);

        //hardcoded way for now
        Set<Class<?>> classes = new HashSet<>();
        classes.add(Requirement.class);
        classes.add(Commit.class);

        return classes;
    }

}
