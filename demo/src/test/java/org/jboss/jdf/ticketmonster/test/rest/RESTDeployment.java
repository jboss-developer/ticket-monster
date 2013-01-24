package org.jboss.jdf.ticketmonster.test.rest;

import org.jboss.jdf.example.ticketmonster.model.Booking;
import org.jboss.jdf.example.ticketmonster.rest.BaseEntityService;
import org.jboss.jdf.example.ticketmonster.service.AllocatedSeats;
import org.jboss.jdf.example.ticketmonster.service.MediaManager;
import org.jboss.jdf.example.ticketmonster.service.MediaPath;
import org.jboss.jdf.example.ticketmonster.service.DatabaseSeatAllocationService;
import org.jboss.jdf.example.ticketmonster.util.MultivaluedHashMap;
import org.jboss.jdf.ticketmonster.test.TicketMonsterDeployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public class RESTDeployment {

    public static WebArchive deployment() {
        return TicketMonsterDeployment.deployment()
                .addPackage(Booking.class.getPackage())
                .addPackage(BaseEntityService.class.getPackage())
                .addPackage(MultivaluedHashMap.class.getPackage())
                .addClass(DatabaseSeatAllocationService.class)
                .addClass(AllocatedSeats.class)
                .addClass(MediaPath.class)
                .addClass(MediaManager.class);
    }
    
}
