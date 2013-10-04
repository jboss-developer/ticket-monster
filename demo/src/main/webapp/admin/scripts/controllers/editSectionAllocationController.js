

angular.module('ticketmonster').controller('EditSectionAllocationController', function($scope, $routeParams, $location, SectionAllocationResource , PerformanceResource, SectionResource) {
    var self = this;
    $scope.disabled = false;
    $scope.$location = $location;
    
    $scope.get = function() {
        var successCallback = function(data){
            self.original = data;
            $scope.sectionAllocation = new SectionAllocationResource(self.original);
            PerformanceResource.queryAll(function(items) {
                $scope.performanceSelectionList = $.map(items, function(item) {
                    var wrappedObject = {
                        id : item.id
                    };
                    var labelObject = {
                        value : item.id,
                        text : item.displayTitle
                    };
                    if($scope.sectionAllocation.performance && item.id == $scope.sectionAllocation.performance.id) {
                        $scope.performanceSelection = labelObject;
                        $scope.sectionAllocation.performance = wrappedObject;
                        self.original.performance = $scope.sectionAllocation.performance;
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
                    if($scope.sectionAllocation.section && item.id == $scope.sectionAllocation.section.id) {
                        $scope.sectionSelection = labelObject;
                        $scope.sectionAllocation.section = wrappedObject;
                        self.original.section = $scope.sectionAllocation.section;
                    }
                    return labelObject;
                });
            });
        };
        var errorCallback = function() {
            $location.path("/SectionAllocations");
        };
        SectionAllocationResource.get({SectionAllocationId:$routeParams.SectionAllocationId}, successCallback, errorCallback);
    };

    $scope.isClean = function() {
        return angular.equals(self.original, $scope.sectionAllocation);
    };

    $scope.save = function() {
        var successCallback = function(){
            $scope.get();
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        };
        $scope.sectionAllocation.$update(successCallback, errorCallback);
    };

    $scope.cancel = function() {
        $location.path("/SectionAllocations");
    };

    $scope.remove = function() {
        var successCallback = function() {
            $location.path("/SectionAllocations");
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        }; 
        $scope.sectionAllocation.$remove(successCallback, errorCallback);
    };
    
    $scope.$watch("performanceSelection", function(selection) {
        if (typeof selection != 'undefined') {
            $scope.sectionAllocation.performance = {};
            $scope.sectionAllocation.performance.id = selection.value;
        }
    });
    $scope.$watch("sectionSelection", function(selection) {
        if (typeof selection != 'undefined') {
            $scope.sectionAllocation.section = {};
            $scope.sectionAllocation.section.id = selection.value;
        }
    });
    
    $scope.get();
});
