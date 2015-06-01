package de.fau.osr.core.db.dao;

import de.fau.osr.core.db.DBOperation;
import de.fau.osr.core.db.domain.Requirement;

import java.util.List;


/**
 * Data Access for Requirement 
 * @author Gayathery
 *
 */
public interface RequirementDao {

    boolean persist(DBOperation dbOperation,Requirement requirement);
    List<Requirement> getAllRequirements();
    Requirement getRequirementById(String id);


}
