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
package de.fau.osr.core.db.dao.impl;

import de.fau.osr.core.db.DBOperation;
import de.fau.osr.core.db.dao.CommitDao;
import de.fau.osr.core.db.domain.Commit;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * implementation of CommitDao
 * Created by Dmitry Gorelenkov on 01.06.2015.
 */
public class CommitDaoImplementation extends AbstractDefaultDao<Commit> implements CommitDao {

    public CommitDaoImplementation(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public CommitDaoImplementation() {
        super();
    }

    @Override
    public boolean persist(DBOperation dbOperation, Commit commit) {
        return super.persist(dbOperation, commit);
    }

    @Override
    public List<Commit> getAllCommits() {
        return super.getAllObjects();
    }

    @Override
    public Commit getCommitById(String id) {
        return super.getObjectById(id);
    }
}
