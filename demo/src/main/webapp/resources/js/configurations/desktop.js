/**
 * Shortcut alias definitions - will come in handy when declaring dependencies
 * Also, they allow you to keep the code free of any knowledge about library
 * locations and versions
 */
requirejs.config({
    baseUrl: "resources/js",
    paths: {
        jquery:'libs/jquery-1.9.1',
        underscore:'libs/underscore',
        text:'libs/text',
        order:'libs/order',
        bootstrap: 'libs/bootstrap',
        backbone: 'libs/backbone',
        utilities: 'app/utilities',
        router:'app/router/desktop/router'
    },
    // We shim Backbone since it doesn't declare an AMD module
    shim: {
        'backbone': {
            deps: ['jquery', 'underscore'],
            exports: 'Backbone'
        }
    }
});

define("initializer", ["jquery"],
    function ($) {
    $('head').append('<link type="text/css" rel="stylesheet" href="resources/css/screen.css"/>');
    $('head').append('<link rel="stylesheet" href="resources/css/bootstrap.css" type="text/css" media="all"/>');
    $('head').append('<link rel="stylesheet" href="resources/css/custom.css" type="text/css" media="all">');
    $('head').append('<link href="http://fonts.googleapis.com/css?family=Rokkitt" rel="stylesheet" type="text/css">');
});

// Now we declare all the dependencies
require([
    'order!initializer',
    'order!router'
], function(){
});

define("configuration", {
    baseUrl : ""
});