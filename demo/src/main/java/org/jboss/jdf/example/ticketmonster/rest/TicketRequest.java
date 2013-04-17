package org.jboss.jdf.example.ticketmonster.rest;

/**
 * <p>
 * A {@link org.jboss.jdf.example.ticketmonster.rest.BookingRequest} will contain multiple {@link TicketRequest}s.
 * </p>
 *
 * @author Marius Bogoevici
 * @author Pete Muir
 *
 */
public class TicketRequest {

    private long ticketPrice;

    private int quantity;

    public TicketRequest() {
        // Empty constructor
    }

    public TicketRequest(Long ticketPrice, int quantity) {
        this.ticketPrice = ticketPrice;
        this.quantity = quantity;
    }

    public long getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(long ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
