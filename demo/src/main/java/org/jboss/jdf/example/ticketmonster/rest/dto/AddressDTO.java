package org.jboss.jdf.example.ticketmonster.rest.dto;

import java.io.Serializable;
import org.jboss.jdf.example.ticketmonster.model.Address;
import javax.persistence.EntityManager;

public class AddressDTO implements Serializable
{

   private String street;
   private String country;
   private String city;

   public AddressDTO()
   {
   }

   public AddressDTO(final Address entity)
   {
      if (entity != null)
      {
         this.street = entity.getStreet();
         this.country = entity.getCountry();
         this.city = entity.getCity();
      }
   }

   public Address fromDTO(Address entity, EntityManager em)
   {
      if (entity == null)
      {
         entity = new Address();
      }
      entity.setStreet(this.street);
      entity.setCountry(this.country);
      entity.setCity(this.city);
      return entity;
   }

   public String getStreet()
   {
      return this.street;
   }

   public void setStreet(final String street)
   {
      this.street = street;
   }

   public String getCountry()
   {
      return this.country;
   }

   public void setCountry(final String country)
   {
      this.country = country;
   }

   public String getCity()
   {
      return this.city;
   }

   public void setCity(final String city)
   {
      this.city = city;
   }
}