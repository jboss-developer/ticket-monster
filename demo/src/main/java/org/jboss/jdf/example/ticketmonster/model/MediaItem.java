package org.jboss.jdf.example.ticketmonster.model;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.validator.constraints.URL;

/**
 * <p>
 * A reference to a media object such as images, sound bites, video recordings, that can be used in the application.
 * </p>
 * 
 * <p>
 * A media item contains the type of the media, which is required to render it correctly, as well as the URL at which the media
 * should be sourced.
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
public class MediaItem implements Serializable {

    /* Declaration of fields */

    /**
     * The synthetic id of the object.
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    /**
     * <p>
     * The type of the media, required to render the media item correctly.
     * </p>
     * 
     * <p>
     * The media type is a <em>closed set</em> - as each different type of media requires support coded into the view layers, it
     * cannot be expanded upon without rebuilding the application. It is therefore represented by an enumeration. We instruct
     * JPA to store the enum value using it's String representation, so that we can later reorder the enum members, without
     * changing the data. Of course, this does mean we can't change the names of media items once the app is put into
     * production.
     * </p>
     */
    @Enumerated(STRING)
    private MediaType mediaType;

    /**
     * <p>
     * The URL from which the media item can be sourced
     * </p>
     * 
     * <p>
     * The url of the media item forms it's natural id and cannot be shared between event categories
     * </p>
     * 
     * <p>
     * The <code>@URL<code> Bean Validation ensures the the URL is, indeed, a valid URL.
     * </p>
     */
    @Column(unique = true)
    @URL
    private String url;

    /* Boilerplate getters and setters */

    public Long getId() {
        return id;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /* toString(), equals() and hashCode() for MediaItem, using the natural identity of the object */

    @Override
    public String toString() {
        return "[" + mediaType.getDescription() + "] " + url;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MediaItem other = (MediaItem) obj;
        if (url == null) {
            if (other.url != null)
                return false;
        } else if (!url.equals(other.url))
            return false;
        return true;
    }

}
