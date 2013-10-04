angular.module('ticketmonster').factory('EventResource', function($resource){
    var resource = $resource('../rest/forge/events/:EventId',{EventId:'@id'},{'queryAll':{method:'GET',isArray:true},'query':{method:'GET',isArray:false},'update':{method:'PUT'}});
    return resource;
});