package org.jboss.jdf.example.ticketmonster.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import org.jboss.jdf.example.ticketmonster.model.Booking;
import org.jboss.jdf.example.ticketmonster.model.Performance;
import org.jboss.jdf.example.ticketmonster.model.Section;
import org.jboss.jdf.example.ticketmonster.model.SectionAllocation;
import org.jboss.jdf.example.ticketmonster.model.Show;
import org.jboss.jdf.example.ticketmonster.rest.dto.PerformanceDTO;

/**
 * 
 */
@Stateless
@Path("/performances")
public class PerformanceEndpoint
{
   @PersistenceContext(unitName = "primary")
   private EntityManager em;

   @POST
   @Consumes("application/json")
   public Response create(PerformanceDTO dto)
   {
      Performance entity = dto.fromDTO(null, em);
      em.persist(entity);
      return Response.created(UriBuilder.fromResource(PerformanceEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
   }

   @DELETE
   @Path("/{id:[0-9][0-9]*}")
   public Response deleteById(@PathParam("id") Long id)
   {
      Performance entity = em.find(Performance.class, id);
      if (entity == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      Show show = entity.getShow();
      show.getPerformances().remove(entity);
      entity.setShow(null);
      this.em.merge(show);
      List<SectionAllocation> sectionAllocations = findSectionAllocationsByPerformance(entity);
      for(SectionAllocation sectionAllocation: sectionAllocations)
      {
         this.em.remove(sectionAllocation);
      }
      List<Booking> bookings = findBookingsByPerformance(entity);
      for(Booking booking: bookings)
      {
         this.em.remove(booking);
      }
      em.remove(entity);
      return Response.noContent().build();
   }

   @GET
   @Path("/{id:[0-9][0-9]*}")
   @Produces("application/json")
   public Response findById(@PathParam("id") Long id)
   {
      TypedQuery<Performance> findByIdQuery = em.createQuery("SELECT DISTINCT p FROM Performance p LEFT JOIN FETCH p.show WHERE p.id = :entityId ORDER BY p.id", Performance.class);
      findByIdQuery.setParameter("entityId", id);
      Performance entity = findByIdQuery.getSingleResult();
      if (entity == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      PerformanceDTO dto = new PerformanceDTO(entity);
      return Response.ok(dto).build();
   }

   @GET
   @Produces("application/json")
   public List<PerformanceDTO> listAll()
   {
      final List<Performance> searchResults = em.createQuery("SELECT DISTINCT p FROM Performance p LEFT JOIN FETCH p.show ORDER BY p.id", Performance.class).getResultList();
      final List<PerformanceDTO> results = new ArrayList<PerformanceDTO>();
      for (Performance searchResult : searchResults)
      {
         PerformanceDTO dto = new PerformanceDTO(searchResult);
         results.add(dto);
      }
      return results;
   }

   @PUT
   @Path("/{id:[0-9][0-9]*}")
   @Consumes("application/json")
   public Response update(@PathParam("id") Long id, PerformanceDTO dto)
   {
      TypedQuery<Performance> findByIdQuery = em.createQuery("SELECT DISTINCT p FROM Performance p LEFT JOIN FETCH p.show WHERE p.id = :entityId ORDER BY p.id", Performance.class);
      findByIdQuery.setParameter("entityId", id);
      Performance entity = findByIdQuery.getSingleResult();
      entity = dto.fromDTO(entity, em);
      entity = em.merge(entity);
      return Response.noContent().build();
   }

   public List<SectionAllocation> findSectionAllocationsByPerformance(Performance performance)
   {
      CriteriaQuery<SectionAllocation> criteria = this.em
            .getCriteriaBuilder().createQuery(SectionAllocation.class);
      Root<SectionAllocation> from = criteria.from(SectionAllocation.class);
      CriteriaBuilder builder = this.em.getCriteriaBuilder();
      Predicate performanceIsSame = builder.equal(from.get("performance"), performance);
      return this.em.createQuery(
            criteria.select(from).where(performanceIsSame)).getResultList();
   }

   public List<Booking> findBookingsByPerformance(Performance performance)
   {
      CriteriaQuery<Booking> criteria = this.em
            .getCriteriaBuilder().createQuery(Booking.class);
      Root<Booking> from = criteria.from(Booking.class);
      CriteriaBuilder builder = this.em.getCriteriaBuilder();
      Predicate performanceIsSame = builder.equal(from.get("performance"), performance);
      return this.em.createQuery(
            criteria.select(from).where(performanceIsSame)).getResultList();
   }

}