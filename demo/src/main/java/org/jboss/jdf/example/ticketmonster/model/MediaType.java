package org.jboss.jdf.example.ticketmonster.model;


/**
 * <p>
 * The {@link MediaType} describes the types of media this application can handle and render.
 * 
 * <p>
 * The media type is a <em>closed set</em> - as each different type of media requires support coded into the view layers, it
 * cannot be expanded upon without rebuilding the application. It is therefore represented by an enumeration. When used, you
 * should instruct JPA to store the enum value using it's String representation, so that we can later reorder the enum members,
 * without changing the data. Of course, this does mean we can't change the names of media items once the app is put into
 * production. To do this add <code>@Enumerated(STRING)</code> to the field declaration.
 * </p>
 * 
 * <p>
 * The {@link MediaType} also describes whether this type of media can be cached locally, and used when there is no internet
 * connection. For example images and or an mpeg video file can be cached, whilst a video streamed across the internet cannot.
 * </p>
 * 
 * @author Pete Muir
 * 
 */
public enum MediaType {

    /**
     * The types of media the application can currently handle. Right now, it can only handle images. We plan to add support for
     * streamed videos in the next development round.
     */
    IMAGE("Image", true);
    
    /**
     * A human readable description of the media type.
     */
    private final String description;
    
    /**
     * A boolean flag indicating whether the media type can be cached.
     */
    private final boolean cacheable;
    
    /* Boilerplate constructor and getters */

    private MediaType(String description, boolean cacheable) {
        this.description = description;
        this.cacheable = cacheable;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCacheable() {
        return cacheable;
    }

}
