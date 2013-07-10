package org.jboss.jdf.example.ticketmonster.service;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.jboss.jdf.example.ticketmonster.util.ForSearch;


@Singleton
@Startup
public class Bootstrap {
    @Inject private EntityManager em;
    @Inject private Logger logger; 

    @PostConstruct
    public void onStartup() {
        try {
            logger.info("Indexing entities");
            FullTextEntityManager ftem = Search.getFullTextEntityManager(em);
            ftem.createIndexer().purgeAllOnStart(true).startAndWait();
        } catch (InterruptedException e) {
            logger.severe("Unable to index data with Hibernate Search");
        }
    }
}
