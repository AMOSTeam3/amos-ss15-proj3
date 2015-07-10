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
package de.fau.osr.core.db.dao;

import de.fau.osr.core.db.DBOperation;
import de.fau.osr.core.db.dao.impl.AbstractDefaultDao;
import de.fau.osr.core.db.domain.Requirement;

import java.io.Serializable;
import java.util.List;


/**
 * Data Access for Requirement 
 * @author Gayathery
 *
 */
public interface RequirementDao {

    /**
     * @see AbstractDefaultDao#persist(DBOperation, Object)
     */
    boolean persist(DBOperation dbOperation,Requirement requirement);

    /**
     * @see AbstractDefaultDao#getAllObjects()
     */
    List<Requirement> getAllRequirements();

    /**
     * @see AbstractDefaultDao#getObjectById(Serializable)
     */
    Requirement getRequirementById(String id);


}
