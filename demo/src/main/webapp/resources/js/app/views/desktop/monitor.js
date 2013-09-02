define([
    'backbone',
	'configuration',
    'utilities',
    'app/models/bot',
    'app/collections/metrics',
    'app/views/desktop/bot',
    'app/views/desktop/metrics',
    'text!../../../../templates/desktop/monitor.html'
], function (
    Backbone,
	config,
    utilities,
    Bot,
    Metrics,
    BotView,
    MetricsView,
    monitorTemplate) {

    var MonitorView = Backbone.View.extend({
        render : function () {
            utilities.applyTemplate($(this.el), monitorTemplate, {});
            var metrics = new Metrics();
            this.metricsView = new MetricsView({collection:metrics, el:$("#metrics-view")});
            var bot = new Bot();
            this.botView = new BotView({model:bot,el:$("#bot-view")});
            return this;
        },
        onClose : function() {
            if(this.botView) {
                this.botView.close();
            }
            if(this.metricsView) {
                this.metricsView.close();
            }
        }
    });

    return MonitorView;
});