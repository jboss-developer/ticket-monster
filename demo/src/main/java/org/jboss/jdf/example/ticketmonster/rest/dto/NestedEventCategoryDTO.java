package org.jboss.jdf.example.ticketmonster.rest.dto;

import java.io.Serializable;
import org.jboss.jdf.example.ticketmonster.model.EventCategory;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class NestedEventCategoryDTO implements Serializable
{

   private Long id;
   private String description;

   public NestedEventCategoryDTO()
   {
   }

   public NestedEventCategoryDTO(final EventCategory entity)
   {
      if (entity != null)
      {
         this.id = entity.getId();
         this.description = entity.getDescription();
      }
   }

   public EventCategory fromDTO(EventCategory entity, EntityManager em)
   {
      if (entity == null)
      {
         entity = new EventCategory();
      }
      if (this.id != null)
      {
         TypedQuery<EventCategory> findByIdQuery = em
               .createQuery(
                     "SELECT DISTINCT e FROM EventCategory e WHERE e.id = :entityId",
                     EventCategory.class);
         findByIdQuery.setParameter("entityId", this.id);
         entity = findByIdQuery.getSingleResult();
         return entity;
      }
      entity.setDescription(this.description);
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

   public String getDescription()
   {
      return this.description;
   }

   public void setDescription(final String description)
   {
      this.description = description;
   }
}