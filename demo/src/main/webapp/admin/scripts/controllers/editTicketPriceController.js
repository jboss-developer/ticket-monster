

angular.module('ticketmonster').controller('EditTicketPriceController', function($scope, $routeParams, $location, TicketPriceResource , ShowResource, SectionResource, TicketCategoryResource) {
    var self = this;
    $scope.disabled = false;
    $scope.$location = $location;
    
    $scope.get = function() {
        var successCallback = function(data){
            self.original = data;
            $scope.ticketPrice = new TicketPriceResource(self.original);
            ShowResource.queryAll(function(items) {
                $scope.showSelectionList = $.map(items, function(item) {
                    var wrappedObject = {
                        id : item.id
                    };
                    var labelObject = {
                        value : item.id,
                        text : item.displayTitle
                    };
                    if($scope.ticketPrice.show && item.id == $scope.ticketPrice.show.id) {
                        $scope.showSelection = labelObject;
                        $scope.ticketPrice.show = wrappedObject;
                        self.original.show = $scope.ticketPrice.show;
                    }
                    return labelObject;
                });
            });
            SectionResource.queryAll(function(items) {
                $scope.sectionSelectionList = $.map(items, function(item) {
                    var wrappedObject = {
                        id : item.id
                    };
                    var labelObject = {
                        value : item.id,
                        text : item.name
                    };
                    if($scope.ticketPrice.section && item.id == $scope.ticketPrice.section.id) {
                        $scope.sectionSelection = labelObject;
                        $scope.ticketPrice.section = wrappedObject;
                        self.original.section = $scope.ticketPrice.section;
                    }
                    return labelObject;
                });
            });
            TicketCategoryResource.queryAll(function(items) {
                $scope.ticketCategorySelectionList = $.map(items, function(item) {
                    var wrappedObject = {
                        id : item.id
                    };
                    var labelObject = {
                        value : item.id,
                        text : item.description
                    };
                    if($scope.ticketPrice.ticketCategory && item.id == $scope.ticketPrice.ticketCategory.id) {
                        $scope.ticketCategorySelection = labelObject;
                        $scope.ticketPrice.ticketCategory = wrappedObject;
                        self.original.ticketCategory = $scope.ticketPrice.ticketCategory;
                    }
                    return labelObject;
                });
            });
        };
        var errorCallback = function() {
            $location.path("/TicketPrices");
        };
        TicketPriceResource.get({TicketPriceId:$routeParams.TicketPriceId}, successCallback, errorCallback);
    };

    $scope.isClean = function() {
        return angular.equals(self.original, $scope.ticketPrice);
    };

    $scope.save = function() {
        var successCallback = function(){
            $scope.get();
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        };
        $scope.ticketPrice.$update(successCallback, errorCallback);
    };

    $scope.cancel = function() {
        $location.path("/TicketPrices");
    };

    $scope.remove = function() {
        var successCallback = function() {
            $location.path("/TicketPrices");
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        }; 
        $scope.ticketPrice.$remove(successCallback, errorCallback);
    };
    
    $scope.$watch("showSelection", function(selection) {
        if (typeof selection != 'undefined') {
            $scope.ticketPrice.show = {};
            $scope.ticketPrice.show.id = selection.value;
            if($scope.showList) {
                var selectedShow = $.map($scope.showList, function(show) {
                    if(show.id === $scope.ticketPrice.show.id) {
                        return show;
                    }
                });
                if(selectedShow.length > 0) {
                    selectedShow = selectedShow[0];
                }
            }
            if($scope.sectionList && selectedShow) {
                var venueSectionList = $.map($scope.sectionList, function(section) {
                    if(selectedShow.venue.id === section.venue.id) {
                        return ( {
                            value : section.id,
                            text : section.name
                        });
                    }
                });
                $scope.sectionSelectionList = venueSectionList;
            }
        }
    });
    $scope.$watch("sectionSelection", function(selection) {
        if (typeof selection != 'undefined') {
            $scope.ticketPrice.section = {};
            $scope.ticketPrice.section.id = selection.value;
        }
    });
    $scope.$watch("ticketCategorySelection", function(selection) {
        if (typeof selection != 'undefined') {
            $scope.ticketPrice.ticketCategory = {};
            $scope.ticketPrice.ticketCategory.id = selection.value;
        }
    });
    
    $scope.get();
});
