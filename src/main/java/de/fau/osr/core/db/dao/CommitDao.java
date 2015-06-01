package de.fau.osr.core.db.dao;

import de.fau.osr.core.db.DBOperation;
import de.fau.osr.core.db.dao.impl.AbstractDefaultDao;
import de.fau.osr.core.db.domain.Commit;

import java.io.Serializable;
import java.util.List;

/**
 * Data Access for Commit
 * Created by Dmitry Gorelenkov on 01.06.2015.
 */
public interface CommitDao {
    /**
     * @see AbstractDefaultDao#persist(DBOperation, Object)
     */
    boolean persist(DBOperation dbOperation,Commit requirement);

    /**
     * @see AbstractDefaultDao#getAllObjects()
     */
    List<Commit> getAllCommits();

    /**
     * @see AbstractDefaultDao#getObjectById(Serializable)
     */
    Commit getCommitById(String id);
}
