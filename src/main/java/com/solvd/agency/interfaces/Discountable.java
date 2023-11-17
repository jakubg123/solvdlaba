package com.solvd.agency.interfaces;


import com.solvd.agency.exceptions.PaymentException;

public interface Discountable {
    void applyDiscount(double percentage) throws PaymentException;

    double getDiscountedPrice();
}