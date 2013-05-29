package org.jboss.jdf.example.ticketmonster.rest.search;

import org.hibernate.search.query.facet.Facet;

public class FacetView {
    private String value;
    private int count;
    
    public FacetView(Facet facet) {
        this.value = facet.getValue();
        this.count = facet.getCount();
    }
    
    public FacetView(String value, int count) {
        this.value = value;
        this.count = count;
    }

    public String getValue() {
        return value;
    }
    public int getCount() {
        return count;
    }
}
