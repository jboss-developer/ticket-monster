/**
 * Module for the Bot model
 */
define([
    // Configuration is a dependency
    'configuration',
    'backbone'
], function (config, Backbone) {

    /**
     * The Bot model class definition
     * Used for CRUD operations against a Bot resource
     */
    var Bot = Backbone.Model.extend({
        url: config.baseUrl + 'rest/bot',
        sync: function(method, model, options) {
            if(method ==='create')
            {
                method = 'update';
            }
            Backbone.sync.call(this,method,model,options);
        }
    });

    return Bot;

});