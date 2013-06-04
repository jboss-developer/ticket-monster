define([
    'utilities',
    'configuration',
    'require',
    'text!../../../../templates/mobile/booking-details.html',
    'text!../../../../templates/mobile/create-booking.html',
    'text!../../../../templates/mobile/confirm-booking.html',
    'text!../../../../templates/mobile/ticket-entries.html',
    'text!../../../../templates/mobile/ticket-summary-view.html'
], function (
    utilities,
    config,
    require,
    bookingDetailsTemplate,
    createBookingTemplate,
    confirmBookingTemplate,
    ticketEntriesTemplate,
    ticketSummaryViewTemplate) {

    var TicketCategoriesView = Backbone.View.extend({
        id:'categoriesView',
        events:{
            "change input":"onChange"
        },
        render:function () {
            var views = {};

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
            var modifiedModelEntry = _.find(this.model, function(item) { return item.ticketPrice.id == ticketPriceId});
            if ($.isNumeric(value) && value > 0) {
                modifiedModelEntry.quantity = parseInt(value);
            }
            else {
                delete modifiedModelEntry.quantity;
            }
        }
    });

     var TicketSummaryView = Backbone.View.extend({
        render:function () {
            utilities.applyTemplate($(this.el), ticketSummaryViewTemplate, this.model.bookingRequest)
        }
    });

    var CreateBookingView = Backbone.View.extend({

        currentView: "CreateBooking",
        events:{
            "click a[id='confirmBooking']":"checkout",
            "change select":"refreshPrices",
            "blur input[type='number']":"updateForm",
            "blur input[name='email']":"updateForm",
            "click a[id='saveBooking']":"saveBooking",
            "click a[id='goBack']":"back",
            "click a[data-action='delete']":"deleteBooking"
        },
        render: function() {
            if (this.currentView === "CreateBooking") {
                this.renderCreateBooking();
            } else if(this.currentView === "ConfirmBooking") {
                this.renderConfirmBooking();
            }
            return this;
        },
        renderCreateBooking:function () {

            var self = this;

            $.getJSON(config.baseUrl + "rest/shows/" + this.model.showId, function (selectedShow) {
                self.model.performance = _.find(selectedShow.performances, function (item) {
                    return item.id == self.model.performanceId;
                });
                self.model.email = self.model.email || ""; 
                var id = function (item) {return item.id;};
                // prepare a list of sections to populate the dropdown
                var sections = _.uniq(_.sortBy(_.pluck(selectedShow.ticketPrices, 'section'), id), true, id);

                utilities.applyTemplate($(self.el), createBookingTemplate, { show:selectedShow,
                    performance:self.model.performance,
                    sections:sections,
                    email:self.model.email});
                $(self.el).trigger('pagecreate');
                self.ticketCategoriesView = new TicketCategoriesView({model:{}, el:$("#ticketCategoriesViewPlaceholder") });
                self.model.show = selectedShow;
                self.ticketCategoriesView.render();
                $('a[id="confirmBooking"]').addClass('ui-disabled');
                $("#sectionSelector").change();
            });

        },
        refreshPrices:function (event) {
            if (event.currentTarget.value != "Choose a section") {
                var ticketPrices = _.filter(this.model.show.ticketPrices, function (item) {
                    return item.section.id == event.currentTarget.value;
                });
                var ticketPriceInputs = new Array();
                _.each(ticketPrices, function (ticketPrice) {
                    var model = {};
                    model.ticketPrice = ticketPrice;
                    ticketPriceInputs.push(model);
                });
                $("#ticketCategoriesViewPlaceholder").show();
                this.ticketCategoriesView.model = ticketPriceInputs;
                this.ticketCategoriesView.render();
                $(this.el).trigger('pagecreate');
            } else {
                $("#ticketCategoriesViewPlaceholder").hide();
                this.ticketCategoriesView.model = new Array();
                this.updateForm();
            }
        },
        checkout:function () {
            var savedTicketRequests = this.model.bookingRequest.tickets = this.model.bookingRequest.tickets || [];
            _.each(this.ticketCategoriesView.model, function(newTicketRequest){
                var matchingRequest = _.find(savedTicketRequests, function(ticketRequest) {
                    return ticketRequest.ticketPrice.id == newTicketRequest.ticketPrice.id;
                });
                if(newTicketRequest.quantity) {
                    if(matchingRequest) {
                        matchingRequest.quantity += newTicketRequest.quantity;
                    } else {
                        savedTicketRequests.push(newTicketRequest);
                    }
                }
            });
            this.model.bookingRequest.totals = this.computeTotals(this.model.bookingRequest.tickets);
            this.currentView = "ConfirmBooking";
            this.render();
        },
        updateForm:function () {
            this.model.email = $("input[type='email']").val();
            var totals = this.computeTotals(this.ticketCategoriesView.model);
            if (totals.tickets > 0 && $("input[type='email']").val()) {
                $('a[id="confirmBooking"]').removeClass('ui-disabled');
            } else {
                $('a[id="confirmBooking"]').addClass('ui-disabled');
            }
        },
        computeTotals: function(ticketRequestCollection) {
            var totals = _.reduce(ticketRequestCollection, function (partial, model) {
                if (model.quantity != undefined) {
                    partial.tickets += model.quantity;
                    partial.price += model.quantity * model.ticketPrice.price;
                    return partial;
                } else {
                    return partial;
                }
            }, {tickets:0, price:0.0});
            return totals;
        },
        renderConfirmBooking:function () {
            utilities.applyTemplate($(this.el), confirmBookingTemplate, this.model);
            this.ticketSummaryView = new TicketSummaryView({model:this.model, el:$("#ticketSummaryView")});
            this.ticketSummaryView.render();
            $(this.el).trigger('pagecreate');
            if (this.model.bookingRequest.totals.tickets > 0) {
                $('a[id="saveBooking"]').removeClass('ui-disabled');
            } else {
                $('a[id="saveBooking"]').addClass('ui-disabled');
            }
            return this;
        },
        back:function () {
            this.currentView = "CreateBooking";
            this.render();
        },
        saveBooking:function (event) {
            var bookingRequest = {ticketRequests:[]};
            var self = this;
            _.each(this.model.bookingRequest.tickets, function (model) {
                if (model.quantity != undefined) {
                    bookingRequest.ticketRequests.push({ticketPrice:model.ticketPrice.id, quantity:model.quantity})
                }
            });

            bookingRequest.email = this.model.email;
            bookingRequest.performance = this.model.performanceId;
            $.ajax({url:(config.baseUrl + "rest/bookings"),
                data:JSON.stringify(bookingRequest),
                type:"POST",
                dataType:"json",
                contentType:"application/json",
                success:function (booking) {
                    utilities.applyTemplate($(self.el), bookingDetailsTemplate, booking);
                    $(self.el).trigger('pagecreate');
                }}).error(function (error) {
                    alert(error);
                });
        },
        deleteBooking: function(event) {
            var deletedIdx = $(event.currentTarget).data("ticketpriceid");
            this.model.bookingRequest.tickets = _.reject(this.model.bookingRequest.tickets, function(ticketRequest) {
                return ticketRequest.ticketPrice.id == deletedIdx; 
            });
            this.model.bookingRequest.totals = this.computeTotals(this.model.bookingRequest.tickets);
            this.renderConfirmBooking();
            return false;
        }
    });
    return CreateBookingView;
});