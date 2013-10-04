

angular.module('ticketmonster').controller('EditSectionController', function($scope, $routeParams, $location, SectionResource , VenueResource) {
    var self = this;
    $scope.disabled = false;
    $scope.$location = $location;
    
    $scope.get = function() {
        var successCallback = function(data){
            self.original = data;
            $scope.section = new SectionResource(self.original);
            VenueResource.queryAll(function(items) {
                $scope.venueSelectionList = $.map(items, function(item) {
                    var wrappedObject = {
                        id : item.id
                    };
                    var labelObject = {
                        value : item.id,
                        text : item.name
                    };
                    if($scope.section.venue && item.id == $scope.section.venue.id) {
                        $scope.venueSelection = labelObject;
                        $scope.section.venue = wrappedObject;
                        self.original.venue = $scope.section.venue;
                    }
                    return labelObject;
                });
            });
        };
        var errorCallback = function() {
            $location.path("/Sections");
        };
        SectionResource.get({SectionId:$routeParams.SectionId}, successCallback, errorCallback);
    };

    $scope.isClean = function() {
        return angular.equals(self.original, $scope.section);
    };

    $scope.save = function() {
        var successCallback = function(){
            $scope.get();
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        };
        $scope.section.$update(successCallback, errorCallback);
    };

    $scope.cancel = function() {
        $location.path("/Sections");
    };

    $scope.remove = function() {
        var successCallback = function() {
            $location.path("/Sections");
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        }; 
        $scope.section.$remove(successCallback, errorCallback);
    };
    
    $scope.$watch("venueSelection", function(selection) {
        if (typeof selection != 'undefined') {
            $scope.section.venue = {};
            $scope.section.venue.id = selection.value;
        }
    });
    
    $scope.get();
});