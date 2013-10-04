package org.jboss.jdf.example.ticketmonster.rest.dto;

import java.io.Serializable;
import org.jboss.jdf.example.ticketmonster.model.Event;
import javax.persistence.EntityManager;
import org.jboss.jdf.example.ticketmonster.rest.dto.NestedEventCategoryDTO;
import org.jboss.jdf.example.ticketmonster.rest.dto.NestedMediaItemDTO;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EventDTO implements Serializable
{

   private Long id;
   private NestedEventCategoryDTO category;
   private NestedMediaItemDTO mediaItem;
   private String description;
   private String name;

   public EventDTO()
   {
   }

   public EventDTO(final Event entity)
   {
      if (entity != null)
      {
         this.id = entity.getId();
         this.category = new NestedEventCategoryDTO(entity.getCategory());
         this.mediaItem = new NestedMediaItemDTO(entity.getMediaItem());
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
      if (this.category != null)
      {
         entity.setCategory(this.category.fromDTO(entity.getCategory(), em));
      }
      if (this.mediaItem != null)
      {
         entity.setMediaItem(this.mediaItem.fromDTO(entity.getMediaItem(),
               em));
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

   public NestedEventCategoryDTO getCategory()
   {
      return this.category;
   }

   public void setCategory(final NestedEventCategoryDTO category)
   {
      this.category = category;
   }

   public NestedMediaItemDTO getMediaItem()
   {
      return this.mediaItem;
   }

   public void setMediaItem(final NestedMediaItemDTO mediaItem)
   {
      this.mediaItem = mediaItem;
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