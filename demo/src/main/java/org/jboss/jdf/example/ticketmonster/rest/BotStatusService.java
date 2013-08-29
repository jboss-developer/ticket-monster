package org.jboss.jdf.example.ticketmonster.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.jdf.example.ticketmonster.service.BotService;

/**
 * A REST-like service for providing the current state of the Bot.
 * 
 * @author Vineet Reynolds
 * 
 */
@Path("/bot")
public class BotStatusService {

	@Inject
	private BotService botService;

	/**
	 * Produces a JSON representation of the state of the bot, containing the
	 * current state of the bot as well as a maximum of 50 messages logged by
	 * the Bot.
	 * 
	 * @return The JSON representation of the Bot
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBotStatus() {
		BotState state = botService.isBotActive() ? BotState.RUNNING
				: BotState.NOT_RUNNING;
		BotRepresentation representation = new BotRepresentation(state,
				botService.fetchLog());
		return Response.ok(representation).build();
	}

	/**
	 * Updates the state of the Bot with the provided state. This may trigger
	 * the bot to start itself, stop itself, or stop and delete all existing
	 * bookings.
	 * 
	 * @param updatedStatus
	 *            The new state of the Bot. Only the state property is
	 *            considered; any messages provided are ignored.
	 * @return An empty HTTP 201 response.
	 */
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateBotStatus(BotRepresentation updatedStatus) {
		if (updatedStatus.getBotState().equals(BotState.RUNNING)) {
			botService.start();
		} else if (updatedStatus.getBotState().equals(BotState.NOT_RUNNING)) {
			botService.stop();
		} else if (updatedStatus.getBotState().equals(BotState.RESET)) {
			botService.deleteAll();
		}
		return Response.noContent().build();
	}

}
