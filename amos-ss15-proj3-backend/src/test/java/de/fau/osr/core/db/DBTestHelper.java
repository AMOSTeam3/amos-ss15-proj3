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
package de.fau.osr.core.db;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 * This class contains useful methods for tests
 * Created by Dmitry Gorelenkov on 31.05.2015.
 */
public class DBTestHelper {

    /**
     * creates session factory to work with H2 db
     * @return new H2 session factory
     */
    public static SessionFactory createH2SessionFactory() {


        Configuration configuration = createH2Configuration();

        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
                configuration.getProperties()).build();

        return configuration.buildSessionFactory(serviceRegistry);
    }

    /**
     * creates configuration from original, and change connection to H2 db
     * sets "hibernate.hbm2ddl.auto" to "create"
     * @return configuration to work with H2
     */
    public static Configuration createH2Configuration(){
        // get original configuration
        Configuration configuration = new Configuration();

        //add persistent classes
        for(Class<?> clazz : HibernateUtil.getPersistentClasses())
        {
            configuration.addAnnotatedClass(clazz);
        }

        //change connection settings
        configuration.setProperty("hibernate.dialect",
                "org.hibernate.dialect.H2Dialect");
        configuration.setProperty("hibernate.connection.driver_class",
                "org.h2.Driver");

        //reset schema
        configuration.setProperty("hibernate.default_schema", "PUBLIC");

        //set db mode to create
        configuration.setProperty("hibernate.hbm2ddl.auto", "create");

        //connection string
        configuration.setProperty("hibernate.connection.url", "jdbc:h2:mem:test");

        return configuration;
    }
}
