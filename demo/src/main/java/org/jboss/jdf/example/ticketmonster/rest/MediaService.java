package org.jboss.jdf.example.ticketmonster.rest;

import java.io.File;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.jboss.jdf.example.ticketmonster.model.MediaItem;
import org.jboss.jdf.example.ticketmonster.service.MediaManager;

@Path("/media")
/**
 * <p>
 *     This is a stateless service, we declare it as an EJB for transaction demarcation
 * </p>
 */
public class MediaService {
    
    @Inject
    private MediaManager mediaManager;
    
    @Inject EntityManager entityManager;

    @GET
    @Path("/cache/{cachedFileName:\\S*}")
    @Produces("*/*")
    public File getCachedMediaContent(@PathParam("cachedFileName") String cachedFileName) {
        return mediaManager.getCachedFile(cachedFileName);
    }

    @GET
    @Path("/{id:\\d*}")
    @Produces("*/*")
    public File getMediaContent(@PathParam("id") Long id) {
        return mediaManager.getCachedFile(mediaManager.getPath(entityManager.find(MediaItem.class, id)).getUrl());
    }
}
