'use strict';

angular.module('ticketmonster',['ngResource'])
  .config(['$routeProvider', function($routeProvider) {
    $routeProvider
      .when('/',{templateUrl:'views/landing.html',controller:'LandingPageController'})
      .when('/Bookings',{templateUrl:'views/Booking/search.html',controller:'SearchBookingController'})
      .when('/Bookings/new',{templateUrl:'views/Booking/detail.html',controller:'NewBookingController'})
      .when('/Bookings/edit/:BookingId',{templateUrl:'views/Booking/detail.html',controller:'EditBookingController'})
      .when('/Events',{templateUrl:'views/Event/search.html',controller:'SearchEventController'})
      .when('/Events/new',{templateUrl:'views/Event/detail.html',controller:'NewEventController'})
      .when('/Events/edit/:EventId',{templateUrl:'views/Event/detail.html',controller:'EditEventController'})
      .when('/EventCategorys',{templateUrl:'views/EventCategory/search.html',controller:'SearchEventCategoryController'})
      .when('/EventCategorys/new',{templateUrl:'views/EventCategory/detail.html',controller:'NewEventCategoryController'})
      .when('/EventCategorys/edit/:EventCategoryId',{templateUrl:'views/EventCategory/detail.html',controller:'EditEventCategoryController'})
      .when('/MediaItems',{templateUrl:'views/MediaItem/search.html',controller:'SearchMediaItemController'})
      .when('/MediaItems/new',{templateUrl:'views/MediaItem/detail.html',controller:'NewMediaItemController'})
      .when('/MediaItems/edit/:MediaItemId',{templateUrl:'views/MediaItem/detail.html',controller:'EditMediaItemController'})
      .when('/Performances',{templateUrl:'views/Performance/search.html',controller:'SearchPerformanceController'})
      .when('/Performances/new',{templateUrl:'views/Performance/detail.html',controller:'NewPerformanceController'})
      .when('/Performances/edit/:PerformanceId',{templateUrl:'views/Performance/detail.html',controller:'EditPerformanceController'})
      .when('/Sections',{templateUrl:'views/Section/search.html',controller:'SearchSectionController'})
      .when('/Sections/new',{templateUrl:'views/Section/detail.html',controller:'NewSectionController'})
      .when('/Sections/edit/:SectionId',{templateUrl:'views/Section/detail.html',controller:'EditSectionController'})
      .when('/SectionAllocations',{templateUrl:'views/SectionAllocation/search.html',controller:'SearchSectionAllocationController'})
      .when('/SectionAllocations/new',{templateUrl:'views/SectionAllocation/detail.html',controller:'NewSectionAllocationController'})
      .when('/SectionAllocations/edit/:SectionAllocationId',{templateUrl:'views/SectionAllocation/detail.html',controller:'EditSectionAllocationController'})
      .when('/Shows',{templateUrl:'views/Show/search.html',controller:'SearchShowController'})
      .when('/Shows/new',{templateUrl:'views/Show/detail.html',controller:'NewShowController'})
      .when('/Shows/edit/:ShowId',{templateUrl:'views/Show/detail.html',controller:'EditShowController'})
      .when('/Tickets',{templateUrl:'views/Ticket/search.html',controller:'SearchTicketController'})
      .when('/Tickets/new',{templateUrl:'views/Ticket/detail.html',controller:'NewTicketController'})
      .when('/Tickets/edit/:TicketId',{templateUrl:'views/Ticket/detail.html',controller:'EditTicketController'})
      .when('/TicketCategorys',{templateUrl:'views/TicketCategory/search.html',controller:'SearchTicketCategoryController'})
      .when('/TicketCategorys/new',{templateUrl:'views/TicketCategory/detail.html',controller:'NewTicketCategoryController'})
      .when('/TicketCategorys/edit/:TicketCategoryId',{templateUrl:'views/TicketCategory/detail.html',controller:'EditTicketCategoryController'})
      .when('/TicketPrices',{templateUrl:'views/TicketPrice/search.html',controller:'SearchTicketPriceController'})
      .when('/TicketPrices/new',{templateUrl:'views/TicketPrice/detail.html',controller:'NewTicketPriceController'})
      .when('/TicketPrices/edit/:TicketPriceId',{templateUrl:'views/TicketPrice/detail.html',controller:'EditTicketPriceController'})
      .when('/Venues',{templateUrl:'views/Venue/search.html',controller:'SearchVenueController'})
      .when('/Venues/new',{templateUrl:'views/Venue/detail.html',controller:'NewVenueController'})
      .when('/Venues/edit/:VenueId',{templateUrl:'views/Venue/detail.html',controller:'EditVenueController'})
      .otherwise({
        redirectTo: '/'
      });
  }])
  .controller('LandingPageController', function LandingPageController() {
  })
  .controller('NavController', function NavController($scope, $location) {
    $scope.matchesRoute = function(route) {
        var path = $location.path();
        return (path === ("/" + route) || path.indexOf("/" + route + "/") == 0);
    };
  });
