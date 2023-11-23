package com.solvd.agency.service;


import com.solvd.agency.enums.TravelStatus;
import com.solvd.agency.exceptions.CustomerDataException;
import com.solvd.agency.exceptions.DestinationException;
import com.solvd.agency.exceptions.PaymentException;
import com.solvd.agency.interfaces.Discountable;
import com.solvd.agency.interfaces.Displayable;
import com.solvd.agency.other.Destination;
import com.solvd.agency.person.Customer;
import com.solvd.agency.place.Agency;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Travel implements Discountable, Displayable {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Logger logger = LogManager.getLogger(Travel.class);


    private int id;
    private Destination destination;
    private Transport transport;
    private LocalDate localDate;

    private LocalDate endDate;
    private double price;
    private double discountedPrice;



    private TravelStatus status;

    private Agency agency;



    public Travel(int id, Transport transport, Destination destination, LocalDate localDate, LocalDate endDate, double price) throws CustomerDataException {

        if (!isValid(localDate, endDate)) {
            throw new CustomerDataException("The travel date is not valid.");
        }
        this.id = id;
        this.destination = destination;
        this.localDate = localDate;
        this.endDate = endDate;
        this.transport = transport;
        this.price = price;
        this.discountedPrice = price;
        setStatus();

    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public TravelStatus getStatus() {
        return status;
    }

    public void setStatus() {
        if(this.localDate.isBefore(LocalDate.now()) && this.endDate.isBefore(LocalDate.now())){
            this.status = TravelStatus.FINISHED;
        }
        else if(this.localDate.isBefore(LocalDate.now()) && this.endDate.isAfter(LocalDate.now())){
            this.status = TravelStatus.IN_PROGRESS;
        }
        else
        {
            this.status = TravelStatus.NOT_STARTED;
        }
    }

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


    public boolean isValid(LocalDate localDate, LocalDate localDate2) {
        boolean isValid = localDate.isBefore(localDate2);
        if (!isValid) {
            logger.error(StringUtils.capitalize("The travel date is not valid."));
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
            logger.error(StringUtils.abbreviate("Invalid destination provided",20));
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
                ", end date=" + endDate +
                ", price=" + price +
                ", status=" + status +
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


    public void bookSeat(Customer customer, boolean discount) {
        for (int i = 0; i < this.transport.getSeats().size(); i++) {
            if (this.transport.getSeats().get(i) == null) {
                this.transport.getSeats().set(i, customer);
                this.transport.bookOneSeat();
                if(discount) {
                    customer.setBalance(customer.getBalance() - discountedPrice);
                    return;
                }
                else{
                    customer.setBalance((customer.getBalance() - price));
                }

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
