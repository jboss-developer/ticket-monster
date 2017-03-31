package org.jboss.examples.ticketmonster.test.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.examples.ticketmonster.model.Venue;
import org.jboss.examples.ticketmonster.rest.VenueService;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class VenueServiceTest {
    
    @Deployment
    public static WebArchive deployment() {
        return RESTDeployment.deployment();
    }
   
    @Inject
    private VenueService venueService;
    
    @Test
    public void testGetVenueById() {
        
        // Test loading a single venue
        Venue venue = venueService.getSingleInstance(1l);
        assertNotNull(venue);
        assertEquals("Roy Thomson Hall", venue.getName());
    }
    
    @Test
    public void testPagination() {
        
        // Test pagination logic
        MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<String, String>();

        queryParameters.add("first", "2");
        queryParameters.add("maxResults", "1");
        
        List<Venue> venues = venueService.getAll(queryParameters);
        assertNotNull(venues);
        assertEquals(1, venues.size());
        assertEquals("Sydney Opera House", venues.get(0).getName());
    }

}
