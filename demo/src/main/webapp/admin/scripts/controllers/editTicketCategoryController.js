

angular.module('ticketmonster').controller('EditTicketCategoryController', function($scope, $routeParams, $location, TicketCategoryResource ) {
    var self = this;
    $scope.disabled = false;
    $scope.$location = $location;
    
    $scope.get = function() {
        var successCallback = function(data){
            self.original = data;
            $scope.ticketCategory = new TicketCategoryResource(self.original);
        };
        var errorCallback = function() {
            $location.path("/TicketCategorys");
        };
        TicketCategoryResource.get({TicketCategoryId:$routeParams.TicketCategoryId}, successCallback, errorCallback);
    };

    $scope.isClean = function() {
        return angular.equals(self.original, $scope.ticketCategory);
    };

    $scope.save = function() {
        var successCallback = function(){
            $scope.get();
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        };
        $scope.ticketCategory.$update(successCallback, errorCallback);
    };

    $scope.cancel = function() {
        $location.path("/TicketCategorys");
    };

    $scope.remove = function() {
        var successCallback = function() {
            $location.path("/TicketCategorys");
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        }; 
        $scope.ticketCategory.$remove(successCallback, errorCallback);
    };
    
    
    $scope.get();
});