package org.jboss.jdf.example.ticketmonster.model;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;

/**
 * TODO
 * @author Marius Bogoevici
 * @author Pete Muir
 */
@Embeddable
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
