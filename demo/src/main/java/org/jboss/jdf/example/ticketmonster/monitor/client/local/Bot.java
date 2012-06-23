package org.jboss.jdf.example.ticketmonster.monitor.client.local;

import java.util.List;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.errai.bus.client.api.RemoteCallback;
import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.jboss.errai.ioc.client.api.Caller;
import org.jboss.errai.ioc.client.api.EntryPoint;
import org.jboss.jdf.example.ticketmonster.model.Booking;
import org.jboss.jdf.example.ticketmonster.monitor.client.shared.BookingMonitorService;
import org.jboss.jdf.example.ticketmonster.monitor.client.shared.BotService;
import org.jboss.jdf.example.ticketmonster.monitor.client.shared.qualifier.BotMessage;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The entry point into the TicketMonster bot.
 *
 * The {@code @EntryPoint} annotation indicates to the Errai framework that this class should be instantiated inside the web
 * browser when the web page is first loaded.
 *
 * @author Christian Sadilek <csadilek@redhat.com>
 * @author Pete Muir
 */
@EntryPoint
public class Bot {

    /**
     * Maximum length of the log, we will truncate beyond this value
     */
    private static final int MAX_LOG_LENGTH = 8000;

    /**
     * This bots log.
     */
    private static TextArea log;

    /**
     * This is the client-side proxy to the {@link BookingMonitorService}. The proxy is generated at build time, and injected
     * into this field when the page loads.
     */
    @Inject
    private Caller<BotService> botService;

    /**
     * This method constructs the UI.
     *
     * Methods annotated with Errai's {@link AfterInitialization} are only called once everything is up and running, including
     * the communication channel to the server.
     */
    @AfterInitialization
    public void createAndShowUI() {
        // Update the bot log
        botService.call(new RemoteCallback<List<String>>() {
            @Override
            public void callback(List<String> log) {
                Bot.this.createUi();
                for (String line : log) {
                    updateLog(line);
                }
            }
        }).fetchLog();
    }

    protected void createUi() {
        HorizontalPanel controls = new HorizontalPanel();

        Button start = new Button("Start bot");
        Button stop = new Button("Stop bot");
        Button deleteAll = new Button("Delete all bookings");

        start.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                botService.call(new RemoteCallback<Void>() {

                    @Override
                    public void callback(Void response) {

                    }
                }).start();
            }
        });

        start.setStyleName("btn", true);
        start.setStyleName("btn-danger",true);
        start.setTitle("Start the bot");

        stop.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                botService.call(new RemoteCallback<Void>() {

                    @Override
                    public void callback(Void response) {

                    }
                }).stop();
            }
        });


        stop.setStyleName("btn", true);
        stop.setStyleName("btn-danger", true);
        deleteAll.setTitle("Stop the bot");

        deleteAll.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                botService.call(new RemoteCallback<Void>() {

                    @Override
                    public void callback(Void response) {

                    }
                }).deleteAll();
            }
        });

        deleteAll.setStyleName("btn", true);
        deleteAll.setStyleName("btn-danger", true);
        deleteAll.setTitle("Delete all bookings (stops the bot first)");

        controls.add(start);
        controls.add(stop);
        controls.add(deleteAll);

        controls.setStyleName("btn-group");

        VerticalPanel console = new VerticalPanel();

        console.setStyleName("bot-console");

        log = new TextArea();
        log.setWidth("400px");
        log.setHeight("300px");
        log.setReadOnly(true);

        Label botLabel = new Label("Bot Log");
        botLabel.setStyleName("bot-label");
        console.add(botLabel);
        console.add(log);

        Panel root = new VerticalPanel();
        root.add(controls);
        root.add(console);
        RootPanel.get("bot-content").add(root);
    }

    private void updateLog(String append) {
        String orig = log.getText();
        String newLogContent = append + orig;
        if (newLogContent.length() > MAX_LOG_LENGTH) {
            newLogContent = newLogContent.substring(0, MAX_LOG_LENGTH);
            newLogContent = newLogContent.substring(0, newLogContent.lastIndexOf("\n"));
        }
        Bot.log.setText(newLogContent);
    }

    /**
     * Responds to the CDI event that's fired on the server when a {@link Booking} is created.
     *
     * @param booking the create booking
     */
    public void onLogUpdated(@Observes @BotMessage String append) {
        updateLog(append);
    }

}