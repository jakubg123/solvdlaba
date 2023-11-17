package com.solvd.agency.person;


import com.solvd.agency.interfaces.Displayable;
import com.solvd.agency.interfaces.Transportable;
import com.solvd.agency.service.Transport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public abstract class Person implements Displayable {

    private String name;
    private String surname;
    private Transport transport;



    private double balance;

    public Person(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    @Override
    public String toString() {
        return name + " " + surname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person))
            return false;
        Person person = (Person) o;
        return Objects.equals(getName(), person.getName()) &&
                Objects.equals(getSurname(), person.getSurname());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getSurname());
    }

    public abstract String getIdentifier();

    public abstract void setIdentifier(String identifier);

}