package org.jboss.jdf.example.ticketmonster.util;

import java.util.logging.Logger;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.picketlink.annotations.PicketLink;

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

    /**
     * Alias the persistence context
     */
    // use @SuppressWarnings to tell IDE to ignore warnings about field not being referenced directly
   @SuppressWarnings("unused")
   @Produces
   @PersistenceContext
   private EntityManager em;

    /**
     * <p>
     * Produces a {@link EntityManager} using the qualifier {@link PicketLink} that will be used by the PicketLink IDM.
     * </p>
     */
    @Produces
    @PicketLink
    @PersistenceContext(unitName = "primary")
    private EntityManager picketLinkEntityManager;
   
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
