package org.jboss.jdf.example.ticketmonster.rest.search;

import java.util.ArrayList;
import java.util.List;

public class ShowResults {
    private List<ShowView> results;
    private List<FacetGroupView> facetGroups = new ArrayList<FacetGroupView>();

    public ShowResults(List<ShowView> results) {
        this.results = results;
    }
    
    public ShowResults addFacetGroup(FacetGroupView facetGroup) {
        facetGroups.add(facetGroup);
        return this;
    }

    public List<ShowView> getResults() {
        return results;
    }
    
    public List<FacetGroupView> getFacetGroups() {
        return facetGroups;
    }
}
