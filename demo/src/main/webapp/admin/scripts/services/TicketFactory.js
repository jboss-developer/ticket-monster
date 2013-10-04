angular.module('ticketmonster').factory('TicketResource', function($resource){
    var resource = $resource('../rest/tickets/:TicketId',{TicketId:'@id'},{'queryAll':{method:'GET',isArray:true},'query':{method:'GET',isArray:false},'update':{method:'PUT'}});
    return resource;
});