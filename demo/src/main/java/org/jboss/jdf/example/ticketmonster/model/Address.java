package org.jboss.jdf.example.ticketmonster.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

/**
 * <p>
 * A reusable representation of an address.
 * </p>
 * 
 * <p>
 * Addresses are used in many places in an application, so to observe the DRY principle, we model Address as an embeddable
 * entity. An embeddable entity appears as a child in the object model, but no relationship is established in the RDBMS..
 * </p>
 * 
 * @author Marius Bogoevici
 * @author Pete Muir
 */
@SuppressWarnings("serial")
@Embeddable
public class Address implements Serializable {

    /* Declaration of fields */
    private String street;
    private String city;
    private String country;
    
    /* Declaration of boilerplate getters and setters */

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    /* toString(), equals() and hashCode() for Address, using the natural identity of the object */
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Address address = (Address) o;

        if (city != null ? !city.equals(address.city) : address.city != null)
            return false;
        if (country != null ? !country.equals(address.country) : address.country != null)
            return false;
        if (street != null ? !street.equals(address.street) : address.street != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = street != null ? street.hashCode() : 0;
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return street + ", " + city + ", " + country;
    }
}
