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
import org.jboss.jdf.example.ticketmonster.model.Venue;
import org.jboss.jdf.example.ticketmonster.rest.dto.VenueDTO;

/**
 * 
 */
@Stateless
@Path("forge/venues")
public class VenueEndpoint
{
   @PersistenceContext(unitName = "primary")
   private EntityManager em;

   @POST
   @Consumes("application/json")
   public Response create(VenueDTO dto)
   {
      Venue entity = dto.fromDTO(null, em);
      em.persist(entity);
      return Response.created(UriBuilder.fromResource(VenueEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
   }

   @DELETE
   @Path("/{id:[0-9][0-9]*}")
   public Response deleteById(@PathParam("id") Long id)
   {
      Venue entity = em.find(Venue.class, id);
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
      TypedQuery<Venue> findByIdQuery = em.createQuery("SELECT DISTINCT v FROM Venue v LEFT JOIN FETCH v.sections LEFT JOIN FETCH v.mediaItem WHERE v.id = :entityId ORDER BY v.id", Venue.class);
      findByIdQuery.setParameter("entityId", id);
      Venue entity = findByIdQuery.getSingleResult();
      if (entity == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      VenueDTO dto = new VenueDTO(entity);
      return Response.ok(dto).build();
   }

   @GET
   @Produces("application/json")
   public List<VenueDTO> listAll()
   {
      final List<Venue> searchResults = em.createQuery("SELECT DISTINCT v FROM Venue v LEFT JOIN FETCH v.sections LEFT JOIN FETCH v.mediaItem ORDER BY v.id", Venue.class).getResultList();
      final List<VenueDTO> results = new ArrayList<VenueDTO>();
      for (Venue searchResult : searchResults)
      {
         VenueDTO dto = new VenueDTO(searchResult);
         results.add(dto);
      }
      return results;
   }

   @PUT
   @Path("/{id:[0-9][0-9]*}")
   @Consumes("application/json")
   public Response update(@PathParam("id") Long id, VenueDTO dto)
   {
      TypedQuery<Venue> findByIdQuery = em.createQuery("SELECT DISTINCT v FROM Venue v LEFT JOIN FETCH v.sections LEFT JOIN FETCH v.mediaItem WHERE v.id = :entityId ORDER BY v.id", Venue.class);
      findByIdQuery.setParameter("entityId", id);
      Venue entity = findByIdQuery.getSingleResult();
      entity = dto.fromDTO(entity, em);
      entity = em.merge(entity);
      return Response.noContent().build();
   }
}