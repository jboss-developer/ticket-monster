package org.jboss.jdf.example.ticketmonster.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.jdf.example.ticketmonster.service.BotService;

/**
 * A non-RESTful service for providing the current state of the Bot. This service also allows the bot to be started, stopped or
 * the existing bookings to be deleted.
 * 
 * @author Vineet Reynolds
 * 
 */
@Path("/bot")
public class BotStatusService {

    @Inject
    private BotService botService;

    /**
     * Produces a JSON representation of the bot's log, containing a maximum of 50 messages logged by the Bot.
     * 
     * @return The JSON representation of the Bot's log
     */
    @Path("messages")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getMessages() {
        return botService.fetchLog();
    }

    /**
     * Produces a representation of the bot's current state. This is a string - "RUNNING" or "NOT_RUNNING" depending on whether
     * the bot is active.
     * 
     * @return The represntation of the Bot's current state.
     */
    @Path("status")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBotStatus() {
        BotState state = botService.isBotActive() ? BotState.RUNNING
            : BotState.NOT_RUNNING;
        return Response.ok(state).build();
    }

    /**
     * Updates the state of the Bot with the provided state. This may trigger the bot to start itself, stop itself, or stop and
     * delete all existing bookings.
     * 
     * @param updatedStatus The new state of the Bot. Only the state property is considered; any messages provided are ignored.
     * @return An empty HTTP 201 response.
     */
    @Path("status")
    @PUT
    public Response updateBotStatus(BotState updatedState) {
        if (updatedState.equals(BotState.RUNNING)) {
            botService.start();
        } else if (updatedState.equals(BotState.NOT_RUNNING)) {
            botService.stop();
        } else if (updatedState.equals(BotState.RESET)) {
            botService.deleteAll();
        }
        return Response.noContent().build();
    }

}
