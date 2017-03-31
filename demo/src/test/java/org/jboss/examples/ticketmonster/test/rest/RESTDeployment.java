package org.jboss.examples.ticketmonster.test.rest;

import org.jboss.examples.ticketmonster.model.Booking;
import org.jboss.examples.ticketmonster.rest.BaseEntityService;
import org.jboss.examples.ticketmonster.rest.dto.VenueDTO;
import org.jboss.examples.ticketmonster.service.*;
import org.jboss.examples.ticketmonster.test.TicketMonsterDeployment;
import org.jboss.examples.ticketmonster.util.Reflections;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public class RESTDeployment {

    public static WebArchive deployment() {


        return TicketMonsterDeployment.deployment()
                .addPackage(Booking.class.getPackage())
                .addPackage(BaseEntityService.class.getPackage())
                .addPackage(VenueDTO.class.getPackage())
                .addPackage(Reflections.class.getPackage())
                .addPackage(SeatAllocationService.class.getPackage())
                .addClass(AllocatedSeats.class)
                .addClass(MediaPath.class)
                .addClass(MediaManager.class);
    }
    
}
