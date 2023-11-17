package com.solvd.agency.service;

import com.solvd.agency.interfaces.Discountable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.Objects;

public class Insurance implements Discountable {
    private static final Logger logger = LogManager.getLogger(Insurance.class);

    private String insuranceNumber;
    private LocalDate startDate;
    private LocalDate endDate;
    private double price;
    private double discountedPrice;

    public Insurance(String insuranceNumber, LocalDate startDate, LocalDate endDate, double price) {
        this.insuranceNumber = insuranceNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
    }

    public String getInsuranceNumber() {
        return insuranceNumber;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setInsuranceNumber(String insuranceNumber) {
        this.insuranceNumber = insuranceNumber;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
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

    public boolean isPolicyActive() {
        LocalDate now = LocalDate.now();
        return (now.isAfter(startDate) && now.isBefore(endDate));
    }

    @Override
    public String toString() {
        return insuranceNumber + " " + startDate + " - " + endDate;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Insurance insurance = (Insurance) o;
        return Objects.equals(this.insuranceNumber, insurance.insuranceNumber) &&
                Objects.equals(this.startDate, insurance.startDate) &&
                Objects.equals(this.endDate, insurance.endDate);

    }

    @Override
    public int hashCode() {
        return Objects.hash(insuranceNumber, startDate, endDate);
    }

    @Override
    public void applyDiscount(double percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Discount % must be lower than 100 and higher than 0.");
        }
        discountedPrice = (percentage / 100) * price;
    }

    @Override
    public double getDiscountedPrice() {
        return this.discountedPrice;
    }

    public void logInsuranceInfo() {
        logger.info("Insurance Number: " + insuranceNumber);
        logger.info("Start Date: " + startDate);
        logger.info("End Date: " + endDate);
        logger.info("Price: " + price);
        logger.info("Discounted Price: " + discountedPrice);
    }
}