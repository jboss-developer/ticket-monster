package org.jboss.jdf.example.ticketmonster.rest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.jdf.example.ticketmonster.model.Performance;

/**
 * <p>
 * A {@link BookingRequest} is populated with unmarshalled JSON data, and handed to 
 * {@link BookingService#createBooking(BookingRequest)}.
 * </p>
 * 
 * @author Marius Bogoevici
 * @author Pete Muir
 * 
 */
public class BookingRequest {

    private List<TicketReservationRequest> ticketRequests = new ArrayList<TicketReservationRequest>();
    private long performance;
    private String email;
    
    public BookingRequest() {
        // Empty constructor for JAXB
    }

    public BookingRequest(Performance performance, String email) {
        this.performance = performance.getId();
        this.email = email;
    }

    public List<TicketReservationRequest> getTicketRequests() {
        return ticketRequests;
    }

    public void setTicketRequests(List<TicketReservationRequest> ticketRequests) {
        this.ticketRequests = ticketRequests;
    }
    
    public BookingRequest addTicketRequest(TicketReservationRequest ticketRequest) {
        ticketRequests.add(ticketRequest);
        return this;
    }

    public long getPerformance() {
        return performance;
    }

    public void setPerformance(long performance) {

        this.performance = performance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Utility method - computes the unique price category ids in the request
     *
     * @return
     */
    Set<Long> getUniquePriceCategoryIds() {
        Set<Long> priceCategoryIds = new HashSet<Long>();
        for (TicketReservationRequest ticketRequest : getTicketRequests()) {
            if (priceCategoryIds.contains(ticketRequest.getTicketPrice())) {
                throw new RuntimeException("Duplicate price category id");
            }
            priceCategoryIds.add(ticketRequest.getTicketPrice());
        }
        return priceCategoryIds;
    }
}
