/**
 * Module for the query results model
 */
define([ 
    'configuration',
    'backbone'
], function (config) {
    /**
     * The Results model class definition
     */
    var Results = Backbone.Model.extend({
        urlRoot: config.baseUrl + 'rest/search', // the URL for performing CRUD operations
        url: function() {
			return this.urlRoot + '?query=' + encodeURIComponent(this.get("query"));
	    }
    });
    // export the Results class
    return Results;
});