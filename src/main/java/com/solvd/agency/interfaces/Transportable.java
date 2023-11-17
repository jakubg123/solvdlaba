package com.solvd.agency.interfaces;

import com.solvd.agency.other.Location;
import com.solvd.agency.service.Transport;

public interface Transportable {
    void getOnTransport(Transport transport, Location location, double price);

    void getOffTransport(Transport transport);
}
