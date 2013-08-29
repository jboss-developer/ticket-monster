package org.jboss.jdf.example.ticketmonster.model;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * <p>
 * Represents a single venue
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
public class Venue implements Serializable {

    /* Declaration of fields */

    /**
     * The synthetic id of the object.
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    /**
     * <p>
     * The name of the event.
     * </p>
     * 
     * <p>
     * The name of the event forms it's natural identity and cannot be shared between events.
     * </p>
     * 
     * <p>
     * The name must not be null and must be one or more characters, the Bean Validation constrain <code>@NotEmpty</code>
     * enforces this.
     * </p>
     */
    @Column(unique = true)
    @NotEmpty
    private String name;

    /**
     * The address of the venue
     */
    private Address address = new Address();

    /**
     * A description of the venue
     */
    private String description;

    /**
     * <p>
     * A set of sections in the venue
     * </p>
     * 
     * <p>
     * The <code>@OneToMany<code> JPA mapping establishes this relationship. TODO Explain EAGER fetch. 
     * This relationship is bi-directional (a section knows which venue it is part of), and the <code>mappedBy</code>
     * attribute establishes this. We cascade all persistence operations to the set of performances, so, for example if a venue
     * is removed, then all of it's sections will also be removed.
     * </p>
     */
    @OneToMany(cascade = ALL, fetch = EAGER, mappedBy = "venue")
    private Set<Section> sections = new HashSet<Section>();

    /**
     * The capacity of the venue
     */
    private int capacity;

    /**
     * An optional media item to entice punters to the venue. The <code>@ManyToOne</code> establishes the relationship.
     */
    @ManyToOne
    private MediaItem mediaItem;

    /* Boilerplate getters and setters */
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public MediaItem getMediaItem() {
        return mediaItem;
    }

    public void setMediaItem(MediaItem description) {
        this.mediaItem = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Section> getSections() {
        return sections;
    }

    public void setSections(Set<Section> sections) {
        this.sections = sections;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /* toString(), equals() and hashCode() for Venue, using the natural identity of the object */
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Venue venue = (Venue) o;

        if (address != null ? !address.equals(venue.address) : venue.address != null)
            return false;
        if (name != null ? !name.equals(venue.name) : venue.name != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (address != null ? address.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return name;
    }
}
