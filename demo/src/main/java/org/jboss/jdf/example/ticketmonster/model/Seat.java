package org.jboss.jdf.example.ticketmonster.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;

import org.jboss.errai.common.client.api.annotations.Portable;

/**
 * TODO
 * @author Marius Bogoevici
 * @author Pete Muir
 */
/*
 * We suppress the warning about not specifying a serialVersionUID, as we are still developing this app, and want the JVM to
 * generate the serialVersionUID for us. When we put this app into production, we'll generate and embed the serialVersionUID
 */
@SuppressWarnings("serial")
@Embeddable
@Portable
public class Seat implements Serializable {

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
