package org.jboss.jdf.example.ticketmonster.rest.search;

import java.util.logging.Logger;

import javax.ejb.Stateless;
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
import org.jboss.jdf.example.ticketmonster.model.Show;

/**
 * Service exposed as a REST endpoint and offering full-text search, geolocalized search
 * as well as faceting using Hibernate Search.
 * 
 * @author Emmanuel Bernard
 */
@Stateless
@Path("/search")
public class SearchService {
    @Inject
    EntityManager em;
    @Inject
    Logger logger;

    /**
     * REST endpoint for the search engine
     * 
     * @param searchString contains the words to search
     * @param latitude (optional) search restricted around latitude
     * @param longitude (optional) search restricted around longitude
     * @param categoryFacetId (optional) selected category facet
     * @param minPriceFacetId (optional) selected price facet
     * @return ShowResults containing the results and the faceting data
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ShowResults search(@QueryParam("query") String searchString, 
        @QueryParam("latitude") Double latitude, @QueryParam("longitude") Double longitude,
        @QueryParam("categoryfacet") Integer categoryFacetId, @QueryParam("minpricefacet") Integer minPriceFacetId) {
        FullTextEntityManager ftem = Search.getFullTextEntityManager(em);
        QueryBuilder qb = ftem.getSearchFactory().buildQueryBuilder().forEntity(Show.class).get();
        
        Query luceneQuery = buildLuceneQuery(searchString, latitude, longitude, qb);
        
        FullTextQuery objectQuery = ftem.createFullTextQuery(luceneQuery, Show.class);
        
        enableFaceting(qb, objectQuery);
        enableFacetRestriction(objectQuery, categoryFacetId, minPriceFacetId);
        
        objectQuery.setResultTransformer(ShowViewResultTransformer.INSTANCE);
        
        ShowResults results = buildResultObject(objectQuery);
        return results;
    }

    private Query buildLuceneQuery(String searchString, Double latitude, Double longitude, QueryBuilder qb) {
        Query luceneQuery;
        Query termsQuery;
        if (searchString.isEmpty()) {
            // Return all terms
            termsQuery = qb.all().createQuery();
        }
        else {
            // Find the terms of searchString with terms in event.name (weight of 10),
            // event.description (weight of 1) and venue.name (weight of 3)
             termsQuery = qb.keyword()
                .onField("event.name").boostedTo(10f)
                .andField("event.description")
                .andField("venue.name").boostedTo(5f)
                .matching(searchString)
                .createQuery();
        }
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
        return luceneQuery;
    }
    private ShowResults buildResultObject(FullTextQuery objectQuery) {
        ShowResults results = new ShowResults(objectQuery.getResultList());
        FacetManager fm = objectQuery.getFacetManager();
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

    private static String[] PRICE_FACET_VALUES = new String[] {"below $50", "$50 to $100", "$100 to $200", "above $200"};

    private void enableFaceting(QueryBuilder qb, FullTextQuery objectQuery) {
        FacetingRequest categoryFaceting = qb.facet()
            .name("category")
            .onField("event.category.description")
            .discrete()
                .includeZeroCounts(true)
                .orderedBy(FacetSortOrder.FIELD_VALUE)
            .createFacetingRequest();
        FacetingRequest priceFaceting = qb.facet()
            .name("price")
            .onField("ticketPrices.min")
            .range()
                .below(50f).excludeLimit()
                .from(50f).to(100f).excludeLimit()
                .from(100f).to(200f).excludeLimit()
                .above(200f)
                .includeZeroCounts(true)
                .orderedBy(FacetSortOrder.RANGE_DEFINITION_ORDER)
            .createFacetingRequest();
        objectQuery.getFacetManager().enableFaceting(categoryFaceting).enableFaceting(priceFaceting);
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

}