package com.solvd.agency.service;


import com.solvd.agency.exceptions.CustomerDataException;
import com.solvd.agency.exceptions.DestinationException;
import com.solvd.agency.exceptions.PaymentException;
import com.solvd.agency.interfaces.Discountable;
import com.solvd.agency.interfaces.Displayable;
import com.solvd.agency.other.Destination;
import com.solvd.agency.person.Customer;
import com.solvd.agency.place.Agency;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.Objects;
import java.util.stream.Collectors;

public class Travel implements Discountable, Displayable {

    private static final Logger logger = LogManager.getLogger(Travel.class);


    private int id;
    private Destination destination;
    private Transport transport;
    private LocalDate localDate;
    private double price;
    private double discountedPrice;

    private Agency agency;

    public Destination getDestination() {
        return destination;
    }


    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public double getPrice() {
        return price;
    }


    public void setPrice(double price) {
        this.price = price;
    }

    public void setDiscountedPrice(double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Travel(int id, Transport transport, Destination destination, LocalDate localDate, double price) throws CustomerDataException {

        if (!isValid(localDate)) {
            throw new CustomerDataException("The travel date is not valid.");
        }
        this.id = id;
        this.destination = destination;
        this.localDate = localDate;
        this.transport = transport;
        this.price = price;

    }


    public boolean isValid(LocalDate localDate) {
        boolean isValid = LocalDate.now().isBefore(localDate);
        if (!isValid) {
            logger.error("The travel date is not valid.");
        }
        return isValid;
    }

    @Override
    public void applyDiscount(double percentage) throws PaymentException {
        try {
            if (!((percentage >= 0) && (percentage <= 100))) {
                throw new PaymentException("Percentage could not be applied");
            }
        } catch (PaymentException e) {
            logger.error(e.getMessage());
        }
        double discountAmount = this.price * (percentage / 100.0);
        this.discountedPrice -= discountAmount;

    }


    @Override
    public double getDiscountedPrice() {
        return this.discountedPrice;
    }


    public void setDestination(Destination destination) throws DestinationException {
        if (destination == null) {
            logger.error("Invalid destination provided");
            throw new DestinationException("Invalid destination provided.");
        }
        this.destination = destination;
    }


    @Override
    public String toString() {
        return "Travel{ id: " + id +
                ", destination=" + destination +
                ", transport=" + transport +
                ", localDate=" + localDate +
                ", price=" + price +
                ", customers" + seatsToString() +
                '}';
    }

    public String seatsToString() {
        String notNullSeats = transport.getSeats().stream().filter(Objects::nonNull).map(Object::toString).collect(Collectors.joining(", "));
        return "Customers: " + notNullSeats;
    }


    @Override
    public void displayInfo() {
        logger.info(this.toString());
    }


    public void bookSeat(Customer customer) {
        for (int i = 0; i < this.transport.getSeats().size(); i++) {
            if (this.transport.getSeats().get(i) == null) {
                this.transport.getSeats().set(i, customer);
                this.transport.bookOneSeat();
                customer.setBalance(customer.getBalance() - price);
                return;
            }
        }
    }

    public boolean isSeatAvailable() {
        for (Customer seat : this.transport.getSeats()) {
            if (seat == null) {
                return true;
            }
        }
        return false;
    }


}