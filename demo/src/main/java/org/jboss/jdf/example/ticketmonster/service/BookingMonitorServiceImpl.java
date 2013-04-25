package org.jboss.jdf.example.ticketmonster.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.errai.bus.server.annotations.Service;
import org.jboss.jdf.example.ticketmonster.model.Show;
import org.jboss.jdf.example.ticketmonster.monitor.client.shared.BookingMonitorService;

/**
 * Implementation of {@link BookingMonitorService}.
 * 
 * Errai's @Service annotation exposes this service as an RPC endpoint.
 * 
 * @author Christian Sadilek <csadilek@redhat.com>
 */
@ApplicationScoped 
@Service
@SuppressWarnings("unchecked")
public class BookingMonitorServiceImpl implements BookingMonitorService {

    @Inject
    private EntityManager entityManager;

    @Override
    public List<Show> retrieveShows() {
        Query showQuery = entityManager.createQuery(
            "select DISTINCT s from Show s JOIN s.performances p WHERE p.date > current_timestamp");
        return showQuery.getResultList();
    }

    @Override
    public Map<Long, Long> retrieveOccupiedCounts() {
        Map <Long, Long> occupiedCounts = new HashMap<Long, Long>();
        
        Query occupiedCountsQuery = entityManager.createQuery("" +
            		"select b.performance.id, SIZE(b.tickets) from Booking b " +
            		"WHERE b.performance.date > current_timestamp GROUP BY b.performance.id");
        
        List<Object[]> results = occupiedCountsQuery.getResultList();
        for (Object[] result : results) {
            occupiedCounts.put((Long) result[0], ((Integer) result[1]).longValue()); 
        }
        
        return occupiedCounts;
    }
}