package com.solvd.agency.service;

import com.solvd.agency.interfaces.Cleanable;
import com.solvd.agency.interfaces.Displayable;
import com.solvd.agency.person.Customer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Transport implements Displayable, Cleanable {
    private static final Logger logger = LogManager.getLogger(Transport.class);
    private static final Scanner scanner = new Scanner(System.in);
    private String name;
    private List<Customer> seats;
    private int availableSeats;
    private boolean isClean;


    public Transport(String name, int seatCount) {
        this.name = name;
        this.seats = new ArrayList<Customer>(Collections.nCopies(seatCount, null));
        this.availableSeats = seatCount;
        this.isClean = false;
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

    @Override
    public void clean() {
        LocalDate today = LocalDate.now();

        DayOfWeek dayOfWeek = today.getDayOfWeek();

        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            System.out.println("Cleaning the transport");
        }
        else {
            System.out.println("Cant clean today");
        }

    }


}