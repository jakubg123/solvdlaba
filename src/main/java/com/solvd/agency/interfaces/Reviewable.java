package com.solvd.agency.interfaces;

import com.solvd.agency.service.Review;

public interface Reviewable {
    void addReview(Review review) throws Exception;

    float getAverageRating();
}