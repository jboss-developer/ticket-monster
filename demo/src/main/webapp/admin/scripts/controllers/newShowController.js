
angular.module('ticketmonster').controller('NewShowController', function ($scope, $location, locationParser, ShowResource , EventResource, PerformanceResource, VenueResource, TicketPriceResource) {
    $scope.disabled = false;
    $scope.$location = $location;
    $scope.show = $scope.show || {};
    
    $scope.eventList = EventResource.queryAll(function(items){
        $scope.eventSelectionList = $.map(items, function(item) {
            return ( {
                value : item.id,
                text : item.name
            });
        });
    });
    $scope.$watch("eventSelection", function(selection) {
        if ( typeof selection != 'undefined') {
            $scope.show.event = {};
            $scope.show.event.id = selection.value;
        }
    });
    
    $scope.performancesList = PerformanceResource.queryAll(function(items){
        $scope.performancesSelectionList = $.map(items, function(item) {
            return ( {
                value : item.id,
                text : item.displayTitle
            });
        });
    });
    $scope.$watch("performancesSelection", function(selection) {
        if (typeof selection != 'undefined') {
            $scope.show.performances = [];
            $.each(selection, function(idx,selectedItem) {
                var collectionItem = {};
                collectionItem.id = selectedItem.value;
                $scope.show.performances.push(collectionItem);
            });
        }
    });
    
    $scope.venueList = VenueResource.queryAll(function(items){
        $scope.venueSelectionList = $.map(items, function(item) {
            return ( {
                value : item.id,
                text : item.name
            });
        });
    });
    $scope.$watch("venueSelection", function(selection) {
        if ( typeof selection != 'undefined') {
            $scope.show.venue = {};
            $scope.show.venue.id = selection.value;
        }
    });
    
    $scope.ticketPricesList = TicketPriceResource.queryAll(function(items){
        $scope.ticketPricesSelectionList = $.map(items, function(item) {
            return ( {
                value : item.id,
                text : item.price
            });
        });
    });
    $scope.$watch("ticketPricesSelection", function(selection) {
        if (typeof selection != 'undefined') {
            $scope.show.ticketPrices = [];
            $.each(selection, function(idx,selectedItem) {
                var collectionItem = {};
                collectionItem.id = selectedItem.value;
                $scope.show.ticketPrices.push(collectionItem);
            });
        }
    });
    

    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            $location.path('/Shows/edit/' + id);
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError = true;
        };
        ShowResource.save($scope.show, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("/Shows");
    };
});
