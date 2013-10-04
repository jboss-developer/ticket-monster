angular.module('ticketmonster').factory('MediaItemResource', function($resource){
    var resource = $resource('../rest/mediaitems/:MediaItemId',{MediaItemId:'@id'},{'queryAll':{method:'GET',isArray:true},'query':{method:'GET',isArray:false},'update':{method:'PUT'}});
    return resource;
});