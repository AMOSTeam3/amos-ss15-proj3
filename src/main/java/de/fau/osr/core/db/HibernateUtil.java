package de.fau.osr.core.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
		
		Configuration configuration = new Configuration().configure();
		StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        sessionFactory = configuration.buildSessionFactory(builder.build());
        
        logger.debug("Static call : getsession factory() end::");
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
	
}
