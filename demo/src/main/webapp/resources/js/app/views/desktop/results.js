define([
    'utilities',
    'require',
    'text!../../../../templates/desktop/results.html',
    'configuration',
    'bootstrap'
], function (
    utilities,
    require,
    resultsTemplate,
    config,
    Bootstrap) {
	
	var ResultsView  = Backbone.View.extend({

        events:{
        },
        
        render:function () {
            $(this.el).empty();
            utilities.applyTemplate($(this.el), resultsTemplate, {model:this.model, query:this.model.get("query")});
            return this;	
        }
    });
    return ResultsView;
});