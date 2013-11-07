package org.jboss.jdf.ticketmonster.test.rest;

import org.jboss.jdf.example.ticketmonster.model.Booking;
import org.jboss.jdf.example.ticketmonster.model.search.PriceMinBridge;
import org.jboss.jdf.example.ticketmonster.rest.BaseEntityService;
import org.jboss.jdf.example.ticketmonster.rest.CartService;
import org.jboss.jdf.example.ticketmonster.rest.dto.VenueDTO;
import org.jboss.jdf.example.ticketmonster.rest.search.SearchService;
import org.jboss.jdf.example.ticketmonster.service.*;
import org.jboss.jdf.example.ticketmonster.util.MultivaluedHashMap;
import org.jboss.jdf.ticketmonster.test.TicketMonsterDeployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

public class RESTDeployment {

    public static WebArchive deployment() {


        return TicketMonsterDeployment.deployment()
                .addPackage(Booking.class.getPackage())
                .addPackage(BaseEntityService.class.getPackage())
                .addPackage(VenueDTO.class.getPackage())
                .addPackage(SearchService.class.getPackage())
                .addPackage(PriceMinBridge.class.getPackage())
                .addPackage(MultivaluedHashMap.class.getPackage())
                .addClass(CartStore.class)
                .addClass(CartService.class)
                .addClass(SectionAllocationKey.class)
                .addClass(SeatAllocationService.class)
                .addClass(AllocatedSeats.class)
                .addClass(MediaPath.class)
                .addClass(MediaManager.class)
                .addClass(Bootstrap.class)
                .addClass(BotService.class)
                .addClass(Bot.class)
                .addAsLibraries(Maven.resolver()
                        .loadPomFromFile("pom.xml")
                        .resolve("org.infinispan:infinispan-core").withTransitivity().asFile());
    }
    
}
