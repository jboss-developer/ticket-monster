

angular.module('ticketmonster').controller('EditMediaItemController', function($scope, $routeParams, $location, MediaItemResource ) {
    var self = this;
    $scope.disabled = false;
    $scope.$location = $location;
    
    $scope.get = function() {
        var successCallback = function(data){
            self.original = data;
            $scope.mediaItem = new MediaItemResource(self.original);
        };
        var errorCallback = function() {
            $location.path("/MediaItems");
        };
        MediaItemResource.get({MediaItemId:$routeParams.MediaItemId}, successCallback, errorCallback);
    };

    $scope.isClean = function() {
        return angular.equals(self.original, $scope.mediaItem);
    };

    $scope.save = function() {
        var successCallback = function(){
            $scope.get();
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        };
        $scope.mediaItem.$update(successCallback, errorCallback);
    };

    $scope.cancel = function() {
        $location.path("/MediaItems");
    };

    $scope.remove = function() {
        var successCallback = function() {
            $location.path("/MediaItems");
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        }; 
        $scope.mediaItem.$remove(successCallback, errorCallback);
    };
    
    $scope.mediaTypeList = [
        "IMAGE"  
    ];
    
    $scope.get();
});