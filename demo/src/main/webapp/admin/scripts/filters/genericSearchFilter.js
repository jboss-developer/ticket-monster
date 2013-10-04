'use strict';

angular.module('ticketmonster').filter('searchFilter', function() {

    function matchObjectProperties(expectedObject, actualObject) {
        var flag = true;
        for(var key in expectedObject) {
            if(expectedObject.hasOwnProperty(key)) {
                var expectedProperty = expectedObject[key];
                if (expectedProperty == null || expectedProperty === "") {
                    continue;
                }
                var actualProperty = actualObject[key];
                if (angular.isUndefined(actualProperty)) {
                    continue;
                }
                if (actualProperty == null) {
                    flag = false;
                } else if (angular.isObject(expectedProperty)) {
                    flag = flag && matchObjectProperties(expectedProperty, actualProperty);
                } else {
                    flag = flag && (actualProperty.toString().indexOf(expectedProperty.toString()) != -1);
                }
            }
        }
        return flag;
    }

    return function(results) {

        this.filteredResults = [];
        for (var ctr = 0; ctr < results.length; ctr++) {
            var flag = true;
            var searchCriteria = this.search;
            var result = results[ctr];
            for (var key in searchCriteria) {
                if (searchCriteria.hasOwnProperty(key)) {
                    var expected = searchCriteria[key];
                    if (expected == null || expected === "") {
                        continue;
                    }
                    var actual = result[key];
                    if (actual == null) {
                        flag = false;
                    } else if (angular.isObject(expected)) {
                        flag = flag && matchObjectProperties(expected, actual);
                    } else {
                        flag = flag && (actual.toString().indexOf(expected.toString()) != -1);
                    }
                }
            }
            if (flag == true) {
                this.filteredResults.push(result);
            }
        }
        this.numberOfPages();
        return this.filteredResults;
    };
});
