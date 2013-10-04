'use strict';

var LOCAL_DATETIME_REGEX = /^([0-2][0-9]{3})\-([0-1][0-9])\-([0-3][0-9])\ ([0-5][0-9])\:([0-5][0-9])\:([0-5][0-9])$/;
angular.module('ticketmonster').directive('datetime', function($parse, dateFilter) {
    return {
        restrict : "E",
        replace : true,
        transclude : false,
        require: '?ngModel',
        compile : function(element, attrs) {
            var modelAccessor = $parse(attrs.ngModel);

            var isRequired = "";
            if(attrs.required) {
                isRequired = "required";
            }
            var html = "<input id='" + attrs.id + "' name='" + attrs.name + "' type='datetime-local' " + isRequired + " placeholder='"+attrs.placeholder+"'></input>";

            var $newElem = $(html);
            element.replaceWith($newElem);

            var nativeLinker = function(scope, element, attrs, ngModelCtrl) {
                if(!ngModelCtrl) return;

                var localDateTimeParser = function(value) {
                    var date;
                    if(value) {
                        date = new Date(Date.parse(value));
                        date.setTime(date.getTime() + date.getTimezoneOffset()*60000);
                    } else {
                        date = value;
                    }
                    return date;
                }
                var localDateTimeFormatter = function(value) {
                    if(value) {
                        var date = dateFilter(value,"yyyy-MM-ddTHH:mm:ss");
                        return date;
                    }
                    return;
                }

                ngModelCtrl.$parsers.unshift(localDateTimeParser);
                ngModelCtrl.$formatters.unshift(localDateTimeFormatter);

                element.bind("blur keyup change", function() {
                    scope.$apply(function() {
                        ngModelCtrl.$setViewValue(element.val());
                    });
                });

                ngModelCtrl.$render = function() {
                    element.val(ngModelCtrl.$viewValue);
                }
            }

            var enhancedLinker = function(scope, element, attrs, ngModelCtrl) {
                if(!ngModelCtrl) return;
                
                var localDateTimeParser = function(value) {
                    if(value) {
                        var d = value.match(LOCAL_DATETIME_REGEX);
                        if(d) {
                            var formattedDate = d[1] + "-" + d[2] + "-" + d[3] + "T"
                                            + d[4] + ":" + d[5] + ":" + d[6];
                            var date = new Date(Date.parse(formattedDate));
                            ngModelCtrl.$setValidity("dateFormat", true);
                            return date;
                        } else {
                            ngModelCtrl.$setValidity("dateFormat", false);
                            return;
                        }
                    }
                    return;
                }
                var localDateTimeFormatter = function(value) {
                    if(value) {
                        var date = dateFilter(value,"yyyy-MM-dd HH:mm:ss");
                        return date;
                    }
                    return;
                }
                
                ngModelCtrl.$parsers.unshift(localDateTimeParser);
                ngModelCtrl.$formatters.unshift(localDateTimeFormatter);

                element.bind("blur keyup change", function() {
                    scope.$apply(function() {
                        ngModelCtrl.$setViewValue(element.val());
                    });
                });

                ngModelCtrl.$render = function() {
                    element.val(ngModelCtrl.$viewValue);
                }
            }

            if(Modernizr.inputtypes["datetime-local"]) {
                return nativeLinker;
            } else {
                return enhancedLinker;
            }
        }
    }
});