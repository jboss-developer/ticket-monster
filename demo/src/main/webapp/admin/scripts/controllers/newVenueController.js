
angular.module('ticketmonster').controller('NewVenueController', function ($scope, $location, locationParser, VenueResource , MediaItemResource, SectionResource) {
    $scope.disabled = false;
    $scope.$location = $location;
    $scope.venue = $scope.venue || {};
    
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
            $scope.venue.mediaItem = {};
            $scope.venue.mediaItem.id = selection.value;
        }
    });
    
    $scope.sectionsList = SectionResource.queryAll(function(items){
        $scope.sectionsSelectionList = $.map(items, function(item) {
            return ( {
                value : item.id,
                text : item.name
            });
        });
    });
    $scope.$watch("sectionsSelection", function(selection) {
        if (typeof selection != 'undefined') {
            $scope.venue.sections = [];
            $.each(selection, function(idx,selectedItem) {
                var collectionItem = {};
                collectionItem.id = selectedItem.value;
                $scope.venue.sections.push(collectionItem);
            });
        }
    });
    

    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            $location.path('/Venues/edit/' + id);
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError = true;
        };
        VenueResource.save($scope.venue, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("/Venues");
    };
});