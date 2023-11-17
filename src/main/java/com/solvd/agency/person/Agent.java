package com.solvd.agency.person;


import com.solvd.agency.other.Location;
import com.solvd.agency.service.Transport;

import java.util.Objects;

public class Agent extends Person {


    private final int id;

    public Agent(int id, String name, String surname) {
        super(name, surname);
        this.id = id;
    }

    @Override
    public void setIdentifier(String identifier) {
        throw new UnsupportedOperationException("Agent ID can't be changed.");
    }

    @Override
    public String getIdentifier() {
        return String.valueOf(id);
    }

    @Override
    public String toString() {
        return getIdentifier() + " " + getName() + " " + getSurname();

    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o))
            return false;
        Agent agent = (Agent) o;
        return Objects.equals(getIdentifier(), agent.getIdentifier());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getIdentifier());
    }


    @Override
    public void getOnTransport(Transport transport, Location location, double price) {
        this.setBalance(getBalance() - price);
        System.out.println("My class is: " + getClass() + " I paid " + price + " for this transport");
    }

    @Override
    public void getOffTransport(Transport transport) {

        System.out.println("Agent out of " + transport.toString());

    }

    @Override
    public void displayInfo() {
        System.out.println("Name: " + getName() + ", Surname " + getSurname());
    }


}