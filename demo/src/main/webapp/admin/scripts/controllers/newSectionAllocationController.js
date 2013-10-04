
angular.module('ticketmonster').controller('NewSectionAllocationController', function ($scope, $location, locationParser, SectionAllocationResource , PerformanceResource, SectionResource) {
    $scope.disabled = false;
    $scope.$location = $location;
    $scope.sectionAllocation = $scope.sectionAllocation || {};
    
    $scope.performanceList = PerformanceResource.queryAll(function(items){
        $scope.performanceSelectionList = $.map(items, function(item) {
            return ( {
                value : item.id,
                text : item.displayTitle
            });
        });
    });
    $scope.$watch("performanceSelection", function(selection) {
        if ( typeof selection != 'undefined') {
            $scope.sectionAllocation.performance = {};
            $scope.sectionAllocation.performance.id = selection.value;
        }
    });
    
    $scope.sectionList = SectionResource.queryAll(function(items){
        $scope.sectionSelectionList = $.map(items, function(item) {
            return ( {
                value : item.id,
                text : item.name
            });
        });
    });
    $scope.$watch("sectionSelection", function(selection) {
        if ( typeof selection != 'undefined') {
            $scope.sectionAllocation.section = {};
            $scope.sectionAllocation.section.id = selection.value;
        }
    });
    

    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            $location.path('/SectionAllocations/edit/' + id);
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError = true;
        };
        SectionAllocationResource.save($scope.sectionAllocation, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("/SectionAllocations");
    };
});
