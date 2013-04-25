/*
 * Copyright 2011 JBoss, by Red Hat, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.jdf.example.ticketmonster.monitor.client.local;

import org.jboss.jdf.example.ticketmonster.model.Performance;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * A UI component to display the status of a {@link Performance}.
 *
 * @author Christian Sadilek <csadilek@redhat.com>
 */
public class PerformanceStatusWidget extends Composite {

    private Label bookingStatusLabel = new Label();

    private HorizontalPanel progressBar = new HorizontalPanel();
    private Label soldPercentLabel;

    private Performance performance;
    private long soldTickets;
    private int capacity;

    public PerformanceStatusWidget(Performance performance) {
        this.performance = performance;

        soldTickets = BookingMonitor.getOccupiedCountForPerformance(performance);
        capacity = performance.getShow().getVenue().getCapacity();

        setBookingStatus();
        setProgress();

        HorizontalPanel performancePanel = new HorizontalPanel();
        String date = DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_SHORT).format(performance.getDate());
        performancePanel.add(new Label(date));
        performancePanel.add(progressBar);
        performancePanel.add(bookingStatusLabel);
        performancePanel.setStyleName("performance-status");
        initWidget(performancePanel);
    }

    /**
     * Updates the booking status (progress bar and corresponding text) of the {@link Performance}
     * associated with this widget based on the number of sold tickets cached in {@link BookingMonitor}.
     */
    public void updateBookingStatus() {
        this.soldTickets = BookingMonitor.getOccupiedCountForPerformance(performance);
        setBookingStatus();
        setProgress();
    }

    private void setBookingStatus() {
        bookingStatusLabel.setText(soldTickets + " of " + capacity + " tickets booked");
    }

    private void setProgress() {
        int soldPercent = Math.round((soldTickets / (float) capacity) * 100);

        String colour;
        if (soldPercent >= 100) {
            colour = "progress-danger";
        } else if (soldPercent > 90) {
            colour = "progress-warning";
        } else {
            colour = "progress-success";
        }
        soldPercentLabel = new HTML("<div class=\"performance-status-progress progress " + colour + "\">\n" +
                "  <div class=\"bar\"\n" +
                "       style=\"width: " + soldPercent + "%;\"></div>\n" +
                "</div>");

        progressBar.clear();
        progressBar.add(soldPercentLabel);

    }
}