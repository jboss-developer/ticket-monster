package org.jboss.jdf.ticketmonster.test.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.core.MultivaluedMap;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.jdf.example.ticketmonster.model.Booking;
import org.jboss.jdf.example.ticketmonster.model.Performance;
import org.jboss.jdf.example.ticketmonster.model.Show;
import org.jboss.jdf.example.ticketmonster.model.Ticket;
import org.jboss.jdf.example.ticketmonster.model.TicketPrice;
import org.jboss.jdf.example.ticketmonster.rest.*;
import org.jboss.jdf.example.ticketmonster.util.MultivaluedHashMap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class BookingServiceTest {

    @Deployment
    public static WebArchive deployment() {
        return RESTDeployment.deployment();
    }

    @Inject
    private BookingService bookingService;

    @Inject
    private ShowService showService;

    @Test
    @InSequence(1)
    public void testCreateBookings() {
        BookingRequest br = createBookingRequest(1l, 0, 0, 1, 3);
        bookingService.createBooking(br);

        BookingRequest br2 = createBookingRequest(2l, 1, 2, 4, 9);
        bookingService.createBooking(br2);

        BookingRequest br3 = createBookingRequest(3l, 0, 0, 1);
        bookingService.createBooking(br3);
    }
    
    @Test
    @InSequence(10)
    public void testGetBookings() {
        checkBooking1();
        checkBooking2();
        checkBooking3();
    }
    
    private void checkBooking1() {
        Booking booking = bookingService.getSingleInstance(1l);
        assertNotNull(booking);
        assertEquals("Roy Thomson Hall", booking.getPerformance().getShow().getVenue().getName());
        assertEquals("Rock concert of the decade", booking.getPerformance().getShow().getEvent().getName());
        assertEquals("bob@acme.com", booking.getContactEmail());

        // Test the ticket requests created

        assertEquals(3 + 2 + 1, booking.getTickets().size());

        List<String> requiredTickets = new ArrayList<String>();
        requiredTickets.add("A @ 219.5 (Adult)");
        requiredTickets.add("A @ 219.5 (Adult)");
        requiredTickets.add("D @ 149.5 (Adult)");
        requiredTickets.add("C @ 179.5 (Adult)");
        requiredTickets.add("C @ 179.5 (Adult)");
        requiredTickets.add("C @ 179.5 (Adult)");

        checkTickets(requiredTickets, booking);
    }
    
    private void checkBooking2() {
        Booking booking = bookingService.getSingleInstance(2l);
        assertNotNull(booking);
        assertEquals("Sydney Opera House", booking.getPerformance().getShow().getVenue().getName());
        assertEquals("Rock concert of the decade", booking.getPerformance().getShow().getEvent().getName());
        assertEquals("bob@acme.com", booking.getContactEmail());

        assertEquals(3 + 2 + 1, booking.getTickets().size());

        List<String> requiredTickets = new ArrayList<String>();
        requiredTickets.add("S2 @ 197.75 (Adult)");
        requiredTickets.add("S6 @ 145.0 (Child 0-14yrs)");
        requiredTickets.add("S6 @ 145.0 (Child 0-14yrs)");
        requiredTickets.add("S4 @ 145.0 (Child 0-14yrs)");
        requiredTickets.add("S6 @ 145.0 (Child 0-14yrs)");
        requiredTickets.add("S4 @ 145.0 (Child 0-14yrs)");

        checkTickets(requiredTickets, booking);
    }
    
    private void checkBooking3() {
        Booking booking = bookingService.getSingleInstance(3l);
        assertNotNull(booking);
        assertEquals("Roy Thomson Hall", booking.getPerformance().getShow().getVenue().getName());
        assertEquals("Shane's Sock Puppets", booking.getPerformance().getShow().getEvent().getName());
        assertEquals("bob@acme.com", booking.getContactEmail());

        assertEquals(2 + 1, booking.getTickets().size());

        List<String> requiredTickets = new ArrayList<String>();
        requiredTickets.add("B @ 199.5 (Adult)");
        requiredTickets.add("D @ 149.5 (Adult)");
        requiredTickets.add("B @ 199.5 (Adult)");
        
        checkTickets(requiredTickets, booking);
    }

    @Test
    @InSequence(10)
    public void testPagination() {

        // Test pagination logic
        MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<java.lang.String, java.lang.String>();

        queryParameters.add("first", "2");
        queryParameters.add("maxResults", "1");

        List<Booking> bookings = bookingService.getAll(queryParameters);
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals("Sydney Opera House", bookings.get(0).getPerformance().getShow().getVenue().getName());
        assertEquals("Rock concert of the decade", bookings.get(0).getPerformance().getShow().getEvent().getName());
    }

    @Test
    @InSequence(20)
    public void testDelete() {
        bookingService.deleteBooking(2l);
        checkBooking1();
        checkBooking3();
        try {
            bookingService.getSingleInstance(2l);
        } catch (Exception e) {
            if (e.getCause() instanceof NoResultException) {
                return;
            }
        }
        fail("Expected NoResultException did not occur.");
    }

    private BookingRequest createBookingRequest(Long showId, int performanceNo, int... ticketPriceNos) {
        Show show = showService.getSingleInstance(showId);

        Performance performance = new ArrayList<Performance>(show.getPerformances()).get(performanceNo);

        BookingRequest bookingRequest = new BookingRequest(performance, "bob@acme.com");

        List<TicketPrice> possibleTicketPrices = new ArrayList<TicketPrice>(show.getTicketPrices());
        int i = 1;
        for (int index : ticketPriceNos) {
            bookingRequest.addTicketRequest(new TicketReservationRequest(possibleTicketPrices.get(index).getId(), i));
            i++;
        }

        return bookingRequest;
    }
    
    private void checkTickets(List<String> requiredTickets, Booking booking) {
        List<String> bookedTickets = new ArrayList<String>();
        for (Ticket t : booking.getTickets()) {
            bookedTickets.add(new StringBuilder().append(t.getSeat().getSection()).append(" @ ").append(t.getPrice()).append(" (").append(t.getTicketCategory()).append(")").toString());
        }
        System.out.println(bookedTickets);
        for (String requiredTicket : requiredTickets) {
            Assert.assertTrue("Required ticket not present: " + requiredTicket, bookedTickets.contains(requiredTicket));
        }
    }

}
