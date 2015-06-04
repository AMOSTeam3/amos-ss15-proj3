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
