package org.jboss.jdf.example.ticketmonster.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.jdf.example.ticketmonster.model.Performance;
import org.jboss.jdf.example.ticketmonster.model.Show;

/**
 * Metric data for a Show. Contains the identifier for the Show to identify it,
 * in addition to the event name, the venue name and capacity, and the metric
 * data for the performances of the Show.
 * 
 * @author Vineet Reynolds
 * 
 */
class ShowMetric {

	private Long show;
	private String event;
	private String venue;
	private int capacity;
	private List<PerformanceMetric> performances;

	// Constructor to populate the instance with data
	public ShowMetric(Show show, Map<Long, Long> occupiedCounts) {
		this.show = show.getId();
		this.event = show.getEvent().getName();
		this.venue = show.getVenue().getName();
		this.capacity = show.getVenue().getCapacity();
		this.performances = convertFrom(show.getPerformances(), occupiedCounts);
	}

	private List<PerformanceMetric> convertFrom(Set<Performance> performances,
			Map<Long, Long> occupiedCounts) {
		List<PerformanceMetric> result = new ArrayList<PerformanceMetric>();
		for (Performance performance : performances) {
			Long occupiedCount = occupiedCounts.get(performance.getId());
			result.add(new PerformanceMetric(performance, occupiedCount));
		}
		return result;
	}

	// Getters for Jackson
	// NOTE: No setters and default constructors are defined since
	// deserialization is not required.

	public Long getShow() {
		return show;
	}

	public String getEvent() {
		return event;
	}

	public String getVenue() {
		return venue;
	}

	public int getCapacity() {
		return capacity;
	}

	public List<PerformanceMetric> getPerformances() {
		return performances;
	}
}
