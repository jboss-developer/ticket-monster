
angular.module('ticketmonster').controller('NewMediaItemController', function ($scope, $location, locationParser, MediaItemResource ) {
    $scope.disabled = false;
    $scope.$location = $location;
    $scope.mediaItem = $scope.mediaItem || {};
    
    $scope.mediaTypeList = [
        "IMAGE"
    ];
    

    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            $location.path('/MediaItems/edit/' + id);
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError = true;
        };
        MediaItemResource.save($scope.mediaItem, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("/MediaItems");
    };
});