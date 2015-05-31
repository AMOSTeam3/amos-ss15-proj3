package de.fau.osr.core.db.dao.impl;

import de.fau.osr.core.db.DBOperation;
import de.fau.osr.core.db.HibernateUtil;
import de.fau.osr.core.db.dao.RequirementDao;
import de.fau.osr.core.db.domain.Requirement;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * Implementation for Requirement Data Access methods.
 * @author Gayathery
 *
 */
public class RequirementDaoImplementation implements RequirementDao{

    static Logger logger = LoggerFactory.getLogger(RequirementDaoImplementation.class);

    SessionFactory sessionFactory;

    public RequirementDaoImplementation(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public RequirementDaoImplementation() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }


    @Override
    public boolean persist(DBOperation dbOperation,Requirement requirement) {
        logger.debug("DB:add()Requirements start()");
        try {
             Session session=sessionFactory.openSession();
               Transaction t=null;
               try{
                   t=session.beginTransaction();
                   switch(dbOperation){
                       case ADD:
                           session.persist(requirement);
                           break;
                       case UPDATE:
                           session.update(requirement);
                           break;
                       case DELETE:
                           session.delete(requirement);
                           break;
                   }

                   t.commit();

               }catch (HibernateException e) {
                   if (t != null) {
                       t.rollback();
                   }
                   throw e;
               }finally {
                    session.close();
                }

        }
        catch (RuntimeException re) {
            re.printStackTrace();
            return false;
        }
        logger.debug("DB:add()Requirements end()");
        return true;
    }


    @SuppressWarnings("unchecked")
    @Override
    public List<Requirement> getAllRequirement() {

        logger.debug("DB:getAllrequirements() start()");
        List<Requirement> requirements = new ArrayList<>();
        try {
             Session session=sessionFactory.openSession();
              try{
                 requirements = session.createCriteria(Requirement.class).list();
               }catch (HibernateException e) {
                   throw e;
               }finally {
                    session.close();
                }

        }
        catch (RuntimeException re) {
            re.printStackTrace();

        }
        logger.debug("getAllrequirements() end()");
        return requirements;
    }

    @Override
    public Requirement getRequirementById(String id) {
        Requirement requirement = null;
        try {
            Session session=sessionFactory.openSession();
            try{
                requirement = (Requirement) session.get(Requirement.class, id);
            }catch (HibernateException e) {
                throw e;
            }finally {
                session.close();
            }

        }
        catch (RuntimeException re) {
            re.printStackTrace();

        }

        return requirement;
    }


}
