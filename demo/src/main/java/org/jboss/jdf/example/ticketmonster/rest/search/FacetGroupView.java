package org.jboss.jdf.example.ticketmonster.rest.search;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.search.query.engine.spi.FacetManager;
import org.hibernate.search.query.facet.Facet;

public class FacetGroupView {
    private String name;
    private String id;
    private List<FacetView> facets = new ArrayList<FacetView>();
    
    public FacetGroupView(String name, FacetManager fm, String facetingName) {
        this.name = name;
        this.id = facetingName;
        for(Facet facet : fm.getFacets(facetingName)) {
            boolean selected = false;
            for(Facet selectedFacet : fm.getFacetGroup(facetingName).getSelectedFacets()) {
                if (facet.getFacetingName().equals(selectedFacet.getFacetingName())
                    && facet.getFieldName().equals(selectedFacet.getFieldName())
                    && facet.getValue().equals(selectedFacet.getValue()) ) {
                    selected = true; 
                    break;
                }
            }
            //TODO replace for loop when HSEARCH-1346 is fixed
            //boolean selected = fm.getFacetGroup(facetingName).getSelectedFacets().contains(facet);
            facets.add(new FacetView(facet, selected));
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
    
    public String getId() {
        return id;
    }
    
    public boolean isWithSelectedFacet() {
        for(FacetView facet : facets) {
            if (facet.isSelected()) {
                return true;
            }
        }
        return false;
    }
}
