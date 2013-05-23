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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.jboss.jdf.example.ticketmonster.model.Event;
import org.jboss.jdf.example.ticketmonster.model.Show;
import org.jboss.jdf.example.ticketmonster.util.ForSearch;

@Stateless
@Path("/search")
public class SearchService {
	@Inject @ForSearch FullTextEntityManager ftem;
	@Inject Logger logger;
	
	private void log(String message) {
		logger.info(message);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Show> search(@QueryParam("query") String searchString) {
		log("Entering search");
		if (searchString == null || searchString.length() == 0) {
			log("search string is empty or null");
			throw new WebApplicationException(new RuntimeException("Query must have a QueryParam 'query'"), Response.Status.BAD_REQUEST);
		}
		log("search string is " + searchString);
		QueryBuilder qb = ftem.getSearchFactory().buildQueryBuilder().forEntity(Show.class).get();
		Query luceneQuery = qb.keyword()
			.onField("event.name").boostedTo(10f)
			.andField("event.description")
			.andField("event.category.description").boostedTo(3f)
			.andField("venue.name").boostedTo(5f)
			.matching(searchString)
			.createQuery();
		log("Executing lucene query " + luceneQuery.toString());
		FullTextQuery objectQuery = ftem.createFullTextQuery(luceneQuery, Show.class);
		return (List<Show>) objectQuery.getResultList();
	}
}