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
import org.jboss.jdf.example.ticketmonster.model.Show;
import org.jboss.jdf.example.ticketmonster.rest.dto.ShowDTO;

/**
 * 
 */
@Stateless
@Path("forge/shows")
public class ShowEndpoint
{
   @PersistenceContext(unitName = "primary")
   private EntityManager em;

   @POST
   @Consumes("application/json")
   public Response create(ShowDTO dto)
   {
      Show entity = dto.fromDTO(null, em);
      em.persist(entity);
      return Response.created(UriBuilder.fromResource(ShowEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
   }

   @DELETE
   @Path("/{id:[0-9][0-9]*}")
   public Response deleteById(@PathParam("id") Long id)
   {
      Show entity = em.find(Show.class, id);
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
      TypedQuery<Show> findByIdQuery = em.createQuery("SELECT DISTINCT s FROM Show s LEFT JOIN FETCH s.event LEFT JOIN FETCH s.venue LEFT JOIN FETCH s.performances LEFT JOIN FETCH s.ticketPrices WHERE s.id = :entityId ORDER BY s.id", Show.class);
      findByIdQuery.setParameter("entityId", id);
      Show entity = findByIdQuery.getSingleResult();
      if (entity == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      ShowDTO dto = new ShowDTO(entity);
      return Response.ok(dto).build();
   }

   @GET
   @Produces("application/json")
   public List<ShowDTO> listAll()
   {
      final List<Show> searchResults = em.createQuery("SELECT DISTINCT s FROM Show s LEFT JOIN FETCH s.event LEFT JOIN FETCH s.venue LEFT JOIN FETCH s.performances LEFT JOIN FETCH s.ticketPrices ORDER BY s.id", Show.class).getResultList();
      final List<ShowDTO> results = new ArrayList<ShowDTO>();
      for (Show searchResult : searchResults)
      {
         ShowDTO dto = new ShowDTO(searchResult);
         results.add(dto);
      }
      return results;
   }

   @PUT
   @Path("/{id:[0-9][0-9]*}")
   @Consumes("application/json")
   public Response update(@PathParam("id") Long id, ShowDTO dto)
   {
      TypedQuery<Show> findByIdQuery = em.createQuery("SELECT DISTINCT s FROM Show s LEFT JOIN FETCH s.event LEFT JOIN FETCH s.venue LEFT JOIN FETCH s.performances LEFT JOIN FETCH s.ticketPrices WHERE s.id = :entityId ORDER BY s.id", Show.class);
      findByIdQuery.setParameter("entityId", id);
      Show entity = findByIdQuery.getSingleResult();
      entity = dto.fromDTO(entity, em);
      entity = em.merge(entity);
      return Response.noContent().build();
   }
}