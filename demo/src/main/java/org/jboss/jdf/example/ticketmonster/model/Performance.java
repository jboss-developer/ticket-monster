package org.jboss.jdf.example.ticketmonster.model;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * <p>
 * A performance represents a single instance of a show.
 * </p>
 * 
 * <p>
 * The show and date form the natural id of this entity, and therefore must be unique. JPA requires us to use the class level
 * <code>@Table</code> constraint.
 * </p>
 * 
 * @author Marius Bogoevici
 * @author Pete Muir
 */
/*
 * We suppress the warning about not specifying a serialVersionUID, as we are still developing this app, and want the JVM to
 * generate the serialVersionUID for us. When we put this app into production, we'll generate and embed the serialVersionUID
 */
@SuppressWarnings("serial")
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "date", "show_id" }))
// TODO Document use of @JsonIgnoreProperties
@JsonIgnoreProperties("show")
public class Performance implements Serializable {

    /* Declaration of fields */

    /**
     * The synthetic id of the object.
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    /**
     * <p>
     * The date and start time of the performance.
     * </p>
     * 
     * <p>
     * A Java {@link Date} object represents both a date and a time, whilst an RDBMS splits out Date, Time and Timestamp.
     * Therefore we instruct JPA to store this date as a timestamp using the <code>@Temporal(TIMESTAMP)</code> annotation.
     * </p>
     * 
     * <p>
     * The date and time of the performance is required, and the Bean Validation constraint <code>@NotNull</code> enforces this.
     * </p>
     */
    @Temporal(TIMESTAMP)
    @NotNull
    private Date date;

    /**
     * <p>
     * The show of which this is a performance. The <code>@ManyToOne<code> JPA mapping establishes this relationship.
     * </p>
     * 
     * <p>
     * The show of which this is a performance is required, and the Bean Validation constraint <code>@NotNull</code> enforces
     * this.
     * </p>
     */
    @ManyToOne
    @NotNull
    private Show show;

    /* Boilerplate getters and setters */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public Show getShow() {
        return show;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    /* equals() and hashCode() for Performance, using the natural identity of the object */

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Performance that = (Performance) o;

        if (date != null ? !date.equals(that.date) : that.date != null)
            return false;
        if (show != null ? !show.equals(that.show) : that.show != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = date != null ? date.hashCode() : 0;
        result = 31 * result + (show != null ? show.hashCode() : 0);
        return result;
    }
    
    @Override
    public String toString() {
        return show + " on " + date.toString(); 
    }
}
