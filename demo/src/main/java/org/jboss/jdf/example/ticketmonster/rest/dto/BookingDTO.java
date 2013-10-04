package org.jboss.jdf.example.ticketmonster.rest.dto;

import java.io.Serializable;
import org.jboss.jdf.example.ticketmonster.model.Booking;
import javax.persistence.EntityManager;
import java.util.Date;
import org.jboss.jdf.example.ticketmonster.rest.dto.NestedPerformanceDTO;
import java.util.Set;
import java.util.HashSet;
import org.jboss.jdf.example.ticketmonster.rest.dto.NestedTicketDTO;
import org.jboss.jdf.example.ticketmonster.model.Ticket;
import java.util.Iterator;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BookingDTO implements Serializable
{

   private Date createdOn;
   private Long id;
   private String cancellationCode;
   private String contactEmail;
   private NestedPerformanceDTO performance;
   private float totalTicketPrice;
   private Set<NestedTicketDTO> tickets = new HashSet<NestedTicketDTO>();

   public BookingDTO()
   {
   }

   public BookingDTO(final Booking entity)
   {
      if (entity != null)
      {
         this.createdOn = entity.getCreatedOn();
         this.id = entity.getId();
         this.cancellationCode = entity.getCancellationCode();
         this.contactEmail = entity.getContactEmail();
         this.performance = new NestedPerformanceDTO(entity.getPerformance());
         this.totalTicketPrice = entity.getTotalTicketPrice();
         Iterator<Ticket> iterTickets = entity.getTickets().iterator();
         for (; iterTickets.hasNext();)
         {
            Ticket element = iterTickets.next();
            this.tickets.add(new NestedTicketDTO(element));
         }
      }
   }

   public Booking fromDTO(Booking entity, EntityManager em)
   {
      if (entity == null)
      {
         entity = new Booking();
      }
      entity.setCreatedOn(this.createdOn);
      entity.setCancellationCode(this.cancellationCode);
      entity.setContactEmail(this.contactEmail);
      if (this.performance != null)
      {
         entity.setPerformance(this.performance.fromDTO(
               entity.getPerformance(), em));
      }
      Iterator<Ticket> iterTickets = entity.getTickets().iterator();
      for (; iterTickets.hasNext();)
      {
         boolean found = false;
         Ticket ticket = iterTickets.next();
         Iterator<NestedTicketDTO> iterDtoTickets = this.getTickets()
               .iterator();
         for (; iterDtoTickets.hasNext();)
         {
            NestedTicketDTO dtoTicket = iterDtoTickets.next();
            if (dtoTicket.getId().equals(ticket.getId()))
            {
               found = true;
               break;
            }
         }
         if (found == false)
         {
            iterTickets.remove();
         }
      }
      Iterator<NestedTicketDTO> iterDtoTickets = this.getTickets().iterator();
      for (; iterDtoTickets.hasNext();)
      {
         boolean found = false;
         NestedTicketDTO dtoTicket = iterDtoTickets.next();
         iterTickets = entity.getTickets().iterator();
         for (; iterTickets.hasNext();)
         {
            Ticket ticket = iterTickets.next();
            if (dtoTicket.getId().equals(ticket.getId()))
            {
               found = true;
               break;
            }
         }
         if (found == false)
         {
            Iterator<Ticket> resultIter = em
                  .createQuery("SELECT DISTINCT t FROM Ticket t",
                        Ticket.class).getResultList().iterator();
            for (; resultIter.hasNext();)
            {
               Ticket result = resultIter.next();
               if (result.getId().equals(dtoTicket.getId()))
               {
                  entity.getTickets().add(result);
                  break;
               }
            }
         }
      }
      entity = em.merge(entity);
      return entity;
   }

   public Date getCreatedOn()
   {
      return this.createdOn;
   }

   public void setCreatedOn(final Date createdOn)
   {
      this.createdOn = createdOn;
   }

   public Long getId()
   {
      return this.id;
   }

   public void setId(final Long id)
   {
      this.id = id;
   }

   public String getCancellationCode()
   {
      return this.cancellationCode;
   }

   public void setCancellationCode(final String cancellationCode)
   {
      this.cancellationCode = cancellationCode;
   }

   public String getContactEmail()
   {
      return this.contactEmail;
   }

   public void setContactEmail(final String contactEmail)
   {
      this.contactEmail = contactEmail;
   }

   public NestedPerformanceDTO getPerformance()
   {
      return this.performance;
   }

   public void setPerformance(final NestedPerformanceDTO performance)
   {
      this.performance = performance;
   }

   public float getTotalTicketPrice()
   {
      return this.totalTicketPrice;
   }

   public void setTotalTicketPrice(final float totalTicketPrice)
   {
      this.totalTicketPrice = totalTicketPrice;
   }

   public Set<NestedTicketDTO> getTickets()
   {
      return this.tickets;
   }

   public void setTickets(final Set<NestedTicketDTO> tickets)
   {
      this.tickets = tickets;
   }
}