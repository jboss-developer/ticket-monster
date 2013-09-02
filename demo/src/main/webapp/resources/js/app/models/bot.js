/**
 * Module for the Bot model
 */
define([
    'jquery',
    'configuration',
], function ($, config) {

    /**
     * The Bot model class definition
     * Used perform operations on the Bot.
     * Note that this is not a Backbone model.
     */
    var Bot = function() {
        this.statusUrl = config.baseUrl + 'rest/bot/status';
        this.messagesUrl = config.baseUrl + 'rest/bot/messages';
    }

    /*
     * Start the Bot by sending a request to the Bot resource
     * with the new status of the Bot set to "RUNNING".
     */
    Bot.prototype.start = function() {
        $.ajax({
            type: "PUT",
            url: this.statusUrl,
            data: "\"RUNNING\"",
            dataType: "json",
            contentType: "application/json"
        });
    }

    /*
     * Stop the Bot by sending a request to the Bot resource
     * with the new status of the Bot set to "NOT_RUNNING".
     */
    Bot.prototype.stop = function() {
        $.ajax({
            type: "PUT",
            url: this.statusUrl,
            data: "\"NOT_RUNNING\"",
            dataType: "json",
            contentType: "application/json"
        });
    }

    /*
     * Stop the Bot and delete all bookings by sending a request to the Bot resource
     * with the new status of the Bot set to "RESET".
     */
    Bot.prototype.reset = function() {
        $.ajax({
            type: "PUT",
            url: this.statusUrl,
            data: "\"RESET\"",
            dataType: "json",
            contentType: "application/json"
        });
    }

    /*
     * Fetch the log messages of the Bot and invoke the callback.
     * The callback is provided with the log messages (an array of Strings).
     */
    Bot.prototype.fetchMessages = function(callback) {
        $.get(this.messagesUrl, function(data) {
            if(callback) {
                callback(data);
            }
        });
    }

    return Bot;

});