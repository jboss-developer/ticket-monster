package org.jboss.jdf.example.ticketmonster.rest.search;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.search.query.facet.Facet;

public class FacetGroupView {
    private String name;
    private List<FacetView> facets = new ArrayList<FacetView>();
    
    public FacetGroupView(String name, List<Facet> rawFacets) {
        this.name = name;
        for(Facet facet : rawFacets) {
            facets.add(new FacetView(facet));
        }
    }
    
    public String getName() {
        return name;
    }
    
    public List<FacetView> getFacets() {
        return facets;
    }
    
    public void addFacet(FacetView facet) {
        facets.add(facet);
    }
}
