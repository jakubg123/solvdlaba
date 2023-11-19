package com.solvd.agency.interfaces;

import com.solvd.agency.exceptions.ReservationException;
import com.solvd.agency.person.Customer;

public interface Reservable {
    boolean reserve(int roomId);

    boolean cancelReservation(int roomId) throws ReservationException;
}