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
import org.jboss.jdf.example.ticketmonster.model.Booking;
import org.jboss.jdf.example.ticketmonster.rest.dto.BookingDTO;

/**
 * 
 */
@Stateless
@Path("forge/bookings")
public class BookingEndpoint
{
   @PersistenceContext(unitName = "primary")
   private EntityManager em;

   @POST
   @Consumes("application/json")
   public Response create(BookingDTO dto)
   {
      Booking entity = dto.fromDTO(null, em);
      em.persist(entity);
      return Response.created(UriBuilder.fromResource(BookingEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
   }

   @DELETE
   @Path("/{id:[0-9][0-9]*}")
   public Response deleteById(@PathParam("id") Long id)
   {
      Booking entity = em.find(Booking.class, id);
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
      TypedQuery<Booking> findByIdQuery = em.createQuery("SELECT DISTINCT b FROM Booking b LEFT JOIN FETCH b.tickets LEFT JOIN FETCH b.performance WHERE b.id = :entityId ORDER BY b.id", Booking.class);
      findByIdQuery.setParameter("entityId", id);
      Booking entity = findByIdQuery.getSingleResult();
      if (entity == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      BookingDTO dto = new BookingDTO(entity);
      return Response.ok(dto).build();
   }

   @GET
   @Produces("application/json")
   public List<BookingDTO> listAll()
   {
      final List<Booking> searchResults = em.createQuery("SELECT DISTINCT b FROM Booking b LEFT JOIN FETCH b.tickets LEFT JOIN FETCH b.performance ORDER BY b.id", Booking.class).getResultList();
      final List<BookingDTO> results = new ArrayList<BookingDTO>();
      for (Booking searchResult : searchResults)
      {
         BookingDTO dto = new BookingDTO(searchResult);
         results.add(dto);
      }
      return results;
   }

   @PUT
   @Path("/{id:[0-9][0-9]*}")
   @Consumes("application/json")
   public Response update(@PathParam("id") Long id, BookingDTO dto)
   {
      TypedQuery<Booking> findByIdQuery = em.createQuery("SELECT DISTINCT b FROM Booking b LEFT JOIN FETCH b.tickets LEFT JOIN FETCH b.performance WHERE b.id = :entityId ORDER BY b.id", Booking.class);
      findByIdQuery.setParameter("entityId", id);
      Booking entity = findByIdQuery.getSingleResult();
      entity = dto.fromDTO(entity, em);
      entity = em.merge(entity);
      return Response.noContent().build();
   }
}