package org.jboss.jdf.example.ticketmonster.rest.dto;

import java.io.Serializable;
import org.jboss.jdf.example.ticketmonster.model.TicketCategory;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class NestedTicketCategoryDTO implements Serializable
{

   private Long id;
   private String description;

   public NestedTicketCategoryDTO()
   {
   }

   public NestedTicketCategoryDTO(final TicketCategory entity)
   {
      if (entity != null)
      {
         this.id = entity.getId();
         this.description = entity.getDescription();
      }
   }

   public TicketCategory fromDTO(TicketCategory entity, EntityManager em)
   {
      if (entity == null)
      {
         entity = new TicketCategory();
      }
      if (this.id != null)
      {
         TypedQuery<TicketCategory> findByIdQuery = em
               .createQuery(
                     "SELECT DISTINCT t FROM TicketCategory t WHERE t.id = :entityId",
                     TicketCategory.class);
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