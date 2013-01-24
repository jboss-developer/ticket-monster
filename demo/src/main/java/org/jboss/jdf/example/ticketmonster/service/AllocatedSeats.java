package org.jboss.jdf.example.ticketmonster.service;

import java.util.ArrayList;
import java.util.List;

import org.jboss.jdf.example.ticketmonster.model.Seat;
import org.jboss.jdf.example.ticketmonster.model.SectionAllocation;

/**
 * A transient object which represents a collection of pre-allocated seats
 *
 * @author Marius Bogoevici
 */
public class AllocatedSeats {

    private final SectionAllocation sectionAllocation;

    private final ArrayList<Seat> seats;

    public AllocatedSeats(SectionAllocation sectionAllocation, ArrayList<Seat> seats) {
        this.sectionAllocation = sectionAllocation;
        this.seats = seats;
    }

    public SectionAllocation getSectionAllocation() {
        return sectionAllocation;
    }

    public ArrayList<Seat> getSeats() {
        return seats;
    }

    public void markOccupied() {
        sectionAllocation.markOccupied(seats);
    }
}
