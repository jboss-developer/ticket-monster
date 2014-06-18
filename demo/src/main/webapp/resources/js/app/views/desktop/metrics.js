define([
    'backbone',
	'configuration',
    'utilities',
    'text!../../../../templates/desktop/metrics.html'
], function (
    Backbone,
	config,
    utilities,
    metricsTemplate) {

    var MetricsView = Backbone.View.extend({
        intervalDuration : 3000,
        initialize : function() {
            _.bind(this.render, this);
            _.bind(this.liveUpdate, this);
            this.collection.on("add remove change", this.render, this);
            var self = this;
            $.when(this.collection.fetch({
                error : function() {
                    utilities.displayAlert("Failed to retrieve metrics from the TicketMonster server.");
                }
            })).done(function(){
                self.liveUpdate();
            });
        },
        liveUpdate : function() {
            this.collection.fetch({
                error : function() {
                    utilities.displayAlert("Failed to retrieve metrics from the TicketMonster server.");
                }
            });
            var self = this;
            this.timerObject = setTimeout(function(){
                self.liveUpdate();
            }, this.intervalDuration);
        },
        render : function () {
            utilities.applyTemplate($(this.el), metricsTemplate, {collection:this.collection});
            return this;
        },
        onClose : function() {
            if(this.timerObject) {
                clearTimeout(this.timerObject);
                delete this.timerObject;
            }
        }
    });

    return MetricsView;
});
