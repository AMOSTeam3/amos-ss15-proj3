package de.fau.osr.core.db.dao.impl;

import de.fau.osr.core.db.DBOperation;
import de.fau.osr.core.db.dao.RequirementDao;
import de.fau.osr.core.db.domain.Requirement;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * Implementation for Requirement Data Access methods.
 * @author Gayathery
 *
 */
public class RequirementDaoImplementation extends AbstractDefaultDao<Requirement> implements RequirementDao {

    static Logger logger = LoggerFactory.getLogger(RequirementDaoImplementation.class);

    public RequirementDaoImplementation(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public RequirementDaoImplementation() {
        super();
    }


    @Override
    public boolean persist(DBOperation dbOperation,Requirement requirement) {
        logger.debug("DB:persist() Requirements start()");

        boolean result = super.persist(dbOperation, requirement);

        logger.debug("DB:persist() Requirements end()");
        return result;
    }


    @Override
    public List<Requirement> getAllRequirements() {

        logger.debug("DB:getAllRequirements() start()");
        List<Requirement> requirements = getAllObjects();

        logger.debug("getAllRequirements() end()");
        return requirements;
    }

    @Override
    public Requirement getRequirementById(String id) {
        return getObjectById(id);
    }


}
