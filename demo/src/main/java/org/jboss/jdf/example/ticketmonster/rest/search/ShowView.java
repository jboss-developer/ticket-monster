package org.jboss.jdf.example.ticketmonster.rest.search;

import org.jboss.jdf.example.ticketmonster.model.Show;

public class ShowView {
	private Long eventId;
	private String eventName;
	private String eventDescription;
	private String eventCategory;
	private String venueName;
	
	public ShowView(Show show) {
		this.eventId = show.getEvent().getId();
		this.eventName = show.getEvent().getName();
		this.eventDescription = show.getEvent().getDescription();
		this.eventCategory = show.getEvent().getCategory().getDescription();
		this.venueName = show.getVenue().getName();
	}

	public Long getEventId() {
		return eventId;
	}

	public String getEventName() {
		return eventName;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public String getEventCategory() {
		return eventCategory;
	}

	public String getVenueName() {
		return venueName;
	}
}
