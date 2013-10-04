package org.jboss.jdf.example.ticketmonster.rest.dto;

import java.io.Serializable;
import org.jboss.jdf.example.ticketmonster.model.Show;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class NestedShowDTO implements Serializable
{

   private Long id;
   private String displayTitle;

   public NestedShowDTO()
   {
   }

   public NestedShowDTO(final Show entity)
   {
      if (entity != null)
      {
         this.id = entity.getId();
         this.displayTitle = entity.toString();
      }
   }

   public Show fromDTO(Show entity, EntityManager em)
   {
      if (entity == null)
      {
         entity = new Show();
      }
      if (this.id != null)
      {
         TypedQuery<Show> findByIdQuery = em.createQuery(
               "SELECT DISTINCT s FROM Show s WHERE s.id = :entityId",
               Show.class);
         findByIdQuery.setParameter("entityId", this.id);
         entity = findByIdQuery.getSingleResult();
         return entity;
      }
      entity = em.merge(entity);
      return entity;
   }

   public Long getId()
   {
      return this.id;
   }

   public void setId(final Long id)
   {
      this.id = id;
   }

   public String getDisplayTitle()
   {
      return this.displayTitle;
   }
}