package de.fau.osr.core.db.dao.impl;

import de.fau.osr.core.db.DBOperation;
import de.fau.osr.core.db.HibernateUtil;
import org.hibernate.*;
import org.hibernate.transform.DistinctRootEntityResultTransformer;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * Default DAO that uses hibernate database, with generic entity type
 * Created by Dmitry Gorelenkov on 01.06.2015.
 */
public abstract class AbstractDefaultDao<EntityClass> {
    SessionFactory sessionFactory;

    /**
     * uses <tt>sessionFactory</tt> for sessions
     *
     * @param sessionFactory factory to use
     */
    public AbstractDefaultDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * uses default <tt>HibernateUtil.getSessionFactory()</tt> factory sessions
     *
     * @see HibernateUtil#getSessionFactory()
     */
    public AbstractDefaultDao() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    /**
     * http://stackoverflow.com/questions/3437897/how-to-get-class-instance-of-generics-type-t
     *
     * @return generic Class of this instance
     */
    protected Class getEntityType() {
        return ((Class) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0]);
    }


    /**
     * apply persistence operation <tt>dbOperation</tt> to  <tt>obj</tt>
     *
     * @param dbOperation operation to apply
     * @param obj         object to apply to
     * @return true if success, false otherwise
     */
    protected boolean persist(DBOperation dbOperation, EntityClass obj) {
        try {
            Session session = sessionFactory.openSession();
            Transaction t = null;
            try {
                t = session.beginTransaction();
                switch (dbOperation) {
                    case ADD:
                        session.persist(obj);
                        break;
                    case UPDATE:
                        session.update(obj);
                        break;
                    case DELETE:
                        session.delete(obj);
                        break;
                }

                t.commit();

            } catch (HibernateException e) {
                if (t != null) {
                    t.rollback();
                }
                throw e;
            } finally {
                session.close();
            }

        } catch (RuntimeException re) {
            re.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * @return all objects of this Entity type
     */
    @SuppressWarnings("unchecked")
    protected List<EntityClass> getAllObjects() {

        List<EntityClass> objects = new ArrayList<>();
        Session session = null;
        try {
            session = sessionFactory.openSession();

            Criteria criteria = session.createCriteria(getEntityType());
            //make uniq, criteria returns many double entities, because of fetched relations
            //http://stackoverflow.com/questions/8758363/why-session-createcriteriaclasstype-list-return-more-object-than-in-list
            criteria.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);

            objects = criteria.list();

        } catch (RuntimeException re) {
            re.printStackTrace();

        } finally {
            if (session != null) {
                session.close();
            }
        }

        return objects;
    }

    /**
     * @param id if of the object
     * @return object founded by id, or null in case nothing found or an error occurred
     */
    @SuppressWarnings("unchecked")
    protected EntityClass getObjectById(Serializable id) {
        EntityClass objects = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            objects = (EntityClass) session.get(getEntityType(), id);

        } catch (RuntimeException re) {
            re.printStackTrace();

        } finally {
            if (session != null) {
                session.close();
            }
        }

        return objects;
    }
}
