package org.jboss.jdf.ticketmonster.test.rest;

import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedMap;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.jdf.example.ticketmonster.model.Event;
import org.jboss.jdf.example.ticketmonster.model.MediaType;
import org.jboss.jdf.example.ticketmonster.rest.EventService;
import org.jboss.jdf.example.ticketmonster.rest.MediaService;
import org.jboss.jdf.example.ticketmonster.service.MediaManager;
import org.jboss.jdf.example.ticketmonster.service.MediaPath;
import org.jboss.jdf.example.ticketmonster.util.MultivaluedHashMap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class EventServiceTest {
    
    @Deployment
    public static WebArchive deployment() {
        return RESTDeployment.deployment();
    }
   
    @Inject
    private EventService eventService;
    
    @Inject
    private MediaService mediaService;
    
    @Inject
    private MediaManager mediaManager;
    
    @Test
    public void testGetEventById() {
        
        // Test loading a single event
        Event event = eventService.getSingleInstance(1l);
        assertNotNull(event);
        assertEquals("Rock concert of the decade", event.getName());
        
    }
    
    @Test
    public void testGetEventMedia() {
        
        // Test loading a single event
        Event event = eventService.getSingleInstance(1l);
        assertNotNull(event);
        
        MediaPath path = mediaManager.getPath(event.getMediaItem());
        assertNotNull(path);
        assertEquals(MediaType.IMAGE, path.getMediaType());
        
        assertNotNull(mediaService.getMediaContent(event.getMediaItem().getId()));
    }
    
    @Test
    public void testPagination() {
        
        // Test pagination logic
        MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<String, String>();
        queryParameters.add("first", "2");
        queryParameters.add("maxResults", "1");
        
        List<Event> events = eventService.getAll(queryParameters);
        assertNotNull(events);
        assertEquals(1, events.size());
        assertEquals("Shane's Sock Puppets", events.get(0).getName());
    }
    
    @Test
    public void testGetEventsByCategory() {
        
        // Test getting events by venue
        MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<java.lang.String, java.lang.String>();
        
        queryParameters.add("category", "1");
        
        List<Event> events = eventService.getAll(queryParameters);
        assertNotNull(events);
        assertEquals(5, events.size());
        for (Event e: events) {
            assertEquals("Concert", e.getCategory().getDescription());
        }
    }

}
