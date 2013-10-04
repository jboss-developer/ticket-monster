package org.jboss.jdf.example.ticketmonster.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import org.jboss.jdf.example.ticketmonster.model.MediaItem;
import org.jboss.jdf.example.ticketmonster.rest.dto.MediaItemDTO;

/**
 * 
 */
@Stateless
@Path("/mediaitems")
public class MediaItemEndpoint
{
   @PersistenceContext(unitName = "primary")
   private EntityManager em;

   @POST
   @Consumes("application/json")
   public Response create(MediaItemDTO dto)
   {
      MediaItem entity = dto.fromDTO(null, em);
      em.persist(entity);
      return Response.created(UriBuilder.fromResource(MediaItemEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
   }

   @DELETE
   @Path("/{id:[0-9][0-9]*}")
   public Response deleteById(@PathParam("id") Long id)
   {
      MediaItem entity = em.find(MediaItem.class, id);
      if (entity == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      em.remove(entity);
      return Response.noContent().build();
   }

   @GET
   @Path("/{id:[0-9][0-9]*}")
   @Produces("application/json")
   public Response findById(@PathParam("id") Long id)
   {
      TypedQuery<MediaItem> findByIdQuery = em.createQuery("SELECT DISTINCT m FROM MediaItem m WHERE m.id = :entityId ORDER BY m.id", MediaItem.class);
      findByIdQuery.setParameter("entityId", id);
      MediaItem entity = findByIdQuery.getSingleResult();
      if (entity == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      MediaItemDTO dto = new MediaItemDTO(entity);
      return Response.ok(dto).build();
   }

   @GET
   @Produces("application/json")
   public List<MediaItemDTO> listAll()
   {
      final List<MediaItem> searchResults = em.createQuery("SELECT DISTINCT m FROM MediaItem m ORDER BY m.id", MediaItem.class).getResultList();
      final List<MediaItemDTO> results = new ArrayList<MediaItemDTO>();
      for (MediaItem searchResult : searchResults)
      {
         MediaItemDTO dto = new MediaItemDTO(searchResult);
         results.add(dto);
      }
      return results;
   }

   @PUT
   @Path("/{id:[0-9][0-9]*}")
   @Consumes("application/json")
   public Response update(@PathParam("id") Long id, MediaItemDTO dto)
   {
      TypedQuery<MediaItem> findByIdQuery = em.createQuery("SELECT DISTINCT m FROM MediaItem m WHERE m.id = :entityId ORDER BY m.id", MediaItem.class);
      findByIdQuery.setParameter("entityId", id);
      MediaItem entity = findByIdQuery.getSingleResult();
      entity = dto.fromDTO(entity, em);
      entity = em.merge(entity);
      return Response.noContent().build();
   }
}