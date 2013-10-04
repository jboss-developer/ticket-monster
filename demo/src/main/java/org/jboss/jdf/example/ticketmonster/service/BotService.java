package org.jboss.jdf.example.ticketmonster.service;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Asynchronous;
import javax.ejb.Singleton;
import javax.ejb.Timer;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.jdf.example.ticketmonster.model.Booking;
import org.jboss.jdf.example.ticketmonster.rest.BookingService;
import org.jboss.jdf.example.ticketmonster.util.CircularBuffer;
import org.jboss.jdf.example.ticketmonster.util.MultivaluedHashMap;
import org.jboss.jdf.example.ticketmonster.util.qualifier.BotMessage;

/**
 * A Bot service that acts as a Facade for the Bot, providing methods to control the Bot state as well as to obtain the current
 * state of the Bot.
 * 
 * @author Christian Sadilek <csadilek@redhat.com>
 * @author Pete Muir
 * @author Vineet Reynolds
 */
@Singleton
public class BotService {

    private static final int MAX_LOG_SIZE = 50;

    private CircularBuffer<String> log;

    @Inject
    private Bot bot;

    @Inject
    private BookingService bookingService;

    @Inject
    private Logger logger;

    @Inject
    @BotMessage
    private Event<String> event;

    private Timer timer;

    public BotService() {
        log = new CircularBuffer<String>(MAX_LOG_SIZE);
    }

    public void start() {
        synchronized (bot) {
            if (timer == null) {
                logger.info("Starting bot");
                timer = bot.start();
            }
        }
    }

    public void stop() {
        synchronized (bot) {
            if (timer != null) {
                logger.info("Stopping bot");
                bot.stop(timer);
                timer = null;
            }
        }
    }

    @Asynchronous
    public void deleteAll() {
        synchronized (bot) {
            stop();
            for (Booking booking : bookingService.getAll(MultivaluedHashMap
                .<String, String> empty())) {
                bookingService.deleteBooking(booking.getId());
                event.fire("Deleted booking " + booking.getId() + " for "
                    + booking.getContactEmail() + "\n");
            }
        }
    }

    public void newBookingRequest(@Observes @BotMessage String bookingRequest) {
        log.add(bookingRequest);
    }

    public List<String> fetchLog() {
        return log.getContents();
    }

    public boolean isBotActive() {
        return (timer != null);
    }

}