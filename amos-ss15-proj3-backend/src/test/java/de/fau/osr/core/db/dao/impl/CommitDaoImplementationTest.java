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