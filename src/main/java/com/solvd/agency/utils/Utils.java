package com.solvd.agency.utils;


import com.solvd.agency.Main;
import com.solvd.agency.exceptions.CustomerDataException;
import com.solvd.agency.exceptions.PaymentException;
import com.solvd.agency.interfaces.Displayable;
import com.solvd.agency.interfaces.Reviewable;
import com.solvd.agency.person.Customer;
import com.solvd.agency.person.Person;
import com.solvd.agency.place.Agency;
import com.solvd.agency.service.Travel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class Utils {
    public static final Logger logger = LogManager.getLogger(Utils.class.getName());


    public void printPersonDetails(Person person) {
        System.out.println("Person Details: " + person.toString());
        System.out.println("Identifier: " + person.getIdentifier());
    }

    public static void printRating(Reviewable reviewable) {
        System.out.println("My rating is: " + reviewable.getAverageRating());
    }

    public static String capitalizeFirstLetter(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }


    public static void bookTravelForCustomer(String phoneNumber, Agency agency) {

        if (agency.getCustomers().isEmpty()) {
            System.out.println("\nThere are no customers!\n");
            return;
        } else {
            System.out.print("Enter customer phone number: ");
            try {
                if (phoneNumber.length() != 9) {
                    throw new CustomerDataException("Incorrect phone number length.");
                }

                Customer customer = agency.findCustomerByPhoneNumber(phoneNumber);

                System.out.println("Booking transport for customer: " + customer.getName());
                System.out.println("Pass travel id");
                Optional<Travel> travelOptional = agency.getTravelById(scanner.nextInt());
                if (travelOptional.isPresent()) {
                    Travel travel = travelOptional.get();

                    if (travel.isSeatAvailable()) {
                        Period diff = Period.between(travel.getLocalDate(), LocalDate.now());
                        int diffDays = diff.getDays();
                        if (diffDays <= 3) {
                            System.out.println("Last minute discount!");
                            travel.applyDiscount(50);

                        }
                        if (customer.getBalance() < travel.getPrice())
                            throw new CustomerDataException("Get more balance bro, you can't book that flight");
                        travel.bookSeat(customer);
                        System.out.println("Transport booked successfully!");
                    }
                } else {
                    System.out.println("No seats available on the transport!");
                }

            } catch (CustomerDataException | PaymentException e) {
                System.out.println(e.getMessage());
            }
        }


    }

    public static void display(@NotNull List<? extends Displayable> displayables) {
        if (displayables.isEmpty()) {
            System.out.println("No displayables available.");
        } else {
            for (Displayable displayable : displayables) {
                displayable.displayInfo();
            }
        }
    }

    public static void printMenu() {
        System.out.println("Please choose an option:");
        System.out.println("1. Create Travel");
        System.out.println("2. Display all travels");
        System.out.println("3. Create Customer");
        System.out.println("4. Add balance");
        System.out.println("5. Get insurance");
        System.out.println("6. Create Agent");
        System.out.println("7. Book Transport for Customer");
        System.out.println("8. Print All Agents");
        System.out.println("9. Print All Customers");
        System.out.println("10. Exit");

        System.out.print("Enter choice: ");
    }

    public static void displayAllTravels(Agency agency) {
        System.out.println();
        if (agency.getTravels().isEmpty())
            System.out.println("No travels available\n");
        for (Travel travel : agency.getTravels()) {
            travel.displayInfo();
        }
        System.out.println();

    }


    public static void displayMap(Map<Integer, ?> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, ?> entry : map.entrySet()) {
            sb.append(entry.getKey()).append(" = ").append(entry.getValue().toString()).append("\n");
        }
        System.out.println(sb.toString());
    }

    public static boolean validateData(String data) throws CustomerDataException {
        if (data.matches("[a-zA-Z]+$"))
            return true;
        throw new CustomerDataException("\nInvalid data\n");
    }

    public static boolean validatePhoneNUmber(String data) throws CustomerDataException {
        if (data.length() != 9) {
            throw new CustomerDataException("Wrong number");
        }
        return true;
    }


    public static void readPhoneNumber(Scanner scanner) {
        System.out.println("Enter the phone number:");
        scanner.next();
    }


}
