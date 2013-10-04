
angular.module('ticketmonster').controller('NewTicketController', function ($scope, $location, locationParser, TicketResource , TicketCategoryResource, SectionResource) {
    $scope.disabled = false;
    $scope.$location = $location;
    $scope.ticket = $scope.ticket || {};
    
    $scope.ticketCategoryList = TicketCategoryResource.queryAll(function(items){
        $scope.ticketCategorySelectionList = $.map(items, function(item) {
            return ( {
                value : item.id,
                text : item.description
            });
        });
    });
    $scope.$watch("ticketCategorySelection", function(selection) {
        if ( typeof selection != 'undefined') {
            $scope.ticket.ticketCategory = {};
            $scope.ticket.ticketCategory.id = selection.value;
        }
    });
    
    $scope.seatsectionList = SectionResource.queryAll(function(items){
        $scope.seatsectionSelectionList = $.map(items, function(item) {
            return ( {
                value : item.id,
                text : item.name
            });
        });
    });
    $scope.$watch("seatsectionSelection", function(selection) {
        if ( typeof selection != 'undefined') {
            $scope.ticket.seat.section = {};
            $scope.ticket.seat.section.id = selection.value;
        }
    });
    

    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            $location.path('/Tickets/edit/' + id);
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError = true;
        };
        TicketResource.save($scope.ticket, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("/Tickets");
    };
});