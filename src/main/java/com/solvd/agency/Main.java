package com.solvd.agency;


import com.solvd.agency.exceptions.CustomerDataException;
import com.solvd.agency.exceptions.PaymentException;
import com.solvd.agency.exceptions.TravelExistsException;
import com.solvd.agency.other.Location;
import com.solvd.agency.person.Agent;
import com.solvd.agency.person.Customer;
import com.solvd.agency.place.Agency;
import com.solvd.agency.service.Insurance;
import com.solvd.agency.service.Transport;
import com.solvd.agency.service.Travel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static com.solvd.agency.person.Customer.getCustomerByPhoneNumber;
import static com.solvd.agency.utils.CreationUtils.*;
import static com.solvd.agency.utils.Utils.*;

public class Main {

    public static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws CustomerDataException, TravelExistsException, PaymentException {
        Map<Integer, Insurance> insuranceMap = createInsuranceMap();
        Location location = new Location("testStreet", "testCity", "testCountry");
        Scanner scanner = new Scanner(System.in);
        List<Agent> agents = new ArrayList<>();
        Transport transport = new Transport("Bus", 40);
        Agency agency = new Agency("MyAgency", location, agents);

        boolean exit = false;
        System.out.println("Welcome to the Travel Agency");
        logger.info("Start");
        while (!exit) {
            printMenu();

            int choice = scanner.nextInt();
            String name, surname;
            String phoneNumber;
            Customer customer;
            switch (choice) {
                case 1:
                    createTravel(scanner, agency);
                    System.out.println("\n0. Display agency related travels\n<Other>. Exit\n"); // will be edited
                    int displayAgency = scanner.nextInt();
                    if (displayAgency == 0) {
                        for (Travel travel : agency.getTravels()) {
                            travel.displayInfo();
                        }
                    }
                    break;
                case 2:
                    displayAllTravels(agency);
                    break;
                case 3:
                    logger.info("Enter name: ");
                    name = scanner.next();
                    logger.info("Enter surname: ");
                    surname = scanner.next();
                    logger.info("Enter phone number: ");
                    phoneNumber = scanner.next();
                    createCustomer(name, surname, phoneNumber, agency, insuranceMap);
                    logger.info("Customer created");
                    break;
                case 4:
                    customer = getCustomerByPhoneNumber(agency, scanner);
                    customer.addBalance(scanner);
                    break;
                case 5:
                    customer = getCustomerByPhoneNumber(agency, scanner);
                    customer.insuranceReminder(insuranceMap, scanner);
                    break;
                case 6:
                    logger.info("Creating a new agent...");
                    logger.info("Enter name: ");
                    name = scanner.next();
                    logger.info("Enter surname: ");
                    surname = scanner.next();
                    createAgent(name, surname, agency);
                    logger.info("Agent created");
                    break;
                case 7:
                    bookTravelForCustomer(scanner, agency); //tu
                    agency.displayInfo();
                    break;
                case 8:
                    display(agency.getAgents());
                    break;
                case 9:
                    display(agency.getCustomers());
                    break;
                case 10:
                    exit = true;
                    break;
                default:
                    logger.info("Enter a number between 1 and 12.");
                    logger.warn("Invalid input: {}", choice);
            }

        }
        scanner.close();
        logger.info("End");


    }


}





