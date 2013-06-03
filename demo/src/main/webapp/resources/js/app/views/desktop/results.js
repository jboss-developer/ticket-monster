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
            "click .faceting": "enableFaceting"
        },
        
        initialize:function () {
            this.model.bind('change', this.render, this);
        },
        
        render:function () {
            $(this.el).empty();
            this.delegateEvents();
            utilities.applyTemplate($(this.el), resultsTemplate, {model:this.model, query:this.model.get("query")});
            return this;	
        },
        
        enableFaceting:function (e) {
            var id = $(e.currentTarget).data("id");
            var faceting = id.substring(0, id.lastIndexOf('-'));
            var index = id.substring(id.lastIndexOf('-') + 1);
            if (index == 'all') {
                this.model.unset(faceting);
            }
            else {
                this.model.set(faceting, index);
            }
            this.model.fetch();
            return false;
        }
    });
    return ResultsView;
});