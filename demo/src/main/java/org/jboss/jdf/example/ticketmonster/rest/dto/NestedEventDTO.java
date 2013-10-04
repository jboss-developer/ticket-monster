package org.jboss.jdf.example.ticketmonster.rest.dto;

import java.io.Serializable;
import org.jboss.jdf.example.ticketmonster.model.Event;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class NestedEventDTO implements Serializable
{

   private Long id;
   private String description;
   private String name;

   public NestedEventDTO()
   {
   }

   public NestedEventDTO(final Event entity)
   {
      if (entity != null)
      {
         this.id = entity.getId();
         this.description = entity.getDescription();
         this.name = entity.getName();
      }
   }

   public Event fromDTO(Event entity, EntityManager em)
   {
      if (entity == null)
      {
         entity = new Event();
      }
      if (this.id != null)
      {
         TypedQuery<Event> findByIdQuery = em.createQuery(
               "SELECT DISTINCT e FROM Event e WHERE e.id = :entityId",
               Event.class);
         findByIdQuery.setParameter("entityId", this.id);
         entity = findByIdQuery.getSingleResult();
         return entity;
      }
      entity.setDescription(this.description);
      entity.setName(this.name);
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

   public String getName()
   {
      return this.name;
   }

   public void setName(final String name)
   {
      this.name = name;
   }
}