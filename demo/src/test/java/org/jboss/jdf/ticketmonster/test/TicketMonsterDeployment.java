package org.jboss.jdf.ticketmonster.test;

import org.jboss.jdf.example.ticketmonster.util.Resources;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public class TicketMonsterDeployment {

    public static WebArchive deployment() {
        return ShrinkWrap
                .create(WebArchive.class, "test.war")
                .addPackage(Resources.class.getPackage())
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("import.sql")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                // Deploy our test datasource
                .addAsWebInfResource("test-ds.xml")
                .addAsWebInfResource("jboss-deployment-structure.xml");
    }
}
