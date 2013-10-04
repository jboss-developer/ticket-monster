
angular.module('ticketmonster').controller('NewEventCategoryController', function ($scope, $location, locationParser, EventCategoryResource ) {
    $scope.disabled = false;
    $scope.$location = $location;
    $scope.eventCategory = $scope.eventCategory || {};
    

    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            $location.path('/EventCategorys/edit/' + id);
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError = true;
        };
        EventCategoryResource.save($scope.eventCategory, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("/EventCategorys");
    };
});