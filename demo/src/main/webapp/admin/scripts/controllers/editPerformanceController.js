

angular.module('ticketmonster').controller('EditPerformanceController', function($scope, $routeParams, $location, PerformanceResource , ShowResource) {
    var self = this;
    $scope.disabled = false;
    $scope.$location = $location;
    
    $scope.get = function() {
        var successCallback = function(data){
            self.original = data;
            $scope.performance = new PerformanceResource(self.original);
            ShowResource.queryAll(function(items) {
                $scope.showSelectionList = $.map(items, function(item) {
                    var wrappedObject = {
                        id : item.id
                    };
                    var labelObject = {
                        value : item.id,
                        text : item.displayTitle
                    };
                    if($scope.performance.show && item.id == $scope.performance.show.id) {
                        $scope.showSelection = labelObject;
                        $scope.performance.show = wrappedObject;
                        self.original.show = $scope.performance.show;
                    }
                    return labelObject;
                });
            });
        };
        var errorCallback = function() {
            $location.path("/Performances");
        };
        PerformanceResource.get({PerformanceId:$routeParams.PerformanceId}, successCallback, errorCallback);
    };

    $scope.isClean = function() {
        return angular.equals(self.original, $scope.performance);
    };

    $scope.save = function() {
        var successCallback = function(){
            $scope.get();
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        };
        $scope.performance.$update(successCallback, errorCallback);
    };

    $scope.cancel = function() {
        $location.path("/Performances");
    };

    $scope.remove = function() {
        var successCallback = function() {
            $location.path("/Performances");
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        }; 
        $scope.performance.$remove(successCallback, errorCallback);
    };
    
    $scope.$watch("showSelection", function(selection) {
        if (typeof selection != 'undefined') {
            $scope.performance.show = {};
            $scope.performance.show.id = selection.value;
        }
    });
    
    $scope.get();
});
