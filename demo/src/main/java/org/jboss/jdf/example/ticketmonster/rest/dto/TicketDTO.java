package org.jboss.jdf.example.ticketmonster.rest.dto;

import java.io.Serializable;
import org.jboss.jdf.example.ticketmonster.model.Ticket;
import javax.persistence.EntityManager;
import org.jboss.jdf.example.ticketmonster.rest.dto.NestedTicketCategoryDTO;
import org.jboss.jdf.example.ticketmonster.rest.dto.SeatDTO;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TicketDTO implements Serializable
{

   private Long id;
   private NestedTicketCategoryDTO ticketCategory;
   private float price;
   private SeatDTO seat;

   public TicketDTO()
   {
   }

   public TicketDTO(final Ticket entity)
   {
      if (entity != null)
      {
         this.id = entity.getId();
         this.ticketCategory = new NestedTicketCategoryDTO(
               entity.getTicketCategory());
         this.price = entity.getPrice();
         this.seat = new SeatDTO(entity.getSeat());
      }
   }

   public Ticket fromDTO(Ticket entity, EntityManager em)
   {
      if (entity == null)
      {
         entity = new Ticket();
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

   public NestedTicketCategoryDTO getTicketCategory()
   {
      return this.ticketCategory;
   }

   public void setTicketCategory(final NestedTicketCategoryDTO ticketCategory)
   {
      this.ticketCategory = ticketCategory;
   }

   public float getPrice()
   {
      return this.price;
   }

   public void setPrice(final float price)
   {
      this.price = price;
   }

   public SeatDTO getSeat()
   {
      return this.seat;
   }

   public void setSeat(final SeatDTO seat)
   {
      this.seat = seat;
   }
}