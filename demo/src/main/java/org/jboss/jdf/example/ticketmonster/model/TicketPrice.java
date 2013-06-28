package org.jboss.jdf.example.ticketmonster.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.jboss.errai.common.client.api.annotations.Portable;

/**
 * <p>
 * Contains price categories - each category represents the price for a ticket in a particular section at a particular venue for
 * a particular event, for a particular ticket category.
 * </p>
 * 
 * <p>
 * The section, show and ticket category form the natural id of this entity, and therefore must be unique. JPA requires us to use the class level
 * <code>@Table</code> constraint
 * </p>
 * 
 * @author Shane Bryzak
 * @author Pete Muir
 */
/*
 * We suppress the warning about not specifying a serialVersionUID, as we are still developing this app, and want the JVM to
 * generate the serialVersionUID for us. When we put this app into production, we'll generate and embed the serialVersionUID
 */
@SuppressWarnings("serial")
@Entity
// TODO Document @JsonIgnoreProperties
@JsonIgnoreProperties("show")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "section_id", "show_id", "ticketcategory_id" }))
@Portable
public class TicketPrice implements Serializable {

    /* Declaration of fields */

    /**
     * The synthetic id of the object.
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    /**
     * <p>
     * The show to which this ticket price category belongs. The <code>@ManyToOne<code> JPA mapping establishes this relationship.
     * </p>
     * 
     * <p>
     * The <code>@NotNull</code> Bean Validation constraint means that the show must be specified.
     * </p>
     */
    @ManyToOne
    @NotNull
    private Show show;

    /**
     * <p>
     * The section to which this ticket price category belongs. The <code>@ManyToOne<code> JPA mapping establishes this relationship.
     * </p>
     * 
     * <p>
     * The <code>@NotNull</code> Bean Validation constraint means that the section must be specified.
     * </p>
     */
    @ManyToOne
    @NotNull
    private Section section;

    /**
     * <p>
     * The ticket category to which this ticket price category belongs. The <code>@ManyToOne<code> JPA mapping establishes this relationship.
     * </p>
     * 
     * <p>
     * The <code>@NotNull</code> Bean Validation constraint means that the ticket category must be specified.
     * </p>
     */
    @ManyToOne
    @NotNull
    private TicketCategory ticketCategory;

    /**
     * The price for this category of ticket.
     */
    private float price;

    /* Boilerplate getters and setters */
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public TicketCategory getTicketCategory() {
        return ticketCategory;
    }

    public void setTicketCategory(TicketCategory ticketCategory) {
        this.ticketCategory = ticketCategory;
    }
    // TODO - Error prone - Change this to Big Decimal
    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    /* equals() and hashCode() for TicketPrice, using the natural identity of the object */
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        TicketPrice that = (TicketPrice) o;

        if (section != null ? !section.equals(that.section) : that.section != null)
            return false;
        if (show != null ? !show.equals(that.show) : that.show != null)
            return false;
        if (ticketCategory != null ? !ticketCategory.equals(that.ticketCategory) : that.ticketCategory != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = show != null ? show.hashCode() : 0;
        result = 31 * result + (section != null ? section.hashCode() : 0);
        result = 31 * result + (ticketCategory != null ? ticketCategory.hashCode() : 0);
        return result;
    }
}
