package com.solvd.agency.other;


import com.solvd.agency.place.Hotel;

public class Destination {
    private Location location;
    private Hotel hotel;

    public Destination(Location location, Hotel hotel) {
        this.location = location;
        this.hotel = hotel;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    @Override
    public String toString() {
        return hotel.toString() + " " + location.toString();
    }

}