package com.solvd.agency.other;

import java.util.Objects;

public class Location {

    private String street;
    private String city;
    private String country;

    public Location(String street, String city, String country) {

        this.street = street;
        this.city = city;
        this.country = country;

    }

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

    @Override
    public String toString() {
        return this.country + " " + this.city + " " + this.street;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Location location = (Location) o;
        return Objects.equals(this.country, ((Location) o).country) &&
                Objects.equals(this.city, ((Location) o).city) &&
                Objects.equals(this.street, ((Location) o).street);

    }

    @Override
    public int hashCode() {
        return Objects.hash(country, city, street);

    }


}
