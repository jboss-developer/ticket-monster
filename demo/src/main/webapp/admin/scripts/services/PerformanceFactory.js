angular.module('ticketmonster').factory('PerformanceResource', function($resource){
    var resource = $resource('../rest/performances/:PerformanceId',{PerformanceId:'@id'},{'queryAll':{method:'GET',isArray:true},'query':{method:'GET',isArray:false},'update':{method:'PUT'}});
    return resource;
});