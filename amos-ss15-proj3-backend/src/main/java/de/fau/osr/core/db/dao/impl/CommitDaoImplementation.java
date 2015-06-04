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
