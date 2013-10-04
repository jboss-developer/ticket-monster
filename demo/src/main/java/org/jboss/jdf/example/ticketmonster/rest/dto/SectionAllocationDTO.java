package org.jboss.jdf.example.ticketmonster.rest.dto;

import java.io.Serializable;
import org.jboss.jdf.example.ticketmonster.model.SectionAllocation;
import javax.persistence.EntityManager;
import org.jboss.jdf.example.ticketmonster.rest.dto.NestedPerformanceDTO;
import org.jboss.jdf.example.ticketmonster.rest.dto.NestedSectionDTO;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SectionAllocationDTO implements Serializable
{

   private Long id;
   private int occupiedCount;
   private NestedPerformanceDTO performance;
   private NestedSectionDTO section;

   public SectionAllocationDTO()
   {
   }

   public SectionAllocationDTO(final SectionAllocation entity)
   {
      if (entity != null)
      {
         this.id = entity.getId();
         this.occupiedCount = entity.getOccupiedCount();
         this.performance = new NestedPerformanceDTO(entity.getPerformance());
         this.section = new NestedSectionDTO(entity.getSection());
      }
   }

   public SectionAllocation fromDTO(SectionAllocation entity, EntityManager em)
   {
      if (entity == null)
      {
         entity = new SectionAllocation();
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

   public int getOccupiedCount()
   {
      return this.occupiedCount;
   }

   public void setOccupiedCount(final int occupiedCount)
   {
      this.occupiedCount = occupiedCount;
   }

   public NestedPerformanceDTO getPerformance()
   {
      return this.performance;
   }

   public void setPerformance(final NestedPerformanceDTO performance)
   {
      this.performance = performance;
   }

   public NestedSectionDTO getSection()
   {
      return this.section;
   }

   public void setSection(final NestedSectionDTO section)
   {
      this.section = section;
   }
}