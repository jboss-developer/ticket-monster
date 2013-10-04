package org.jboss.jdf.example.ticketmonster.rest.dto;

import java.io.Serializable;
import org.jboss.jdf.example.ticketmonster.model.Venue;
import javax.persistence.EntityManager;
import java.util.Set;
import java.util.HashSet;
import org.jboss.jdf.example.ticketmonster.rest.dto.NestedSectionDTO;
import org.jboss.jdf.example.ticketmonster.model.Section;
import java.util.Iterator;
import org.jboss.jdf.example.ticketmonster.rest.dto.AddressDTO;
import org.jboss.jdf.example.ticketmonster.rest.dto.NestedMediaItemDTO;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class VenueDTO implements Serializable
{

   private Long id;
   private Set<NestedSectionDTO> sections = new HashSet<NestedSectionDTO>();
   private AddressDTO address;
   private NestedMediaItemDTO mediaItem;
   private String description;
   private String name;
   private int capacity;

   public VenueDTO()
   {
   }

   public VenueDTO(final Venue entity)
   {
      if (entity != null)
      {
         this.id = entity.getId();
         Iterator<Section> iterSections = entity.getSections().iterator();
         for (; iterSections.hasNext();)
         {
            Section element = iterSections.next();
            this.sections.add(new NestedSectionDTO(element));
         }
         this.address = new AddressDTO(entity.getAddress());
         this.mediaItem = new NestedMediaItemDTO(entity.getMediaItem());
         this.description = entity.getDescription();
         this.name = entity.getName();
         this.capacity = entity.getCapacity();
      }
   }

   public Venue fromDTO(Venue entity, EntityManager em)
   {
      if (entity == null)
      {
         entity = new Venue();
      }
      Iterator<Section> iterSections = entity.getSections().iterator();
      for (; iterSections.hasNext();)
      {
         boolean found = false;
         Section section = iterSections.next();
         Iterator<NestedSectionDTO> iterDtoSections = this.getSections()
               .iterator();
         for (; iterDtoSections.hasNext();)
         {
            NestedSectionDTO dtoSection = iterDtoSections.next();
            if (dtoSection.getId().equals(section.getId()))
            {
               found = true;
               break;
            }
         }
         if (found == false)
         {
            iterSections.remove();
         }
      }
      Iterator<NestedSectionDTO> iterDtoSections = this.getSections()
            .iterator();
      for (; iterDtoSections.hasNext();)
      {
         boolean found = false;
         NestedSectionDTO dtoSection = iterDtoSections.next();
         iterSections = entity.getSections().iterator();
         for (; iterSections.hasNext();)
         {
            Section section = iterSections.next();
            if (dtoSection.getId().equals(section.getId()))
            {
               found = true;
               break;
            }
         }
         if (found == false)
         {
            Iterator<Section> resultIter = em
                  .createQuery("SELECT DISTINCT s FROM Section s",
                        Section.class).getResultList().iterator();
            for (; resultIter.hasNext();)
            {
               Section result = resultIter.next();
               if (result.getId().equals(dtoSection.getId()))
               {
                  entity.getSections().add(result);
                  break;
               }
            }
         }
      }
      if (this.address != null)
      {
         entity.setAddress(this.address.fromDTO(entity.getAddress(), em));
      }
      if (this.mediaItem != null)
      {
         entity.setMediaItem(this.mediaItem.fromDTO(entity.getMediaItem(),
               em));
      }
      entity.setDescription(this.description);
      entity.setName(this.name);
      entity.setCapacity(this.capacity);
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

   public Set<NestedSectionDTO> getSections()
   {
      return this.sections;
   }

   public void setSections(final Set<NestedSectionDTO> sections)
   {
      this.sections = sections;
   }

   public AddressDTO getAddress()
   {
      return this.address;
   }

   public void setAddress(final AddressDTO address)
   {
      this.address = address;
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

   public int getCapacity()
   {
      return this.capacity;
   }

   public void setCapacity(final int capacity)
   {
      this.capacity = capacity;
   }
}