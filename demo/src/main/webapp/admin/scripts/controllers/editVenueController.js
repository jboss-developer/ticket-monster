

angular.module('ticketmonster').controller('EditVenueController', function($scope, $routeParams, $location, VenueResource , MediaItemResource, SectionResource) {
    var self = this;
    $scope.disabled = false;
    $scope.$location = $location;
    
    $scope.get = function() {
        var successCallback = function(data){
            self.original = data;
            $scope.venue = new VenueResource(self.original);
            MediaItemResource.queryAll(function(items) {
                $scope.mediaItemSelectionList = $.map(items, function(item) {
                    var wrappedObject = {
                        id : item.id
                    };
                    var labelObject = {
                        value : item.id,
                        text : item.url
                    };
                    if($scope.venue.mediaItem && item.id == $scope.venue.mediaItem.id) {
                        $scope.mediaItemSelection = labelObject;
                        $scope.venue.mediaItem = wrappedObject;
                        self.original.mediaItem = $scope.venue.mediaItem;
                    }
                    return labelObject;
                });
            });
            SectionResource.queryAll(function(items) {
                $scope.sectionsSelectionList = $.map(items, function(item) {
                    var wrappedObject = {
                        id : item.id
                    };
                    var labelObject = {
                        value : item.id,
                        text : item.name
                    };
                    if($scope.venue.sections){
                        $.each($scope.venue.sections, function(idx, element) {
                            if(item.id == element.id) {
                                $scope.sectionsSelection.push(labelObject);
                                $scope.venue.sections.push(wrappedObject);
                            }
                        });
                        self.original.sections = $scope.venue.sections;
                    }
                    return labelObject;
                });
            });
        };
        var errorCallback = function() {
            $location.path("/Venues");
        };
        VenueResource.get({VenueId:$routeParams.VenueId}, successCallback, errorCallback);
    };

    $scope.isClean = function() {
        return angular.equals(self.original, $scope.venue);
    };

    $scope.save = function() {
        var successCallback = function(){
            $scope.get();
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        };
        $scope.venue.$update(successCallback, errorCallback);
    };

    $scope.cancel = function() {
        $location.path("/Venues");
    };

    $scope.remove = function() {
        var successCallback = function() {
            $location.path("/Venues");
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        }; 
        $scope.venue.$remove(successCallback, errorCallback);
    };
    
    $scope.$watch("mediaItemSelection", function(selection) {
        if (typeof selection != 'undefined') {
            $scope.venue.mediaItem = {};
            $scope.venue.mediaItem.id = selection.value;
        }
    });
    $scope.sectionsSelection = $scope.sectionsSelection || [];
    $scope.$watch("sectionsSelection", function(selection) {
        if (typeof selection != 'undefined' && $scope.venue) {
            $scope.venue.sections = [];
            $.each(selection, function(idx,selectedItem) {
                var collectionItem = {};
                collectionItem.id = selectedItem.value;
                $scope.venue.sections.push(collectionItem);
            });
        }
    });
    
    $scope.get();
});