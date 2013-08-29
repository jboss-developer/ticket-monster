package org.jboss.jdf.example.ticketmonster.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * <p>
 * Categories of event.
 * </p>
 * 
 * <p>
 * {@link EventCategory} is a simple entity, used to easier filtering of information by users.
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
public class EventCategory implements Serializable {

    /* Declaration of fields */
    
    /**
     * The synthetic id of the object.
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    /**
     * <p>
     * A description of the event category.
     * </p>
     * 
     * <p>
     * The description of an event category forms it's natural id and cannot be shared between event categories
     * </p>
     * 
     * <p>
     * The <code>@NotEmpty<code> Bean Validation constraint means that the event category descripton must be least 1 character and cannot be null.
     * </p>
     */
    @Column(unique=true)
    @NotEmpty
    private String description;

    /* Boilerplate getters and setters */
    
    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /* toString(), equals() and hashCode() for EventCategory, using the natural identity of the object */
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        EventCategory that = (EventCategory) o;

        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return description != null ? description.hashCode() : 0;
    }

    @Override
    public String toString() {
        return description;
    }
}
