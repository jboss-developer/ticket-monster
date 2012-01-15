package org.jboss.jdf.example.ticketmonster.action;

import java.io.Serializable;
import java.util.Date;

import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.jdf.example.ticketmonster.model.Document;
import org.jboss.jdf.example.ticketmonster.model.Event;

/**
 * Event management related operations
 * 
 * @author Shane Bryzak
 *
 */
public @Stateful @Named @ConversationScoped class EventAction implements Serializable
{
   private static final long serialVersionUID = 1735221845795268934L;
   
   @PersistenceContext EntityManager entityManager;
   @Inject Conversation conversation;
   /*@Inject @RequestParam("eventId")*/ String eventId;
   
   private Event event;
   private String description;
   
   public void createEvent()
   {
      conversation.begin();
      event = new Event();      
   }
   
   public boolean isLoadEvent()
   {
      if (event == null && eventId != null && conversation.isTransient())
      {      
         conversation.begin();      
         event = entityManager.find(Event.class, Long.valueOf(eventId));
         description = event.getDescription().getContent();
      }
      
      return false;
   }   
   
   public String save()
   {
      if (event.getId() != null)
      {
         entityManager.merge(event);
         
         if (!description.equals(event.getDescription().getContent()))
         {
            event.getDescription().setContent(description);
            entityManager.merge(event.getDescription());            
         }
      }
      else
      {
         Document doc = new Document();
         event.setDescription(doc);         
         
         if (description != null)
         {
            doc.setContent(description);
            entityManager.persist(doc);
         }
         else
         {
            entityManager.persist(doc);
         }
              
         entityManager.persist(event);

      }
      
      conversation.end();
      return "success";
   }
   
   public String cancel()
   {
      conversation.end();
      return "cancel";
   }   

   public Event getEvent()
   {
      return event;
   }
   
   public String getDescription()
   {
      return description;
   }
   
   public void setDescription(String description)
   {
      this.description = description;
   }
}
