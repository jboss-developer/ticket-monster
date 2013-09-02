define([
    'jquery',
    'underscore',
    'backbone',
	'configuration',
    'utilities',
    'text!../../../../templates/desktop/bot.html'
], function (
    $,
    _,
    Backbone,
	config,
    utilities,
    botTemplate) {

    var BotView = Backbone.View.extend({
        intervalDuration : 3000,
        initialize : function() {
            _.bind(this.liveUpdate, this);
            _.bind(this.startBot, this);
            _.bind(this.stopBot, this);
            _.bind(this.resetBot, this);
            utilities.applyTemplate($(this.el), botTemplate, {});
            this.liveUpdate();
        },
        events: {
            "click #start-bot" : "startBot",
            "click #stop-bot" : "stopBot",
            "click #reset" : "resetBot"
        },
        liveUpdate : function() {
            this.model.fetchMessages(this.renderMessages);
            var self = this;
            this.timerObject = setTimeout(function() {
                self.liveUpdate();
            }, this.intervalDuration);
        },
        renderMessages : function(data) {
            var displayMessages = data.reverse();
            var botLog = $("textarea").get(0);
            // The botLog textarea element may have been removed if the user navigated to a different view
            if(botLog) {
                botLog.value = displayMessages.join("");
            }
        },
        onClose : function() {
            if(this.timerObject) {
                clearTimeout(this.timerObject);
                delete this.timerObject;
            }
        },
        startBot : function() {
            this.model.start();
            // Refresh the log immediately without waiting for the live update to trigger.
            this.model.fetchMessages(this.renderMessages);
        },
        stopBot : function() {
            this.model.stop();
            // Refresh the log immediately without waiting for the live update to trigger.
            this.model.fetchMessages(this.renderMessages);
        },
        resetBot : function() {
            this.model.reset();
            // Refresh the log immediately without waiting for the live update to trigger.
            this.model.fetchMessages(this.renderMessages);
        }
    });

    return BotView;
});