define([
    'utilities',
    'require',
    'configuration',
    'text!../../../../templates/mobile/venue-detail.html',
    'text!../../../../templates/mobile/venue-event-description.html'
], function (
    utilities,
    require,
    config,
    venueDetail,
    venueEventDescription) {

    var VenueDetailView = Backbone.View.extend({

        events:{
            "click a[id='bookButton']":"beginBooking",
            "change select[id='showSelector']":"refreshShows",
            "change select[id='performanceTimes']":"performanceSelected",
            "change select[id='dayPicker']":'refreshTimes'
        },
        render:function () {
            $(this.el).empty()
            utilities.applyTemplate($(this.el), venueDetail, _.extend({}, this.model.attributes, config));
            $(this.el).trigger('create')
            $("#bookButton").addClass("ui-disabled")
            var self = this
            $.getJSON(config.baseUrl + "rest/shows?venue=" + this.model.get('id'), function (shows) {
                self.shows = shows;
                $("#showSelector").empty().append("<option data-placeholder='true'>Choose an event ...</option>");
                $.each(shows, function (i, show) {
                    $("#showSelector").append("<option value='" + show.id + "'>" + show.event.name + "</option>")
                })
                $("#showSelector").selectmenu('refresh', true)
                $("#dayPicker").selectmenu('disable')
                $("#dayPicker").empty().append("<option data-placeholder='true'>Choose a show date ...</option>")
                $("#performanceTimes").selectmenu('disable')
                $("#performanceTimes").empty().append("<option data-placeholder='true'>Choose a show time ...</option>")

            });
            $("#dayPicker").empty();
            $("#dayPicker").selectmenu('disable');
            $("#performanceTimes").empty();
            $("#performanceTimes").selectmenu('disable');
            $(this.el).trigger('pagecreate');
            $("#showSelector").selectmenu('refresh', true);
            $("#performanceTimes").selectmenu('refresh');
            return this;
        },
        performanceSelected:function () {
            if ($("#performanceTimes").val() != 'Choose a show time ...') {
                $("#bookButton").removeClass("ui-disabled")
            } else {
                $("#bookButton").addClass("ui-disabled")
            }
        },
        beginBooking:function () {
            require('router').navigate('/book/' + $("#showSelector option:selected").val() + '/' + $("#performanceTimes").val(), true)
        },
        refreshShows:function (event) {

            var selectedShowId = event.currentTarget.value;

            if (selectedShowId != 'Choose a venue ...') {
                var selectedShow = _.find(this.shows, function (show) {
                    return show.id == selectedShowId
                });
                this.selectedShow = selectedShow;
                var times = _.uniq(_.sortBy(_.map(selectedShow.performances, function (performance) {
                    return (new Date(performance.date).withoutTimeOfDay()).getTime()
                }), function (item) {
                    return item
                }));
                utilities.applyTemplate($("#venueEventDescription"), venueEventDescription, _.extend({}, {event:selectedShow.event}, config));
                $("#detailsCollapsible").show()
                $("#dayPicker").removeAttr('disabled')
                $("#performanceTimes").removeAttr('disabled')
                $("#dayPicker").empty().append("<option data-placeholder='true'>Choose a show date ...</option>")
                _.each(times, function (time) {
                    var date = new Date(time)
                    $("#dayPicker").append("<option value='" + date.toYMD() + "'>" + date.toPrettyStringWithoutTime() + "</option>")
                });
                $("#dayPicker").selectmenu('refresh')
                $("#dayPicker").selectmenu('enable')
                this.refreshTimes()
            } else {
                $("#detailsCollapsible").hide()
                $("#venueEventDescription").empty()
                $("#dayPicker").empty()
                $("#dayPicker").selectmenu('disable')
                $("#performanceTimes").empty()
                $("#performanceTimes").selectmenu('disable')
            }


        },
        refreshTimes:function () {
            var selectedDate = $("#dayPicker").val();
            $("#performanceTimes").empty().append("<option data-placeholder='true'>Choose a show time ...</option>")
            if (selectedDate) {
                $.each(this.selectedShow.performances, function (i, performance) {
                    var performanceDate = new Date(performance.date);
                    if (_.isEqual(performanceDate.toYMD(), selectedDate)) {
                        $("#performanceTimes").append("<option value='" + performance.id + "'>" + performanceDate.getHours().toZeroPaddedString(2) + ":" + performanceDate.getMinutes().toZeroPaddedString(2) + "</option>")
                    }
                })
                $("#performanceTimes").selectmenu('enable')
            }
            $("#performanceTimes").selectmenu('refresh')
            this.performanceSelected()
        }

    });
    return  VenueDetailView;

});