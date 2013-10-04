

angular.module('ticketmonster').controller('EditEventCategoryController', function($scope, $routeParams, $location, EventCategoryResource ) {
    var self = this;
    $scope.disabled = false;
    $scope.$location = $location;
    
    $scope.get = function() {
        var successCallback = function(data){
            self.original = data;
            $scope.eventCategory = new EventCategoryResource(self.original);
        };
        var errorCallback = function() {
            $location.path("/EventCategorys");
        };
        EventCategoryResource.get({EventCategoryId:$routeParams.EventCategoryId}, successCallback, errorCallback);
    };

    $scope.isClean = function() {
        return angular.equals(self.original, $scope.eventCategory);
    };

    $scope.save = function() {
        var successCallback = function(){
            $scope.get();
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        };
        $scope.eventCategory.$update(successCallback, errorCallback);
    };

    $scope.cancel = function() {
        $location.path("/EventCategorys");
    };

    $scope.remove = function() {
        var successCallback = function() {
            $location.path("/EventCategorys");
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        }; 
        $scope.eventCategory.$remove(successCallback, errorCallback);
    };
    
    
    $scope.get();
});