package org.jboss.jdf.example.ticketmonster.model;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;

import org.jboss.errai.common.client.api.annotations.Portable;

/**
 * <p>
 * Represents a seat within a @Section
 * </p>
 * 
 * <p>
 * A Seat is annotated as @Embeddable entity, that appears as a related entity in the object model but no relationship
 * is established in the RDMS level.
 * </p>
 * 
 * @author Marius Bogoevici
 * @author Pete Muir
 */
@Embeddable
@Portable
public class Seat {

    @Min(1)
    private int rowNumber;

    @Min(1)
    private int number;

    /**
     * The <code>@ManyToOne<code> JPA mapping establishes this relationship.
     */
    @ManyToOne
    private Section section;

    /** Constructor for persistence */
    public Seat() {
    }

    /* Boilerplate getters and setters */

    public Seat(Section section, int rowNumber, int number) {
        this.section = section;
        this.rowNumber = rowNumber;
        this.number = number;
    }

    public Section getSection() {
        return section;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public int getNumber() {
        return number;
    }
    
    @Override
    public String toString() {
        return new StringBuilder().append(getSection()).append(" (").append(getRowNumber()).append(", ").append(getNumber()).append(")").toString();
    }
}
