package org.jboss.jdf.example.ticketmonster.util;

import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;

/**
 * This class uses CDI to alias Java EE resources, such as the persistence context, to CDI beans
 * 
 * <p>
 * Example injection on a managed bean field:
 * </p>
 * 
 * <pre>
 * &#064;Inject
 * private EntityManager em;
 * </pre>
 */
public class Resources {

   @Produces
   @PersistenceContext
   private EntityManager em;
   
   /**
    * Produces Hibernate Search EntityManager
    */
   @Produces @ForSearch @Dependent
   public FullTextEntityManager getFullTextEntityManager(EntityManager em) {
      return Search.getFullTextEntityManager(em); 
   }
   
   /**
    * Provider injectable loggers based around Java Util Logging.
    * @param injectionPoint
    * @return
    */
   @Produces
   public Logger produceLog(InjectionPoint injectionPoint) {
      return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
   }
 
   @Produces @DataDir
   public String getDataDir() {
       String openshiftDataDir = System.getenv("OPENSHIFT_DATA_DIR");
       if (openshiftDataDir != null) {
           return openshiftDataDir;
       } else {
           return "";
       }
   }
   
}
