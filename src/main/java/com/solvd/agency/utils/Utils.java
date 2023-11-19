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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

public class Utils {
    public static final Logger logger = LogManager.getLogger(Utils.class.getName());
    private static final Scanner scanner = new Scanner(System.in);

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
        logger.info("Please choose an option:");
        logger.info("1. Display agents");
        logger.info("2. Display customers");
        logger.info("3. Display agency travels");
        logger.info("4. Register Customer");
        logger.info("5. Register Agent");
        logger.info("6. Customer add balance");
        logger.info("7. Customer get insurance");
        logger.info("8. Book travel for customer");
        logger.info("9. Add travel to agencys offer");
        logger.info("10. Exit");
        logger.info("Enter choice: ");
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
        if (data.length() != 9 || !data.matches("\\d{9}")) {
            throw new CustomerDataException("Wrong number");
        }
        return true;
    }



    public static String readPhoneNumber() {
        String phoneNumber = null;
        boolean isValidNumber = false;

        while (!isValidNumber) {
            try {
                logger.info("Enter the phone number:");
                phoneNumber = scanner.nextLine();
                validatePhoneNUmber(phoneNumber);
                isValidNumber = true;
            } catch (CustomerDataException e) {
                logger.warn(e.getMessage() + " Please try again.");
            }
        }

        return phoneNumber;
    }


    public static void bookTravelForCustomer( Agency agency) throws CustomerDataException {
        if (agency.getCustomers().isEmpty()) {
            logger.info("\nThere are no customers!\n");
            return;
        }
        String phoneNumber = readPhoneNumber();
        validatePhoneNUmber(phoneNumber);
        try {
            Customer customer = agency.findCustomerByPhoneNumber(phoneNumber);
            logger.info("Pass travel id");
            int id = scanner.nextInt();
            Travel travel = selectTravel(id, agency);

            Integer roomId = travel.getDestination().getHotel().getRandomAvailableRoomId();
            if (roomId == null) {
                logger.info("No available rooms to reserve.");
                return;
            }
            travel.getDestination().getHotel().reserve(roomId);
            bookTravelFor(customer, travel);
        } catch (CustomerDataException | PaymentException e) {
            logger.info(e.getMessage());
        }
    }
    private static Travel selectTravel(int id, Agency agency) throws CustomerDataException {

        Optional<Travel> travelOptional = agency.getTravelById(id);
        if (travelOptional.isEmpty()) {
            throw new CustomerDataException("Invalid travel ID.");
        }
        return travelOptional.get();
    }

    private static void bookTravelFor(Customer customer, Travel travel) throws CustomerDataException, PaymentException {
        if (!travel.isSeatAvailable()) {
            throw new CustomerDataException("No seats available on the transport!");
        }

        applyDiscountIfNeeded(travel);

        if (customer.getBalance() < travel.getPrice()) {
            throw new CustomerDataException("Insufficient balance to book the flight.");
        }

        travel.bookSeat(customer);
        logger.info("Transport booked successfully!");
    }

    private static void applyDiscountIfNeeded(Travel travel) throws PaymentException {
        Period diff = Period.between(travel.getLocalDate(), LocalDate.now());
        int diffDays = diff.getDays();
        if (diffDays <= 3) {
            logger.info("Last minute discount!");
            travel.applyDiscount(50);
        }
    }


}
