package org.jboss.jdf.example.ticketmonster.model;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * <p>
 * A Booking represents a set of tickets purchased for a performance.
 * </p>
 * 
 * <p>
 * Booking's principle members are a <em>set</em> of tickets, and the performance for which the tickets are booked. It also
 * contains meta-data about the booking, including a contact for the booking, a booking date and a cancellation code
 * </p>
 * 
 * @author Marius Bogoevici
 */
@SuppressWarnings("serial")
@Entity
public class Booking implements Serializable {

    /* Declaration of fields */

    /**
     * The synthetic ID of the object.
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    /**
     * <p>
     * The set of tickets contained within the booking. The <code>@OneToMany<code> JPA mapping establishes this relationship.
     * </p>
     * 
     * <p>
     * The set of tickets is eagerly loaded because FIXME . All operations are cascaded to each ticket, so for example if a
     * booking is removed, then all associated tickets will be removed.
     * </p>
     * 
     * <p>
     * This relationship is uni-directional, so we need to inform JPA to create a foreign key mapping. The foreign key mapping
     * is not visible in the {@link Ticket} entity despite being present in the database.
     * </p>
     * 
     */
    @OneToMany(fetch = EAGER, cascade = ALL)
    @JoinColumn
    @NotEmpty
    @Valid
    private Set<Ticket> tickets = new HashSet<Ticket>();

    /**
     * The performance of the show with which the booking is validated. The
     * <code>@ManyToOne<code> JPA mapping establishes this relationship.
     */
    @ManyToOne
    private Performance performance;

    /**
     * <p>
     * A cancellation code, provided to the ticket booker to allow them to cancel a booking.
     * </p>
     * 
     * <p>
     * The
     * <code>@NotEmpty<code> Bean Validation constraint means that the booking must contain a cancellation code of at least 1 character.
     * </p>
     */
    @NotEmpty
    private String cancellationCode;

    /**
     * <p>
     * The date the booking was made.
     * </p>
     * 
     * <p>
     * The <code>@NotNull</code> Bean Validation constraint means that the booking date must be set. By default, it is set to
     * the date the object was created.
     * </p>
     * 
     */
    @NotNull
    private Date createdOn = new Date();

    /**
     * <p>
     * A contact for the booking, in case the event organizers need to contact the booker. In a later iteration of this demo
     * application, this will be replaced by a full user management system, but this wasn't part of the initial requirements.
     * </p>
     * 
     * <p>
     * Two constraints are applied using Bean Validation
     * </p>
     * 
     * <ol>
     * <li><code>@NotEmpty</code> &mdash; the string must not be null, and must have at least one character.</li>
     * <li><code>@Email</code> &mdash; the string must be a valid email address</li>
     * </ol>
     * 
     */
    @NotEmpty
    @Email(message = "Not a valid email format")
    private String contactEmail;

    /**
     * Compute the total price of all tickets in this booking.
     */
    public float getTotalTicketPrice() {
        float totalPrice = 0.0f;
        for (Ticket ticket : tickets) {
            totalPrice += (ticket.getPrice());
        }
        return totalPrice;
    }

    /* Boilerplate getters and setters */

    public Long getId() {
        return id;
    }

    public Set<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(Set<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getCancellationCode() {
        return cancellationCode;
    }

    public void setCancellationCode(String cancellationCode) {
        this.cancellationCode = cancellationCode;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public Performance getPerformance() {
        return performance;
    }

    public void setPerformance(Performance performance) {
        this.performance = performance;
    }

    /* equals() and hashCode() for Booking, using the synthetic identity of the object */

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Booking booking = (Booking) o;

        if (id != null ? !id.equals(booking.id) : booking.id != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
