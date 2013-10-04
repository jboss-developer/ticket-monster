
angular.module('ticketmonster').controller('NewSectionController', function ($scope, $location, locationParser, SectionResource , VenueResource) {
    $scope.disabled = false;
    $scope.$location = $location;
    $scope.section = $scope.section || {};
    
    $scope.venueList = VenueResource.queryAll(function(items){
        $scope.venueSelectionList = $.map(items, function(item) {
            return ( {
                value : item.id,
                text : item.name
            });
        });
    });
    $scope.$watch("venueSelection", function(selection) {
        if ( typeof selection != 'undefined') {
            $scope.section.venue = {};
            $scope.section.venue.id = selection.value;
        }
    });
    

    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            $location.path('/Sections/edit/' + id);
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError = true;
        };
        SectionResource.save($scope.section, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("/Sections");
    };
});