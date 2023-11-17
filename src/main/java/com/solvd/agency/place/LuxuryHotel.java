package com.solvd.agency.place;

import com.solvd.agency.other.Location;
import com.solvd.agency.service.Review;

import java.util.*;

public class LuxuryHotel extends Hotel {

    private int starRating;
    private List<String> amenities;
    private Location location;

    public LuxuryHotel(String name, Location location, Map<Integer, Boolean> rooms, int starRating, List<String> amenities) {
        super(name, rooms);
        this.starRating = starRating;
        this.amenities = amenities;
    }


    public int getStarRating() {
        return starRating;
    }

    public void setStarRating(int starRating) {
        this.starRating = starRating;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("\nStar Rating: ").append(starRating);
        sb.append("\nAmenities: ").append(String.join(", ", amenities));
        return sb.toString();
    }
}