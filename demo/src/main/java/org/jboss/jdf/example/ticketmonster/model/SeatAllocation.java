package org.jboss.jdf.example.ticketmonster.model;

import java.util.ArrayList;

import org.jboss.jdf.example.ticketmonster.rest.TicketRequest;

/**
 *
 * <p>
 * Represents the Seats that are allocated to a particular {@link TicketRequest}. Utility class
 * used by a {@link Cart}.
 * </p>
 *
 * @author Marius Bogoevici
 */
public class SeatAllocation {

    private TicketRequest ticketRequest;

    private ArrayList<Seat> allocatedSeats;

    public SeatAllocation(TicketRequest ticketRequest, ArrayList<Seat> allocatedSeats) {
        this.ticketRequest = ticketRequest;
        this.allocatedSeats = allocatedSeats;
    }

    public TicketRequest getTicketRequest() {
        return ticketRequest;
    }

    public ArrayList<Seat> getAllocatedSeats() {
        return allocatedSeats;
    }
}
