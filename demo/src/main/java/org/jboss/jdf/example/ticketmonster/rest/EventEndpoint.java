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
import org.jboss.jdf.example.ticketmonster.model.Event;
import org.jboss.jdf.example.ticketmonster.rest.dto.EventDTO;

/**
 * 
 */
@Stateless
@Path("forge/events")
public class EventEndpoint
{
   @PersistenceContext(unitName = "primary")
   private EntityManager em;

   @POST
   @Consumes("application/json")
   public Response create(EventDTO dto)
   {
      Event entity = dto.fromDTO(null, em);
      em.persist(entity);
      return Response.created(UriBuilder.fromResource(EventEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
   }

   @DELETE
   @Path("/{id:[0-9][0-9]*}")
   public Response deleteById(@PathParam("id") Long id)
   {
      Event entity = em.find(Event.class, id);
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
      TypedQuery<Event> findByIdQuery = em.createQuery("SELECT DISTINCT e FROM Event e LEFT JOIN FETCH e.mediaItem LEFT JOIN FETCH e.category WHERE e.id = :entityId ORDER BY e.id", Event.class);
      findByIdQuery.setParameter("entityId", id);
      Event entity = findByIdQuery.getSingleResult();
      if (entity == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      EventDTO dto = new EventDTO(entity);
      return Response.ok(dto).build();
   }

   @GET
   @Produces("application/json")
   public List<EventDTO> listAll()
   {
      final List<Event> searchResults = em.createQuery("SELECT DISTINCT e FROM Event e LEFT JOIN FETCH e.mediaItem LEFT JOIN FETCH e.category ORDER BY e.id", Event.class).getResultList();
      final List<EventDTO> results = new ArrayList<EventDTO>();
      for (Event searchResult : searchResults)
      {
         EventDTO dto = new EventDTO(searchResult);
         results.add(dto);
      }
      return results;
   }

   @PUT
   @Path("/{id:[0-9][0-9]*}")
   @Consumes("application/json")
   public Response update(@PathParam("id") Long id, EventDTO dto)
   {
      TypedQuery<Event> findByIdQuery = em.createQuery("SELECT DISTINCT e FROM Event e LEFT JOIN FETCH e.mediaItem LEFT JOIN FETCH e.category WHERE e.id = :entityId ORDER BY e.id", Event.class);
      findByIdQuery.setParameter("entityId", id);
      Event entity = findByIdQuery.getSingleResult();
      entity = dto.fromDTO(entity, em);
      entity = em.merge(entity);
      return Response.noContent().build();
   }
}