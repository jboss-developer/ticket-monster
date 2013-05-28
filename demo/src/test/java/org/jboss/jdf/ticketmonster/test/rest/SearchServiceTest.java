package org.jboss.jdf.ticketmonster.test.rest;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.jdf.example.ticketmonster.rest.search.SearchService;
import org.jboss.jdf.example.ticketmonster.rest.search.ShowResults;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class SearchServiceTest {
    
    @Deployment
    public static WebArchive deployment() {
        return RESTDeployment.deployment();
    }
   
    @Inject
    private SearchService searchService;
    
    @Test
    public void testResults() {
        ShowResults results = searchService.search("decade", null, null);
        assertEquals(2, results.getResults().size());
        assertEquals("Rock concert of the decade", results.getResults().iterator().next().getEventName());
    }
    
    @Test
    public void testNoResults() {
        ShowResults results = searchService.search("notavalidone", null, null);
        assertEquals(0, results.getResults().size());
    }
}