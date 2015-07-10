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
import de.fau.osr.core.db.dao.RequirementDao;
import de.fau.osr.core.db.domain.Requirement;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * This tests are mostly for AbstractDefaultDao class
 * Created by Dmitry Gorelenkov on 01.06.2015.
 */
public class RequirementDaoImplementationTest {
    RequirementDao dao;
    private SessionFactory currentSessionFactory;

    /**
     * generates some random requirement
     * @return generated requirement
     */
    private Requirement genRandomReq(){
        Requirement r = new Requirement();
        r.setId("Test"+Math.random());
        r.setTitle("title-test"+Math.random());
        r.setDescription("desc-test"+Math.random());
        r.setStoryPoint(new Random().nextInt(10));
        return r;
    }


    @Before
    public void before() {
        currentSessionFactory = DBTestHelper.createH2SessionFactory();
        dao = new RequirementDaoImplementation(currentSessionFactory);
    }


    @Test
    public void persist_addedRequirement_shouldExists() throws Exception {
        //given
        Session session = currentSessionFactory.openSession();
        Requirement tempRequirement = genRandomReq();

        //when add temp req
        dao.persist(DBOperation.ADD, tempRequirement);

        //then same req could be loaded from db
        Requirement reqFromDb = (Requirement) session.get(tempRequirement.getClass(), tempRequirement.getId());
        assertEquals(tempRequirement, reqFromDb);
    }

    @Test
    public void getAllRequirementTest() throws Exception {
        //this test expects that persist method works

        //should be empty at beginning
        assertTrue(dao.getAllRequirements().size() == 0);

        Requirement tempRequirement = genRandomReq();
        ArrayList<Requirement> addedReqs = new ArrayList<>();

        //when add one
        addedReqs.add(tempRequirement);
        dao.persist(DBOperation.ADD, tempRequirement);
        //then
        assertEquals(addedReqs, dao.getAllRequirements());

        //when add 1000 more
        for (int i = 0; i < 1000; i++) {
            Requirement requirement = genRandomReq();
            addedReqs.add(requirement);
            dao.persist(DBOperation.ADD, requirement);
        }
        //then
        assertEquals(addedReqs, dao.getAllRequirements());

        //when remove 1
        dao.persist(DBOperation.DELETE, tempRequirement);
        addedReqs.remove(tempRequirement);
        //then
        assertEquals(addedReqs, dao.getAllRequirements());

        //when remove all one by one
        for (Requirement req : dao.getAllRequirements()) {
            dao.persist(DBOperation.DELETE, req);
        }
        //then is empty
        assertTrue(dao.getAllRequirements().isEmpty());
    }

    @Test
    public void getRequirementByIdTest_existsOne_shouldBeReturned() throws Exception {
        //given
        Requirement tempRequirement = genRandomReq();
        dao.persist(DBOperation.ADD, tempRequirement);
        //when
        Requirement fromDb = dao.getRequirementById(tempRequirement.getId());
        //then
        assertEquals(tempRequirement, fromDb);
    }

    @Test
    public void getRequirementByIdTest_existsMany_oneShouldBeReturned() throws Exception {
        //given
        Requirement someMiddleAddedReq = null;
        for (int i = 0; i < 1000; i++) {
            Requirement reqToAdd = genRandomReq();

            if (i == 815){
                someMiddleAddedReq = reqToAdd;
            }

            dao.persist(DBOperation.ADD, reqToAdd);
        }
        //when
        assertNotNull(someMiddleAddedReq); //to be sure
        Requirement fromDb = dao.getRequirementById(someMiddleAddedReq.getId());
        //then
        assertEquals(someMiddleAddedReq, fromDb);
    }

    @Test
    public void getRequirementByIdTest_DbIsEmpty_shouldReturnNull() throws Exception {
        //given
        Requirement tempRequirement = genRandomReq();
        //when
        Requirement fromDb = dao.getRequirementById(tempRequirement.getId());
        //then
        assertNull(fromDb);
    }

    @Test
    public void getRequirementByIdTest_getNotExistsIdFromFilledDb_shouldReturnNull() throws Exception {
        //given
        for (int i = 0; i < 1000; i++) {
            dao.persist(DBOperation.ADD, genRandomReq());
        }
        //when
        Requirement fromDb = dao.getRequirementById("this id should not exists in db");
        //then
        assertNull("queried non existing id, returned some not null object", fromDb);
    }
}
