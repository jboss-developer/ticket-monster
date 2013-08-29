define([
    'jquery',
    'underscore',
    'backbone',
	'configuration',
    'utilities',
    'app/models/bot',
    'text!../../../../templates/desktop/bot.html'
], function (
    $,
    _,
    Backbone,
	config,
    utilities,
    Bot,
    botTemplate) {

    var BotView = Backbone.View.extend({
        intervalDuration : 1000,
        initialize : function() {
            _.bind(this.render, this);
            _.bind(this.liveUpdate, this);
            _.bind(this.startBot, this);
            _.bind(this.stopBot, this);
            _.bind(this.resetBot, this);
            this.model.on("change", this.render, this);
            utilities.applyTemplate($(this.el), botTemplate, {model:this.model});
            var self = this;
            $.when(this.model.fetch())
                .done(function(){
                    self.liveUpdate();
                });
        },
        events: {
            "click #start-bot" : "startBot",
            "click #stop-bot" : "stopBot",
            "click #reset" : "resetBot"
        },
        liveUpdate : function() {
            this.model.fetch();
            var self = this;
            this.timerObject = setTimeout(function(){
                self.liveUpdate();
            }, this.intervalDuration);
        },
        render : function () {
            var displayMessages = this.model.get("messages").reverse();
            $("textarea").get(0).value = displayMessages.join("");
            return this;
        },
        onClose : function() {
            if(this.timerObject) {
                clearTimeout(this.timerObject);
                delete this.timerObject;
            }
        },
        startBot : function() {
            var updatedBot = new Bot();
            updatedBot.save({botState:"RUNNING"});
        },
        stopBot : function() {
            var updatedBot = new Bot();
            updatedBot.save({botState:"NOT_RUNNING"});
        },
        resetBot : function() {
            var updatedBot = new Bot();
            updatedBot.save({botState:"RESET"});
        }
    });

    return BotView;
});