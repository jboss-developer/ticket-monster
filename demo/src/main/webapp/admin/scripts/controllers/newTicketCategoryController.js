
angular.module('ticketmonster').controller('NewTicketCategoryController', function ($scope, $location, locationParser, TicketCategoryResource ) {
    $scope.disabled = false;
    $scope.$location = $location;
    $scope.ticketCategory = $scope.ticketCategory || {};
    

    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            $location.path('/TicketCategorys/edit/' + id);
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError = true;
        };
        TicketCategoryResource.save($scope.ticketCategory, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("/TicketCategorys");
    };
});