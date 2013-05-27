package org.jboss.jdf.example.ticketmonster.rest.search;

import java.util.List;

public class ShowResults {
    private List<ShowView> results;

    public ShowResults(List<ShowView> results) {
        this.results = results;
    }

    public List<ShowView> getResults() {
        return results;
    }
}
