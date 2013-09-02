package org.jboss.jdf.example.ticketmonster.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.jdf.example.ticketmonster.model.Show;

/**
 * A read-only REST resource that provides a collection of metrics for shows occuring in the future. Updates to metrics via
 * POST/PUT etc. are not allowed, since they are not meant to be computed by consumers.
 * 
 * @author Vineet Reynolds
 * 
 */
@Path("/metrics")
@Stateless
public class MetricsService {

    @Inject
    private EntityManager entityManager;

    /**
     * Retrieves a collection of metrics for Shows. Each metric in the collection contains
     * <ul>
     * <li>the show id,</li>
     * <li>the event name of the show,</li>
     * <li>the venue for the show,</li>
     * <li>the capacity for the venue</li>
     * <li>the performances for the show,
     * <ul>
     * <li>the timestamp for each performance,</li>
     * <li>the occupied count for each performance</li>
     * </ul>
     * </li>
     * </ul>
     * 
     * @return A JSON representation of metrics for shows.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ShowMetric> getMetrics() {
        return retrieveMetricsFromShows(retrieveShows(),
            retrieveOccupiedCounts());
    }

    private List<ShowMetric> retrieveMetricsFromShows(List<Show> shows,
        Map<Long, Long> occupiedCounts) {
        List<ShowMetric> metrics = new ArrayList<ShowMetric>();
        for (Show show : shows) {
            metrics.add(new ShowMetric(show, occupiedCounts));
        }
        return metrics;
    }

    private List<Show> retrieveShows() {
        TypedQuery<Show> showQuery = entityManager
            .createQuery("select DISTINCT s from Show s JOIN s.performances p WHERE p.date > current_timestamp", Show.class);
        return showQuery.getResultList();
    }

    private Map<Long, Long> retrieveOccupiedCounts() {
        Map<Long, Long> occupiedCounts = new HashMap<Long, Long>();

        Query occupiedCountsQuery = entityManager
            .createQuery("select b.performance.id, SIZE(b.tickets) from Booking b "
                + "WHERE b.performance.date > current_timestamp GROUP BY b.performance.id");

        List<Object[]> results = occupiedCountsQuery.getResultList();
        for (Object[] result : results) {
            occupiedCounts.put((Long) result[0],
                ((Integer) result[1]).longValue());
        }

        return occupiedCounts;
    }
}
