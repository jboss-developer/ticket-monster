

angular.module('ticketmonster').controller('EditShowController', function($scope, $routeParams, $location, ShowResource , EventResource, PerformanceResource, VenueResource, TicketPriceResource) {
    var self = this;
    $scope.disabled = false;
    $scope.$location = $location;
    
    $scope.get = function() {
        var successCallback = function(data){
            self.original = data;
            $scope.show = new ShowResource(self.original);
            EventResource.queryAll(function(items) {
                $scope.eventSelectionList = $.map(items, function(item) {
                    var wrappedObject = {
                        id : item.id
                    };
                    var labelObject = {
                        value : item.id,
                        text : item.name
                    };
                    if($scope.show.event && item.id == $scope.show.event.id) {
                        $scope.eventSelection = labelObject;
                        $scope.show.event = wrappedObject;
                        self.original.event = $scope.show.event;
                    }
                    return labelObject;
                });
            });
            PerformanceResource.queryAll(function(items) {
                $scope.performancesSelectionList = $.map(items, function(item) {
                    var wrappedObject = {
                        id : item.id
                    };
                    var labelObject = {
                        value : item.id,
                        text : item.displayTitle
                    };
                    if($scope.show.performances){
                        $.each($scope.show.performances, function(idx, element) {
                            if(item.id == element.id) {
                                $scope.performancesSelection.push(labelObject);
                                $scope.show.performances.push(wrappedObject);
                            }
                        });
                        self.original.performances = $scope.show.performances;
                    }
                    return labelObject;
                });
            });
            VenueResource.queryAll(function(items) {
                $scope.venueSelectionList = $.map(items, function(item) {
                    var wrappedObject = {
                        id : item.id
                    };
                    var labelObject = {
                        value : item.id,
                        text : item.name
                    };
                    if($scope.show.venue && item.id == $scope.show.venue.id) {
                        $scope.venueSelection = labelObject;
                        $scope.show.venue = wrappedObject;
                        self.original.venue = $scope.show.venue;
                    }
                    return labelObject;
                });
            });
            TicketPriceResource.queryAll(function(items) {
                $scope.ticketPricesSelectionList = $.map(items, function(item) {
                    var wrappedObject = {
                        id : item.id
                    };
                    var labelObject = {
                        value : item.id,
                        text : item.price
                    };
                    if($scope.show.ticketPrices){
                        $.each($scope.show.ticketPrices, function(idx, element) {
                            if(item.id == element.id) {
                                $scope.ticketPricesSelection.push(labelObject);
                                $scope.show.ticketPrices.push(wrappedObject);
                            }
                        });
                        self.original.ticketPrices = $scope.show.ticketPrices;
                    }
                    return labelObject;
                });
            });
        };
        var errorCallback = function() {
            $location.path("/Shows");
        };
        ShowResource.get({ShowId:$routeParams.ShowId}, successCallback, errorCallback);
    };

    $scope.isClean = function() {
        return angular.equals(self.original, $scope.show);
    };

    $scope.save = function() {
        var successCallback = function(){
            $scope.get();
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        };
        $scope.show.$update(successCallback, errorCallback);
    };

    $scope.cancel = function() {
        $location.path("/Shows");
    };

    $scope.remove = function() {
        var successCallback = function() {
            $location.path("/Shows");
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        }; 
        $scope.show.$remove(successCallback, errorCallback);
    };
    
    $scope.$watch("eventSelection", function(selection) {
        if (typeof selection != 'undefined') {
            $scope.show.event = {};
            $scope.show.event.id = selection.value;
        }
    });
    $scope.performancesSelection = $scope.performancesSelection || [];
    $scope.$watch("performancesSelection", function(selection) {
        if (typeof selection != 'undefined' && $scope.show) {
            $scope.show.performances = [];
            $.each(selection, function(idx,selectedItem) {
                var collectionItem = {};
                collectionItem.id = selectedItem.value;
                $scope.show.performances.push(collectionItem);
            });
        }
    });
    $scope.$watch("venueSelection", function(selection) {
        if (typeof selection != 'undefined') {
            $scope.show.venue = {};
            $scope.show.venue.id = selection.value;
        }
    });
    $scope.ticketPricesSelection = $scope.ticketPricesSelection || [];
    $scope.$watch("ticketPricesSelection", function(selection) {
        if (typeof selection != 'undefined' && $scope.show) {
            $scope.show.ticketPrices = [];
            $.each(selection, function(idx,selectedItem) {
                var collectionItem = {};
                collectionItem.id = selectedItem.value;
                $scope.show.ticketPrices.push(collectionItem);
            });
        }
    });
    
    $scope.get();
});
