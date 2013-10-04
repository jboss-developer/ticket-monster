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
import org.jboss.jdf.example.ticketmonster.model.Section;
import org.jboss.jdf.example.ticketmonster.rest.dto.SectionDTO;

/**
 * 
 */
@Stateless
@Path("/sections")
public class SectionEndpoint
{
   @PersistenceContext(unitName = "primary")
   private EntityManager em;

   @POST
   @Consumes("application/json")
   public Response create(SectionDTO dto)
   {
      Section entity = dto.fromDTO(null, em);
      em.persist(entity);
      return Response.created(UriBuilder.fromResource(SectionEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
   }

   @DELETE
   @Path("/{id:[0-9][0-9]*}")
   public Response deleteById(@PathParam("id") Long id)
   {
      Section entity = em.find(Section.class, id);
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
      TypedQuery<Section> findByIdQuery = em.createQuery("SELECT DISTINCT s FROM Section s LEFT JOIN FETCH s.venue WHERE s.id = :entityId ORDER BY s.id", Section.class);
      findByIdQuery.setParameter("entityId", id);
      Section entity = findByIdQuery.getSingleResult();
      if (entity == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      SectionDTO dto = new SectionDTO(entity);
      return Response.ok(dto).build();
   }

   @GET
   @Produces("application/json")
   public List<SectionDTO> listAll()
   {
      final List<Section> searchResults = em.createQuery("SELECT DISTINCT s FROM Section s LEFT JOIN FETCH s.venue ORDER BY s.id", Section.class).getResultList();
      final List<SectionDTO> results = new ArrayList<SectionDTO>();
      for (Section searchResult : searchResults)
      {
         SectionDTO dto = new SectionDTO(searchResult);
         results.add(dto);
      }
      return results;
   }

   @PUT
   @Path("/{id:[0-9][0-9]*}")
   @Consumes("application/json")
   public Response update(@PathParam("id") Long id, SectionDTO dto)
   {
      TypedQuery<Section> findByIdQuery = em.createQuery("SELECT DISTINCT s FROM Section s LEFT JOIN FETCH s.venue WHERE s.id = :entityId ORDER BY s.id", Section.class);
      findByIdQuery.setParameter("entityId", id);
      Section entity = findByIdQuery.getSingleResult();
      entity = dto.fromDTO(entity, em);
      entity = em.merge(entity);
      return Response.noContent().build();
   }
}