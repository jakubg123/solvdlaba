package com.solvd.agency.interfaces;

import com.solvd.agency.exceptions.ReservationException;

public interface Reservable {
    boolean reserve(int roomId);

    boolean cancelReservation(int roomId) throws ReservationException;
}