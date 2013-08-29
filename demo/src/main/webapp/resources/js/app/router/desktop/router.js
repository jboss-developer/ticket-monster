/**
 * A module for the router of the desktop application
 */
define("router", [
    'jquery',
    'underscore',
    'configuration',
    'utilities',
    'app/models/booking',
    'app/models/event',
    'app/models/venue',
    'app/collections/bookings',
    'app/collections/events',
    'app/collections/venues',
    'app/views/desktop/home',
    'app/views/desktop/events',
    'app/views/desktop/venues',
    'app/views/desktop/create-booking',
    'app/views/desktop/bookings',
    'app/views/desktop/event-detail',
    'app/views/desktop/venue-detail',
    'app/views/desktop/booking-detail',
    'app/views/desktop/monitor',
    'text!../templates/desktop/main.html'
],function ($,
            _,
            config,
            utilities,
            Booking,
            Event,
            Venue,
            Bookings,
            Events,
            Venues,
            HomeView,
            EventsView,
            VenuesView,
            CreateBookingView,
            BookingsView,
            EventDetailView,
            VenueDetailView,
            BookingDetailView,
            MonitorView,
            MainTemplate) {

    $(document).ready(new function() {
       utilities.applyTemplate($('body'), MainTemplate)
    })

    /**
     * The Router class contains all the routes within the application - 
     * i.e. URLs and the actions that will be taken as a result.
     *
     * @type {Router}
     */

    var Router = Backbone.Router.extend({
        initialize: function() {
            //Begin dispatching routes
            Backbone.history.start();
        },
        routes:{
            "":"home",
            "about":"home",
            "events":"events",
            "events/:id":"eventDetail",
            "venues":"venues",
            "venues/:id":"venueDetail",
            "book/:showId/:performanceId":"bookTickets",
            "bookings":"listBookings",
            "bookings/:id":"bookingDetail",
            "monitor":"displayMonitor",
            "ignore":"ignore",
            "*actions":"defaultHandler"
        },
        events:function () {
            var events = new Events();
            var eventsView = new EventsView({model:events, el:$("#content")});
            events.on("reset",
                function () {
                    utilities.viewManager.showView(eventsView);
                }).fetch({reset:true});
        },
        venues:function () {
            var venues = new Venues;
            var venuesView = new VenuesView({model:venues, el:$("#content")});
            venues.on("reset",
                function () {
                    utilities.viewManager.showView(venuesView);
                }).fetch({reset:true});
        },
        home:function () {
            utilities.viewManager.showView(new HomeView({el:$("#content")}));
        },
        bookTickets:function (showId, performanceId) {
            var createBookingView = 
            	new CreateBookingView({
            		model:{ showId:showId, 
            			    performanceId:performanceId, 
            			    bookingRequest:{seatAllocations:[]}},
            			    el:$("#content")
            			   });
            utilities.viewManager.showView(createBookingView);
        },
        listBookings:function () {
            $.get(
                config.baseUrl + "rest/bookings/count",
                function (data) {
                    var bookings = new Bookings;
                    var bookingsView = new BookingsView({
                        model:{bookings: bookings},
                        el:$("#content"),
                        pageSize: 10,
                        page: 1,
                        count:data.count});

                    bookings.on("destroy",
                        function () {
                            bookingsView.refreshPage();
                        });
                    bookings.fetch({reset:true,
                    	data:{first:1, maxResults:10},
                        processData:true, success:function () {
                            utilities.viewManager.showView(bookingsView);
                        }});
                }
            );

        },
        displayMonitor:function() {
            var monitorView = new MonitorView({el:$("#content")});
            utilities.viewManager.showView(monitorView);
        },
        eventDetail:function (id) {
            var model = new Event({id:id});
            var eventDetailView = new EventDetailView({model:model, el:$("#content")});
            model.on("change",
                function () {
                    utilities.viewManager.showView(eventDetailView);
                }).fetch();
        },
        venueDetail:function (id) {
            var model = new Venue({id:id});
            var venueDetailView = new VenueDetailView({model:model, el:$("#content")});
            model.on("change",
                function () {
                    utilities.viewManager.showView(venueDetailView);
                }).fetch();
        },
        bookingDetail:function (id) {
            var bookingModel = new Booking({id:id});
            var bookingDetailView = new BookingDetailView({model:bookingModel, el:$("#content")});
            bookingModel.on("change",
                function () {
                    utilities.viewManager.showView(bookingDetailView);
                }).fetch();

        }
    });

    // Create a router instance
    var router = new Router();

    return router;
});