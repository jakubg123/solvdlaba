package com.solvd.agency.place;


import com.solvd.agency.exceptions.ReservationException;
import com.solvd.agency.interfaces.Reservable;
import com.solvd.agency.interfaces.Reviewable;
import com.solvd.agency.service.Review;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Hotel implements Reservable, Reviewable  {
    private static final Logger logger = Logger.getLogger(Hotel.class.getName());

    private String name;
    private Set<Review> reviews;
    private Map<Integer, Boolean> roomReservationStatus;



    public Hotel(String name, int numberOfRooms) {
        this.name = name;
        this.roomReservationStatus = initializeRoomStatus(numberOfRooms);
        this.reviews = new HashSet<>();

    }

    public Hotel(String name)
    {
        this.name = name;
        this.reviews = new HashSet<>();
        this.roomReservationStatus = new HashMap<>();
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Map<Integer, Boolean> getRoomReservationStatus() {
        return roomReservationStatus;
    }
    public void setRoomReservationStatus(Map<Integer, Boolean> roomReservationStatus) {
        this.roomReservationStatus = roomReservationStatus;
    }



    @Override
    public String toString() {
        if(this.reviews.isEmpty())
        {
            return "Name: " + name + ", No reviews available, " ;
        }

        return "Name: " + name + "Reviews:" + reviews.toString() + ", ";
    }


    public Set<Review> getReviews() {
        return new HashSet<>(reviews);
    }

    @Override
    public float getAverageRating() {
        if (reviews.isEmpty()) return 0;
        float sum = 0;
        for (Review review : reviews) {
            sum += review.getRating();
        }
        return sum / reviews.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hotel)) return false;
        Hotel hotel = (Hotel) o;
        return name.equals(hotel.name) && reviews.equals(hotel.reviews);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name);
        result = 31 * result + reviews.hashCode();
        return result;
    }

    public boolean isRoomAvailable(int roomId) {
        return roomReservationStatus.containsKey(roomId) && !roomReservationStatus.get(roomId);
    }

    public Integer getRandomAvailableRoomId() {
        List<Integer> availableRoomIds = roomReservationStatus.entrySet().stream()
                .filter(entry -> !entry.getValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (availableRoomIds.isEmpty()) {
            return null;
        }

        Random random = new Random();
        return availableRoomIds.get(random.nextInt(availableRoomIds.size()));
    }

    public void addReview(Review review) throws Exception {
        if (review == null) {
            logger.warning("Attempted to add a null review");
            throw new Exception("Null");
        }
        reviews.add(review);
        logger.info("Review added successfully");
    }

    @Override
    public void reserve(int roomId) {
        if (!roomReservationStatus.containsKey(roomId)) {
            logger.warning("Room ID " + roomId + " does not exist.");
            return;
        }
        if (isRoomAvailable(roomId)) {
            roomReservationStatus.put(roomId, true);
            logger.info("Room " + roomId + " at " + name + " has been reserved.");
        } else {
            logger.info("Room " + roomId + " at " + name + " is already reserved.");
        }
    }

    @Override
    public void cancelReservation(int roomId) throws ReservationException {
        if (!roomReservationStatus.containsKey(roomId)) {
            logger.warning("Room " + roomId + " not found");
            throw new ReservationException("Room " + roomId + " not found");
        }
        if (roomReservationStatus.get(roomId)) {
            roomReservationStatus.put(roomId, false);
            logger.info("Reservation for room " + roomId + " at " + name + " has been cancelled.");
        } else {
            logger.info("Room " + roomId + " at " + name + " is not currently reserved.");
        }
    }

    private Map<Integer, Boolean> initializeRoomStatus(int numberOfRooms) {
        return IntStream.rangeClosed(1, numberOfRooms)
                .boxed()
                .collect(Collectors.toMap(roomNumber -> roomNumber, roomNumber -> false));
    }



}