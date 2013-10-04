
angular.module('ticketmonster').controller('NewEventController', function ($scope, $location, locationParser, EventResource , MediaItemResource, EventCategoryResource) {
    $scope.disabled = false;
    $scope.$location = $location;
    $scope.event = $scope.event || {};
    
    $scope.mediaItemList = MediaItemResource.queryAll(function(items){
        $scope.mediaItemSelectionList = $.map(items, function(item) {
            return ( {
                value : item.id,
                text : item.url
            });
        });
    });
    $scope.$watch("mediaItemSelection", function(selection) {
        if ( typeof selection != 'undefined') {
            $scope.event.mediaItem = {};
            $scope.event.mediaItem.id = selection.value;
        }
    });
    
    $scope.categoryList = EventCategoryResource.queryAll(function(items){
        $scope.categorySelectionList = $.map(items, function(item) {
            return ( {
                value : item.id,
                text : item.description
            });
        });
    });
    $scope.$watch("categorySelection", function(selection) {
        if ( typeof selection != 'undefined') {
            $scope.event.category = {};
            $scope.event.category.id = selection.value;
        }
    });
    

    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            $location.path('/Events/edit/' + id);
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError = true;
        };
        EventResource.save($scope.event, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("/Events");
    };
});