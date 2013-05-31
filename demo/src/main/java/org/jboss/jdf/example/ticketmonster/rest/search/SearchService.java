package org.jboss.jdf.example.ticketmonster.rest.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.New;
import javax.inject.Inject;
import javax.persistence.EntityManager;
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
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.Unit;
import org.hibernate.search.query.engine.spi.FacetManager;
import org.hibernate.search.query.facet.Facet;
import org.hibernate.search.query.facet.FacetSortOrder;
import org.hibernate.search.query.facet.FacetingRequest;
import org.jboss.jdf.example.ticketmonster.model.Event;
import org.jboss.jdf.example.ticketmonster.model.Show;
import org.jboss.jdf.example.ticketmonster.util.ForSearch;

@Stateless
@Path("/search")
public class SearchService {
    @Inject
    EntityManager em;
    @Inject
    Logger logger;

    // @Inject @ForSearch FullTextEntityManager ftem;

    private FullTextEntityManager ftem() {
        return Search.getFullTextEntityManager(em);
        // return ftem;
    }

    private void log(String message) {
        logger.info(message);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ShowResults search(@QueryParam("query") String searchString, 
        @QueryParam("latitude") Double latitude, @QueryParam("longitude") Double longitude,
        @QueryParam("categoryfacet") Integer categoryFacetId, @QueryParam("minpricefacet") Integer minPriceFacetId) {
        log("Entering search");
        if (searchString == null || searchString.length() == 0) {
            log("search string is empty or null");
            throw new WebApplicationException(new RuntimeException("Query must have a QueryParam 'query'"),
                Response.Status.BAD_REQUEST);
        }
        log("search string is " + searchString);
        log("search latitude is " + latitude + " and longitude is " + longitude);
        QueryBuilder qb = ftem().getSearchFactory().buildQueryBuilder().forEntity(Show.class).get();
        
        Query luceneQuery = buildLuceneQuery(searchString, latitude, longitude, qb);
        
        FullTextQuery objectQuery = ftem().createFullTextQuery(luceneQuery, Show.class);
        
        enableFacets(qb, objectQuery);
        enableFacetRestriction(objectQuery, categoryFacetId, minPriceFacetId);
        
        objectQuery.setResultTransformer(ShowViewResultTransformer.INSTANCE);
        
        ShowResults results = buildResultObject(objectQuery);
        return results;
    }

    private void enableFacetRestriction(FullTextQuery objectQuery, Integer categoryFacetId, Integer minPriceFacetId) {
        FacetManager fm = objectQuery.getFacetManager();
        if (categoryFacetId != null) {
            Facet selectedFacet = fm.getFacets("category").get(categoryFacetId);
            fm.getFacetGroup("category").selectFacets(selectedFacet);
        }
        if (minPriceFacetId != null) {
            Facet selectedFacet = fm.getFacets("price").get(minPriceFacetId);
            fm.getFacetGroup("price").selectFacets(selectedFacet);
        }
    }

    private ShowResults buildResultObject(FullTextQuery objectQuery) {
        FacetManager fm = objectQuery.getFacetManager();
        ShowResults results = new ShowResults(objectQuery.getResultList());
        FacetGroupView facetGroup = new FacetGroupView("Category", fm, "category");
        results.addFacetGroup(facetGroup);
        facetGroup = new FacetGroupView("Starting price", fm, "price");
        for(int index = 0 ; index < facetGroup.getFacets().size() ; index++) {
            FacetView facet = facetGroup.getFacets().get(index);
            facet.overrideValue(PRICE_FACET_VALUES[index]);
        }
        results.addFacetGroup(facetGroup);
        return results;
    }

    private void enableFacets(QueryBuilder qb, FullTextQuery objectQuery) {
        FacetingRequest categoryFaceting = qb.facet()
            .name("category")
            .onField("event.category.description")
            .discrete()
            .createFacetingRequest();
        FacetingRequest priceFaceting = qb.facet()
            .name("price")
            .onField("ticketPrices.min")
            .range() //uncomment once HSEARCH-1338 is fixed
                .below(50f) //.excludeLimit()
                .from(50f).to(100f) //.excludeLimit()
                .from(100f).to(200f) //.excludeLimit()
                .above(200f)
                .includeZeroCounts(true)
                .orderedBy(FacetSortOrder.RANGE_DEFINITION_ODER) //fix typo once HSEARCH-1339 is fixed
            .createFacetingRequest();
        FacetManager facetManager = objectQuery.getFacetManager().enableFaceting(categoryFaceting).enableFaceting(priceFaceting);
    }
    
    private static String[] PRICE_FACET_VALUES = new String[] {"below $50", "$50 to $100", "$100 to $200", "above $200"};

    private Query buildLuceneQuery(String searchString, Double latitude, Double longitude, QueryBuilder qb) {
        Query luceneQuery;
        Query termsQuery = qb.keyword()
            .onField("event.name").boostedTo(10f)
            .andField("event.description")
            .andField("event.category.description").boostedTo(3f)
            .andField("venue.name").boostedTo(5f)
            .matching(searchString)
            .createQuery();
        if (latitude != null && longitude != null) {
            Query localQuery = qb.spatial()
                .onCoordinates("venue.address.coordinates")
                .within(50, Unit.KM)
                .ofLatitude(latitude).andLongitude(longitude)
                .createQuery();
            luceneQuery = qb.bool()
                .must(termsQuery)
                .must(localQuery)
                .createQuery();
        }
        else {
            luceneQuery = termsQuery;
        }
        log("Executing lucene query " + luceneQuery.toString());
        return luceneQuery;
    }
}