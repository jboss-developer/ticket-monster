define([
    'utilities',
    'require',
    'configuration',
    'text!../../../../templates/desktop/booking-confirmation.html',
    'text!../../../../templates/desktop/create-booking.html',
    'text!../../../../templates/desktop/ticket-categories.html',
    'text!../../../../templates/desktop/ticket-summary-view.html',
    'bootstrap'
],function (
    utilities,
    require,
    config,
    bookingConfirmationTemplate,
    createBookingTemplate,
    ticketEntriesTemplate,
    ticketSummaryViewTemplate){


    var TicketCategoriesView = Backbone.View.extend({
        id:'categoriesView',
        events:{
            "keyup input":"onChange"
        },
        render:function () {
            if (this.model != null) {
                var ticketPrices = _.map(this.model, function (item) {
                    return item.ticketPrice;
                });
                utilities.applyTemplate($(this.el), ticketEntriesTemplate, {ticketPrices:ticketPrices});
            } else {
                $(this.el).empty();
            }
            return this;
        },
        onChange:function (event) {
            var value = event.currentTarget.value;
            var ticketPriceId = $(event.currentTarget).data("tm-id");
            var modifiedModelEntry = _.find(this.model, function (item) {
                return item.ticketPrice.id == ticketPriceId
            });
            // update model
            if ($.isNumeric(value) && value > 0) {
                modifiedModelEntry.quantity = parseInt(value);
            }
            else {
                delete modifiedModelEntry.quantity;
            }
            // display error messages
            if (value.length > 0 &&
                   (!$.isNumeric(value)  // is a non-number, other than empty string
                        || value <= 0 // is negative
                        || parseFloat(value) != parseInt(value))) { // is not an integer
                $("#error-input-"+ticketPriceId).empty().append("Please enter a positive integer value");
                $("#ticket-category-fieldset-"+ticketPriceId).addClass("error")
            } else {
                $("#error-input-"+ticketPriceId).empty();
                $("#ticket-category-fieldset-"+ticketPriceId).removeClass("error")
            }
            // are there any outstanding errors after this update?
            // if yes, disable the input button
            if (
               $("div[id^='ticket-category-fieldset-']").hasClass("error") ||
                   _.isUndefined(modifiedModelEntry.quantity) ) {
              $("input[name='add']").attr("disabled", true)
            } else {
              $("input[name='add']").removeAttr("disabled")
            }
        }
    });

    var TicketSummaryView = Backbone.View.extend({
        tagName:'tr',
        events:{
            "click i":"removeEntry"
        },
        render:function () {
            var self = this;
            utilities.applyTemplate($(this.el), ticketSummaryViewTemplate, this.model.bookingRequest);
        },
        removeEntry:function (event) {
           var index = $(event.currentTarget).data("index");
           var ticketPriceId = this.model.bookingRequest.seatAllocations[index].ticketRequest.ticketPrice.id;
           var self = this;
           $.ajax({url: (config.baseUrl + "rest/carts/" + this.model.cartId),
                data: JSON.stringify([{ticketPrice:ticketPriceId, quantity:-1}]),
                type: "POST",
                dataType: "json",
                contentType: "application/json",
                success: function(cart) {
                    self.owner.refreshSummary(cart, self.owner)
                }
           });
        }
    });

    var CreateBookingView = Backbone.View.extend({

        events:{
            "click input[name='submit']":"save",
            "change select[id='sectionSelect']":"refreshPrices",
            "keyup #email":"updateEmail",
            "change #email":"updateEmail",
            "click input[name='add']":"addQuantities"
        },
        render:function () {

            var self = this;
            $.ajax({url: (config.baseUrl + "rest/carts"),
                    data:JSON.stringify({performance:this.model.performanceId}),
                    type:"POST",
                    dataType:"json",
                    contentType:"application/json",
                    success: function (cart) {
                        self.model.cartId = cart.id;
                        $.getJSON(config.baseUrl + "rest/shows/" + self.model.showId, function (selectedShow) {

                            self.currentPerformance = _.find(selectedShow.performances, function (item) {
                                return item.id == self.model.performanceId;
                            });

                            var id = function (item) {return item.id;};
                            // prepare a list of sections to populate the dropdown
                            var sections = _.uniq(_.sortBy(_.pluck(selectedShow.ticketPrices, 'section'), id), true, id);
                            utilities.applyTemplate($(self.el), createBookingTemplate, {
                                sections:sections,
                                show:selectedShow,
                                performance:self.currentPerformance});
                            self.ticketCategoriesView = new TicketCategoriesView({model:{}, el:$("#ticketCategoriesViewPlaceholder")});
                            self.ticketSummaryView = new TicketSummaryView({model:self.model, el:$("#ticketSummaryView")});
                            self.ticketSummaryView.owner = self;
                            self.show = selectedShow;
                            self.ticketCategoriesView.render();
                            self.ticketSummaryView.render();
                            $("#sectionSelector").change();
                        });
                    }
                }
            );
            return this;
        },
        refreshPrices:function (event) {
            var ticketPrices = _.filter(this.show.ticketPrices, function (item) {
                return item.section.id == event.currentTarget.value;
            });
            var sortedTicketPrices = _.sortBy(ticketPrices, function(ticketPrice) {
                return ticketPrice.ticketCategory.description;
            });
            var ticketPriceInputs = new Array();
            _.each(sortedTicketPrices, function (ticketPrice) {
                ticketPriceInputs.push({ticketPrice:ticketPrice});
            });
            this.ticketCategoriesView.model = ticketPriceInputs;
            this.ticketCategoriesView.render();
        },
        save:function (event) {
            var bookingRequest = {ticketRequests:[]};
            var self = this;
            bookingRequest.email = this.model.bookingRequest.email;
            bookingRequest.performance = this.model.performanceId
            $("input[name='submit']").attr("disabled", true)
            $.ajax({url: (config.baseUrl + "rest/carts/" + this.model.cartId + "/checkout"),
                data:JSON.stringify({email:this.model.bookingRequest.email}),
                type:"POST",
                dataType:"json",
                contentType:"application/json",
                success:function (booking) {
                    this.model = {}
                    $.getJSON(config.baseUrl +'rest/shows/performance/' + booking.performance.id, function (retrievedPerformance) {
                        utilities.applyTemplate($(self.el), bookingConfirmationTemplate, {booking:booking, performance:retrievedPerformance })
                    });
                }}).error(function (error) {
                    if (error.status == 400 || error.status == 409) {
                        var errors = $.parseJSON(error.responseText).errors;
                        _.each(errors, function (errorMessage) {
                            $("#request-summary").append('<div class="alert alert-error"><a class="close" data-dismiss="alert">×</a><strong>Error!</strong> ' + errorMessage + '</div>')
                        });
                    } else {
                        $("#request-summary").append('<div class="alert alert-error"><a class="close" data-dismiss="alert">×</a><strong>Error! </strong>An error has occured</div>')
                    }
                    $("input[name='submit']").removeAttr("disabled");
                })

        },
        calculateTotals:function () {
            // make sure that tickets are sorted by section and ticket category
            this.model.bookingRequest.seatAllocations.sort(function (t1, t2) {
                if (t1.ticketRequest.ticketPrice.section.id != t2.ticketRequest.ticketPrice.section.id) {
                    return t1.ticketRequest.ticketPrice.section.id - t2.ticketRequest.ticketPrice.section.id;
                }
                else {
                    return t1.ticketRequest.ticketPrice.ticketCategory.id - t2.ticketRequest.ticketPrice.ticketCategory.id;
                }
            });

            this.model.bookingRequest.totals = _.reduce(this.model.bookingRequest.seatAllocations, function (totals, seatAllocation) {
                var ticketRequest = seatAllocation.ticketRequest;
                return {
                    tickets:totals.tickets + ticketRequest.quantity,
                    price:totals.price + ticketRequest.quantity * ticketRequest.ticketPrice.price
                };
            }, {tickets:0, price:0.0});
        },
        addQuantities:function () {
            var self = this;
            var ticketRequests = [];
            _.each(this.ticketCategoriesView.model, function (model) {
                if (model.quantity != undefined) {
                    ticketRequests.push({ticketPrice:model.ticketPrice.id, quantity:model.quantity})
                }
            });
            $.ajax({url: (config.baseUrl + "rest/carts/" + this.model.cartId),
                data:JSON.stringify(ticketRequests),
                type:"POST",
                dataType:"json",
                contentType:"application/json",
                success: function(cart) {
                   self.refreshSummary(cart, self)
                }}
            );
        },
        refreshSummary: function(cart, view) {
            view.model.bookingRequest.seatAllocations = cart.seatAllocations;
            view.ticketCategoriesView.model = null;
            $('option:selected', 'select').removeAttr('selected');
            view.calculateTotals();
            view.ticketCategoriesView.render();
            view.ticketSummaryView.render();
            view.setCheckoutStatus();
        },
        updateEmail:function (event) {
            if ($(event.currentTarget).is(':valid')) {
                this.model.bookingRequest.email = event.currentTarget.value;
                $("#error-email").empty();
            } else {
                $("#error-email").empty().append("Please enter a valid e-mail address");
                delete this.model.bookingRequest.email;
            }
            this.setCheckoutStatus();
        },
        setCheckoutStatus:function () {
            if (this.model.bookingRequest.totals != undefined && this.model.bookingRequest.totals.tickets > 0 && this.model.bookingRequest.email != undefined && this.model.bookingRequest.email != '') {
                $('input[name="submit"]').removeAttr('disabled');
            }
            else {
                $('input[name="submit"]').attr('disabled', true);
            }
        }
    });

    return CreateBookingView;
});