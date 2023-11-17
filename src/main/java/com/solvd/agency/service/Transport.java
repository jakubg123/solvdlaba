package com.solvd.agency.service;

import com.solvd.agency.interfaces.Displayable;
import com.solvd.agency.person.Customer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Transport implements Displayable {
    private static final Logger logger = LogManager.getLogger(Transport.class);

    private String name;
    private List<Customer> seats;
    private int availableSeats;


    public Transport(String name, int seatCount) {
        this.name = name;
        this.seats = new ArrayList<Customer>(Collections.nCopies(seatCount, null));
        this.availableSeats = seatCount;
    }


    public int getAvailableSeats() {
        return availableSeats;
    }

    public void bookOneSeat() {
        this.availableSeats--;
    }

    public String getName() {
        return name;
    }

    public List<Customer> getSeats() {
        return seats;
    }


    public void setName(String name) {
        this.name = name;
    }


    @Override
    public void displayInfo() {
        logger.info(this.toString());
    }

    public void addCustomer(Customer customer) {
        this.seats.add(customer);
    }

    @Override
    public String toString() {
        StringBuilder info = new StringBuilder("Transport " + name + ", availableSeats " + availableSeats);
        info.append(", Taken Seats=[");
        for (Customer seat : seats) {
            if (seat != null) {
                info.append(seat.toString()).append(", ");
            }
        }
        info.append("]");
        return info.toString();
    }


}