define([
    'utilities',
    'configuration',
    'text!../../../../templates/desktop/booking-table.html'
],function (utilities,
            config,
            bookingTableTemplate) {

    var BookingsView = Backbone.View.extend({
        events:{
            "click i[data-tm-role='delete']":"deleteBooking",
            "click a[data-tm-role='page']":"refreshPage"
        },
        render:function () {
            var paginator = {};
            paginator.totalPageCount = Math.floor(this.options.count/this.options.pageSize)
                                       + (this.options.count%this.options.pageSize == 0? 0 : 1);
            paginator.currentPage = this.options.page;
            utilities.applyTemplate($(this.el), bookingTableTemplate, {model:this.model.bookings, paginator:paginator});
            return this;
        },
        refreshPage: function(event) {
            if (!_.isUndefined(event)) {
              this.loadPageByNumber($(event.currentTarget).data("tm-page"));
            }
            else {
                this.loadPageByNumber(this.options.page);
            }
        },
        loadPageByNumber: function(page) {
            var options = {};
            if (_.isNumber(page) && page > 0) {
                this.options.page = page;
            }
            options.first = (this.options.page-1)*this.options.pageSize + 1;
            options.maxResults = this.options.pageSize;

            var self = this;
            $.get(
                config.baseUrl + "rest/bookings/count",
                function (data) {
                    self.options.count = data.count;
                    if (self.options.count > 0 ) {
                    self.model.bookings.fetch({data:options,
                        processData:true, success:function () {
                            self.render();
                            $("a[data-tm-page='"+self.options.page+"']").addClass("active")
                        }});
                    } else {
                        self.render();    
                    }
                });

        },
        deleteBooking:function (event) {
            var id = $(event.currentTarget).data("tm-id");
            if (confirm("Are you sure you want to delete booking " + id)) {
                this.model.bookings.get(id).destroy({wait:true});
            };
        }
    });

    return BookingsView;

});