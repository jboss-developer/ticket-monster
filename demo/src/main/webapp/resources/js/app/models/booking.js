/**
 * Module for the Booking model
 */
define([
    // Configuration is a dependency
    'configuration',
    'backbone'
], function (config, Backbone) {

    /**
     * The Booking model class definition
     * Used for CRUD operations against individual bookings
     */
    var Booking = Backbone.Model.extend({
        urlRoot: config.baseUrl + 'rest/bookings'
    });

    return Booking;

});