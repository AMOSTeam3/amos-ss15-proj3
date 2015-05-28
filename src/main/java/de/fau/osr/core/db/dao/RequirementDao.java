package de.fau.osr.core.db.dao;

import java.util.List;

import de.fau.osr.core.db.DBOperation;
import de.fau.osr.core.db.domain.Requirement;


/**
 * Data Access for Requirement 
 * @author Gayathery
 *
 */
public interface RequirementDao {
	
	boolean persist(DBOperation dbOperation,Requirement requirement);	
	List<Requirement> getAllRequirement();
	Requirement getRequirementById(String id);
	

}
