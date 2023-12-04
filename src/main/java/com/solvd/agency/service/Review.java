package com.solvd.agency.service;

import com.solvd.agency.interfaces.Displayable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.function.Supplier;


public class Review implements Displayable {
    private static final Logger logger = LogManager.getLogger(Review.class);
    private String name;
    private String reviewBody;
    private int rating;

    private static int reviewCount = 0;

    public Review(String name, String reviewBody, int rating) {
        this.name = name;
        this.reviewBody = reviewBody;
        this.rating = rating;
        reviewCount++;
    }

    public static void resetReviewCount() {
        reviewCount = 0;
        logger.info("Review count has been reset.");
    }

    public String getName() {
        return name;
    }

    public int getRating() {
        return rating;
    }

    public String getReviewBody() {
        return reviewBody;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setReviewBody(String reviewBody) {
        this.reviewBody = reviewBody;
    }

    @Override
    public String toString() {
        return "Title: " + this.name + "\nText: " + this.reviewBody + "\nRating: " + this.rating;
    }

    @Override
    public void displayInfo() {
        logger.info(this.toString());
    }


}