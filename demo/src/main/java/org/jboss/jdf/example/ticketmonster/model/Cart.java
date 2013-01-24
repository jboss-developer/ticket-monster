package org.jboss.jdf.example.ticketmonster.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

/**
 * <p>
 *     A Cart contains tickets that the user has reserved for purchase in this session.
 * </p>
 *
 * @author Marius Bogoevici
 */
public class Cart implements Serializable  {

    private String id;

    private Performance performance;

    private ArrayList<SeatAllocation> seatAllocations = new ArrayList<SeatAllocation>();

    /**
     * Constructor for deserialization
     */
    private Cart() {
    }

    private Cart(String id) {
        this.id = id;
    }

    public static Cart initialize() {
        return new Cart(UUID.randomUUID().toString());
    }

    public String getId() {
        return id;
    }

    public Performance getPerformance() {
        return performance;
    }

    public void setPerformance(Performance performance) {
        this.performance = performance;
    }

    public ArrayList<SeatAllocation> getSeatAllocations() {
        return seatAllocations;
    }
}
