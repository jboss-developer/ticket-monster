package org.jboss.jdf.example.ticketmonster.rest;

import java.util.List;

/**
 * A class that is eventually used to represent the state of the Bot in a
 * REST-like service. It holds the current state of the bot as well as a
 * collection of messages produced by the bot for display by clients.
 * 
 * @author Vineet Reynolds
 * 
 */
public class BotRepresentation {

	private BotState state;
	private List<String> messages;

	public BotRepresentation() {
		// Default constructor for Jackson
	}

	// Constructor to populate the instance with the current system state
	public BotRepresentation(BotState state, List<String> messages) {
		this.state = state;
		this.messages = messages;
	}

	// Getters and setters for use by Jackson

	public BotState getBotState() {
		return state;
	}

	public void setBotState(BotState state) {
		this.state = state;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}
}
