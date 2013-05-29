package org.jboss.jdf.example.ticketmonster.model.search;

import java.util.Set;

import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.builtin.NumericFieldBridge;
import org.jboss.jdf.example.ticketmonster.model.TicketPrice;

/**
 * Find the minimum price for a give show and index it.
 * 
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 *
 */
public class PriceMinBridge extends NumericFieldBridge {

    @Override
    public Object get(String name, Document document) {
        return Float.valueOf( document.getFieldable( name ).stringValue() );
    }
    
    @Override
    public void set(String name, Object value, Document document, LuceneOptions luceneOptions) {
        if (value != null) {
            float min = Float.MAX_VALUE;
            for (TicketPrice price : (Set<TicketPrice>) value) {
                float current = price.getPrice();
                if (current < min) {
                    min = current;
                }
            }
            luceneOptions.addNumericFieldToDocument(name, min, document);
        }
    }
}
