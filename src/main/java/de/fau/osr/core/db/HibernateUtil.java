package de.fau.osr.core.db;

import de.fau.osr.core.db.domain.Requirement;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;


/**
 * This class Create the SessionFactory from hibernate.cfg.xml
 * @author Gayathery
 *
 */
public class HibernateUtil {

    static Logger logger = LoggerFactory.getLogger(HibernateUtil.class);

    private static SessionFactory sessionFactory ;
    static {
        logger.debug("Static call : getsession factory() start::");

        //generate configuration
        Configuration configuration = getConfiguration();

        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        sessionFactory = configuration.buildSessionFactory(builder.build());
        
        logger.debug("Static call : getsession factory() end::");
    }

    /**
     * generates configuration from hibernate.cfg.xml and adds all persistent classes to it
     * @return generated configuration
     */
    public static Configuration getConfiguration() {
        Configuration configuration = new Configuration().configure();
        //add persistent classes
        for(Class<?> clazz : getPersistentClasses())
        {
            configuration.addAnnotatedClass(clazz);
        }
        return configuration;
    }

    public static SessionFactory getSessionFactory() {
       return sessionFactory;
   }
   
    public static void shutdown() {
        getSessionFactory().close();
    }

    public static Session getSession() {
        return sessionFactory.openSession();
    }

    public void closeSession(Session session) {
        session.clear();
        session.close();
    }

    /**
     * generates persistent classes set, for now hardcoded.
     * todo use reflection, or spring
     * @see <a href="http://stackoverflow.com/questions/1413190/hibernate-mapping-package">http://stackoverflow.com/questions/1413190/hibernate-mapping-package</a>
     * @return set of classes are persistent
     */
    static public Set<Class<?>> getPersistentClasses(){
        //reflection way
//        Reflections reflections = new Reflections("your_package");
//        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(javax.persistence.Entity.class);

        //hardcoded way for now
        Set<Class<?>> classes = new HashSet<>();
        classes.add(Requirement.class);

        return classes;
    }

}
