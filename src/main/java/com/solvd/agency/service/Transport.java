package com.solvd.agency.service;

import com.solvd.agency.enums.TransportType;
import com.solvd.agency.interfaces.Cleanable;
import com.solvd.agency.interfaces.Displayable;
import com.solvd.agency.person.Customer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Transport implements Displayable, Cleanable {
    private static final Logger logger = LogManager.getLogger(Transport.class);
    private static final Scanner scanner = new Scanner(System.in);
    private String name;
    private List<Customer> seats;
    private int availableSeats;
    private boolean isClean;
    private TransportType transportType;



    public Transport(int seatCount, TransportType transportType) {
        this.seats = new ArrayList<Customer>(Collections.nCopies(seatCount, null));
        this.availableSeats = seatCount;
        this.isClean = false;
        this.transportType = transportType;
    }

    public TransportType getTransportType() {
        return transportType;
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
        String takenSeats = seats.stream()
                .filter(Objects::nonNull)
                .map(Customer::toString)
                .collect(Collectors.joining(", "));

        return "AvailableSeats " + availableSeats + ", Taken Seats=[" + takenSeats + "]";
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