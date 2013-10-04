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
import org.jboss.jdf.example.ticketmonster.model.TicketPrice;
import org.jboss.jdf.example.ticketmonster.rest.dto.TicketPriceDTO;

/**
 * 
 */
@Stateless
@Path("/ticketprices")
public class TicketPriceEndpoint
{
   @PersistenceContext(unitName = "primary")
   private EntityManager em;

   @POST
   @Consumes("application/json")
   public Response create(TicketPriceDTO dto)
   {
      TicketPrice entity = dto.fromDTO(null, em);
      em.persist(entity);
      return Response.created(UriBuilder.fromResource(TicketPriceEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
   }

   @DELETE
   @Path("/{id:[0-9][0-9]*}")
   public Response deleteById(@PathParam("id") Long id)
   {
      TicketPrice entity = em.find(TicketPrice.class, id);
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
      TypedQuery<TicketPrice> findByIdQuery = em.createQuery("SELECT DISTINCT t FROM TicketPrice t LEFT JOIN FETCH t.show LEFT JOIN FETCH t.section LEFT JOIN FETCH t.ticketCategory WHERE t.id = :entityId ORDER BY t.id", TicketPrice.class);
      findByIdQuery.setParameter("entityId", id);
      TicketPrice entity = findByIdQuery.getSingleResult();
      if (entity == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      TicketPriceDTO dto = new TicketPriceDTO(entity);
      return Response.ok(dto).build();
   }

   @GET
   @Produces("application/json")
   public List<TicketPriceDTO> listAll()
   {
      final List<TicketPrice> searchResults = em.createQuery("SELECT DISTINCT t FROM TicketPrice t LEFT JOIN FETCH t.show LEFT JOIN FETCH t.section LEFT JOIN FETCH t.ticketCategory ORDER BY t.id", TicketPrice.class).getResultList();
      final List<TicketPriceDTO> results = new ArrayList<TicketPriceDTO>();
      for (TicketPrice searchResult : searchResults)
      {
         TicketPriceDTO dto = new TicketPriceDTO(searchResult);
         results.add(dto);
      }
      return results;
   }

   @PUT
   @Path("/{id:[0-9][0-9]*}")
   @Consumes("application/json")
   public Response update(@PathParam("id") Long id, TicketPriceDTO dto)
   {
      TypedQuery<TicketPrice> findByIdQuery = em.createQuery("SELECT DISTINCT t FROM TicketPrice t LEFT JOIN FETCH t.show LEFT JOIN FETCH t.section LEFT JOIN FETCH t.ticketCategory WHERE t.id = :entityId ORDER BY t.id", TicketPrice.class);
      findByIdQuery.setParameter("entityId", id);
      TicketPrice entity = findByIdQuery.getSingleResult();
      entity = dto.fromDTO(entity, em);
      entity = em.merge(entity);
      return Response.noContent().build();
   }
}