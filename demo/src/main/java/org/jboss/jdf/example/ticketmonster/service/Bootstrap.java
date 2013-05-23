package org.jboss.jdf.example.ticketmonster.service;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.jboss.jdf.example.ticketmonster.util.ForSearch;


@Singleton
@Startup
public class Bootstrap {
    @Inject @ForSearch private FullTextEntityManager ftem;
    @Inject private Logger logger; 

    @PostConstruct
    public void onStartup() {
        try {
        	ftem.createIndexer().purgeAllOnStart(true).startAndWait();
        } catch (InterruptedException e) {
            logger.severe("Unable to index data with Hibernate Search");
        }
    }
}
