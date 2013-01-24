package org.jboss.jdf.example.ticketmonster.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.jboss.jdf.example.ticketmonster.model.Performance;
import org.jboss.jdf.example.ticketmonster.model.Show;
import org.jboss.jdf.example.ticketmonster.model.TicketPrice;
import org.jboss.jdf.example.ticketmonster.monitor.client.shared.qualifier.BotMessage;
import org.jboss.jdf.example.ticketmonster.rest.*;
import org.jboss.jdf.example.ticketmonster.util.MultivaluedHashMap;

@Stateless
public class Bot {
    
    private static final Random random = new Random(System.nanoTime());
    
    /** Frequency with which the bot will book **/
    public static final long DURATION = TimeUnit.SECONDS.toMillis(3);
    
    /** Maximum number of ticket requests that will be filed **/
    public static int MAX_TICKET_REQUESTS = 100;
    
    /** Maximum number of tickets per request **/
    public static int MAX_TICKETS_PER_REQUEST = 100;
    
    public static String [] BOOKERS = {"anne@acme.com", "george@acme.com", "william@acme.com", "victoria@acme.com", "edward@acme.com", "elizabeth@acme.com", "mary@acme.com", "charles@acme.com", "james@acme.com", "henry@acme.com", "richard@acme.com", "john@acme.com", "stephen@acme.com"}; 

    @Inject 
    private ShowService showService;
    
    @Inject
    private BookingService bookingService;
    
    @Inject @BotMessage
    Event<String> event;
    
    @Resource
    private TimerService timerService;
    
    public Timer start() {
        String startMessage = new StringBuilder("==========================\n")
                .append("Bot started at ").append(new Date().toString()).append("\n")
                .toString();
        event.fire(startMessage);
        return timerService.createIntervalTimer(0, DURATION, new TimerConfig(null, false));
    }
    
    public void stop(Timer timer) {
        String startMessage = new StringBuilder("==========================\n")
                .append("Bot stopped at ").append(new Date().toString()).append("\n")
                .toString();
        event.fire(startMessage);
        timer.cancel();
    }
    
    @Timeout
    public void book(Timer timer) {
        // Select a show at random
        Show show = selectAtRandom(showService.getAll(MultivaluedHashMap.<String, String>empty()));

        // Select a performance at random
        Performance performance = selectAtRandom(show.getPerformances());
        
        String requestor = selectAtRandom(BOOKERS);

        BookingRequest bookingRequest = new BookingRequest(performance, requestor);

        List<TicketPrice> possibleTicketPrices = new ArrayList<TicketPrice>(show.getTicketPrices());
        
        List<Integer> indicies = selectAtRandom(MAX_TICKET_REQUESTS < possibleTicketPrices.size() ? MAX_TICKET_REQUESTS : possibleTicketPrices.size());
        
        StringBuilder message = new StringBuilder("==========================\n")
        .append("Booking by ")
        .append(requestor)
        .append(" at ")
        .append(new Date().toString())
        .append("\n")
        .append(performance)
        .append("\n")
        .append("~~~~~~~~~~~~~~~~~~~~~~~~~\n");
        
        for (int index : indicies) {
            int no = random.nextInt(MAX_TICKETS_PER_REQUEST);
            TicketPrice price = possibleTicketPrices.get(index);  
            bookingRequest.addTicketRequest(new TicketReservationRequest(price.getId(), no));
            message
                .append(no)
                .append(" of ")
                .append(price.getSection())
                .append("\n");
            
        }
        Response response = bookingService.createBooking(bookingRequest);
        if(response.getStatus() == Response.Status.OK.getStatusCode()) {
            message.append("SUCCESSFUL\n")
                    .append("~~~~~~~~~~~~~~~~~~~~~~~~~\n");
        }
        else {
                message.append("FAILED:\n")
                        .append(((Map<String, Object>) response.getEntity()).get("errors"))
                        .append("~~~~~~~~~~~~~~~~~~~~~~~~~\n");
        }
        event.fire(message.toString());
    }
    
    
    
    private <T> T selectAtRandom(List<T> list) {
        int i = random.nextInt(list.size());
        return list.get(i);
    }
    
    private <T> T selectAtRandom(T[] array) {
        int i = random.nextInt(array.length);
        return array[i];
    }
    
    private <T> T selectAtRandom(Collection<T> collection) {
        int item = random.nextInt(collection.size());
        int i = 0;
        for(T obj : collection)
        {
            if (i == item)
                return obj;
            i++;
        }
        throw new IllegalStateException();
    }
    
    private List<Integer> selectAtRandom(int max) {
        List<Integer> indicies = new ArrayList<Integer>();
        for (int i = 0; i < max;) {
            int r = random.nextInt(max);
            if (!indicies.contains(r)) {
                indicies.add(r);
                i++;
            }
        }
        return indicies;
    }
}
