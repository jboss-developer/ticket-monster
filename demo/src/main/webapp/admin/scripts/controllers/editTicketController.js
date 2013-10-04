

angular.module('ticketmonster').controller('EditTicketController', function($scope, $routeParams, $location, TicketResource , TicketCategoryResource, SectionResource) {
    var self = this;
    $scope.disabled = false;
    $scope.$location = $location;
    
    $scope.get = function() {
        var successCallback = function(data){
            self.original = data;
            $scope.ticket = new TicketResource(self.original);
            TicketCategoryResource.queryAll(function(items) {
                $scope.ticketCategorySelectionList = $.map(items, function(item) {
                    var wrappedObject = {
                        id : item.id
                    };
                    var labelObject = {
                        value : item.id,
                        text : item.description
                    };
                    if($scope.ticket.ticketCategory && item.id == $scope.ticket.ticketCategory.id) {
                        $scope.ticketCategorySelection = labelObject;
                        $scope.ticket.ticketCategory = wrappedObject;
                        self.original.ticketCategory = $scope.ticket.ticketCategory;
                    }
                    return labelObject;
                });
            });
            SectionResource.queryAll(function(items) {
                $scope.seatsectionSelectionList = $.map(items, function(item) {
                    var wrappedObject = {
                        id : item.id
                    };
                    var labelObject = {
                        value : item.id,
                        text : item.name
                    };
                    if($scope.ticket.seat.section && item.id == $scope.ticket.seat.section.id) {
                        $scope.seatsectionSelection = labelObject;
                        $scope.ticket.seat.section = wrappedObject;
                        self.original.seat.section = $scope.ticket.seat.section;
                    }
                    return labelObject;
                });
            });
        };
        var errorCallback = function() {
            $location.path("/Tickets");
        };
        TicketResource.get({TicketId:$routeParams.TicketId}, successCallback, errorCallback);
    };

    $scope.isClean = function() {
        return angular.equals(self.original, $scope.ticket);
    };

    $scope.save = function() {
        var successCallback = function(){
            $scope.get();
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        };
        $scope.ticket.$update(successCallback, errorCallback);
    };

    $scope.cancel = function() {
        $location.path("/Tickets");
    };

    $scope.remove = function() {
        var successCallback = function() {
            $location.path("/Tickets");
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        }; 
        $scope.ticket.$remove(successCallback, errorCallback);
    };
    
    $scope.$watch("ticketCategorySelection", function(selection) {
        if (typeof selection != 'undefined') {
            $scope.ticket.ticketCategory = {};
            $scope.ticket.ticketCategory.id = selection.value;
        }
    });
    $scope.$watch("seatsectionSelection", function(selection) {
        if (typeof selection != 'undefined') {
            $scope.ticket.seat.section = {};
            $scope.ticket.seat.section.id = selection.value;
        }
    });
    
    $scope.get();
});