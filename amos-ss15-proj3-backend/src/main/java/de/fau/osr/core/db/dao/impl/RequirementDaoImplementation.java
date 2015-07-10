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
package de.fau.osr.core.db.dao.impl;

import de.fau.osr.core.db.DBOperation;
import de.fau.osr.core.db.dao.RequirementDao;
import de.fau.osr.core.db.domain.Requirement;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * Implementation for Requirement Data Access methods.
 * @author Gayathery
 *
 */
public class RequirementDaoImplementation extends AbstractDefaultDao<Requirement> implements RequirementDao {

    static Logger logger = LoggerFactory.getLogger(RequirementDaoImplementation.class);

    public RequirementDaoImplementation(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public RequirementDaoImplementation() {
        super();
    }


    @Override
    public boolean persist(DBOperation dbOperation,Requirement requirement) {
        logger.debug("DB:persist() Requirements start()");

        boolean result = super.persist(dbOperation, requirement);

        logger.debug("DB:persist() Requirements end()");
        return result;
    }


    @Override
    public List<Requirement> getAllRequirements() {

        logger.debug("DB:getAllRequirements() start()");
        List<Requirement> requirements = getAllObjects();

        logger.debug("getAllRequirements() end()");
        return requirements;
    }

    @Override
    public Requirement getRequirementById(String id) {
        return getObjectById(id);
    }


}
