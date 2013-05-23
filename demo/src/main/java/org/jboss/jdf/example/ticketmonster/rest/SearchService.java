package org.jboss.jdf.example.ticketmonster.rest;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.jboss.jdf.example.ticketmonster.model.Event;
import org.jboss.jdf.example.ticketmonster.util.ForSearch;

@Stateless
@Path("/search")
public class SearchService {
	@Inject @ForSearch FullTextEntityManager ftem;
	@Inject Logger logger;
	
	private void log(String message) {
		logger.severe(message);
		System.err.println("*** " + message);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Event> search(@QueryParam("query") String searchString) {
		log("Entering search");
		if (searchString == null || searchString.length() == 0) {
			log("search string is empty or null");
			return Collections.EMPTY_LIST;
		}
		log("search string is " + searchString);
		QueryBuilder qb = ftem.getSearchFactory().buildQueryBuilder().forEntity(Event.class).get();
		Query luceneQuery = qb.keyword()
			.onField("name").boostedTo(2f)
			.andField("description")
			.matching(searchString)
			.createQuery();
		log("Executing lucene query " + luceneQuery.toString());
		FullTextQuery objectQuery = ftem.createFullTextQuery(luceneQuery, Event.class);
		return (List<Event>) objectQuery.getResultList();
	}
}
