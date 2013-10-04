package org.jboss.jdf.example.ticketmonster.rest.dto;

import java.io.Serializable;
import org.jboss.jdf.example.ticketmonster.model.Section;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class NestedSectionDTO implements Serializable
{

   private Long id;
   private String description;
   private int rowCapacity;
   private String name;
   private int capacity;
   private int numberOfRows;

   public NestedSectionDTO()
   {
   }

   public NestedSectionDTO(final Section entity)
   {
      if (entity != null)
      {
         this.id = entity.getId();
         this.description = entity.getDescription();
         this.rowCapacity = entity.getRowCapacity();
         this.name = entity.getName();
         this.capacity = entity.getCapacity();
         this.numberOfRows = entity.getNumberOfRows();
      }
   }

   public Section fromDTO(Section entity, EntityManager em)
   {
      if (entity == null)
      {
         entity = new Section();
      }
      if (this.id != null)
      {
         TypedQuery<Section> findByIdQuery = em.createQuery(
               "SELECT DISTINCT s FROM Section s WHERE s.id = :entityId",
               Section.class);
         findByIdQuery.setParameter("entityId", this.id);
         entity = findByIdQuery.getSingleResult();
         return entity;
      }
      entity.setDescription(this.description);
      entity.setRowCapacity(this.rowCapacity);
      entity.setName(this.name);
      entity.setNumberOfRows(this.numberOfRows);
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

   public int getRowCapacity()
   {
      return this.rowCapacity;
   }

   public void setRowCapacity(final int rowCapacity)
   {
      this.rowCapacity = rowCapacity;
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

   public int getNumberOfRows()
   {
      return this.numberOfRows;
   }

   public void setNumberOfRows(final int numberOfRows)
   {
      this.numberOfRows = numberOfRows;
   }
}