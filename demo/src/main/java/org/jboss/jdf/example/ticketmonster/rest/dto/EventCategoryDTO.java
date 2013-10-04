package org.jboss.jdf.example.ticketmonster.rest.dto;

import java.io.Serializable;
import org.jboss.jdf.example.ticketmonster.model.EventCategory;
import javax.persistence.EntityManager;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EventCategoryDTO implements Serializable
{

   private Long id;
   private String description;

   public EventCategoryDTO()
   {
   }

   public EventCategoryDTO(final EventCategory entity)
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