package com.solvd.agency.interfaces;

import com.solvd.agency.exceptions.ReservationException;

public interface Reservable {
    void reserve(int roomId);

    void cancelReservation(int roomId) throws ReservationException;
}