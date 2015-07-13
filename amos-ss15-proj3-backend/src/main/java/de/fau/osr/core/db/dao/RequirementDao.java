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
     * Looks up a Requirement in the database and if it does not exist already,
     * a row with the key id and all other fields null is inserted. 
     * @param id the unique key of this requirement
     * @return if already present in the database, the requirement corresponding
     * to id, otherwise a freshly created requirement
     */
    Requirement getRequirementById(String id);


}
