
angular.module('ticketmonster').controller('NewPerformanceController', function ($scope, $location, locationParser, PerformanceResource , ShowResource) {
    $scope.disabled = false;
    $scope.$location = $location;
    $scope.performance = $scope.performance || {};
    
    $scope.showList = ShowResource.queryAll(function(items){
        $scope.showSelectionList = $.map(items, function(item) {
            return ( {
                value : item.id,
                text : item.displayTitle
            });
        });
    });
    $scope.$watch("showSelection", function(selection) {
        if ( typeof selection != 'undefined') {
            $scope.performance.show = {};
            $scope.performance.show.id = selection.value;
        }
    });
    

    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            $location.path('/Performances/edit/' + id);
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError = true;
        };
        PerformanceResource.save($scope.performance, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("/Performances");
    };
});
