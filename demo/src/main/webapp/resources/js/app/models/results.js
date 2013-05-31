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
        initialize  : function() {
            _.bindAll(this,"fetch");
        },
        url: function() {
            params = '?query=' + encodeURIComponent(this.get("query"));
            if (typeof this.get("lat") != 'undefined' && typeof this.get("lng") != 'undefined') {
                params = params + '&latitude=' + encodeURIComponent(this.get("lat")) + '&longitude=' + encodeURIComponent(this.get("lng"));
            }
            if (typeof this.get("category") != 'undefined') {
                params = params + '&categoryfacet=' + this.get("category");
            }
            if (typeof this.get("price") != 'undefined') {
                params = params + '&minpricefacet=' + this.get("price");
            }
            console.log("Calling REST endpoint " + this.urlRoot + params);
            return this.urlRoot + params;
        }
    });
    // export the Results class
    return Results;
});