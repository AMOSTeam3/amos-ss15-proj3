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
import de.fau.osr.core.db.DBTestHelper;
import de.fau.osr.core.db.domain.Commit;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * test for CommitDaoImplementation (its more for Commit domain + AbstractDefaultDao)
 * Created by Dmitry Gorelenkov on 02.06.2015.
 */
public class CommitDaoImplementationTest {

    private SessionFactory currentSessionFactory;
    private CommitDaoImplementation comDao;

    @Before
    public void before() {
        currentSessionFactory = DBTestHelper.createH2SessionFactory();
        comDao = new CommitDaoImplementation(currentSessionFactory);
    }

    @Test
    public void persistTest() throws Exception {

        Commit commitExpected = new Commit("1");

        //when add
        comDao.persist(DBOperation.ADD, commitExpected);

        //then this commit is in db
        Commit commitActual = (Commit) currentSessionFactory.openSession().get(Commit.class, "1");
        assertEquals(commitExpected, commitActual);


        //when remove
        comDao.persist(DBOperation.DELETE, commitExpected);
        //then no object in db
        assertNull(currentSessionFactory.openSession().get(Commit.class, "1"));

    }

    @Test
    public void getAllCommitsTest() throws Exception {
        Commit first = genRandomCommit();
        List<Commit> realList = new ArrayList<>();
        comDao.persist(DBOperation.ADD, first);
        realList.add(first);

        assertEquals(realList, comDao.getAllCommits());
    }

    @Test
    public void getCommitByIdTest() throws Exception {
        Commit first = genRandomCommit();
        comDao.persist(DBOperation.ADD, first);
        assertEquals(first, comDao.getCommitById(first.getId()));
    }

    private Commit genRandomCommit(){
        return new Commit(String.valueOf(Math.random()));
    }
}