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
        logger.info("Welcome to the Travel Agency");

        while (!exit) {
            printMenu();
            int choice = scanner.nextInt();

            String name, surname;
            String phoneNumber;
            Customer customer;

            switch (choice) {
                case 1:
                    display(agency.getAgents());
                    break;
                case 2:
                    display(agency.getCustomers());
                    break;
                case 3:
                    displayAllTravels(agency);
                    break;

                case 4:
                    logger.info("Enter name: ");
                    name = scanner.next();
                    logger.info("Enter surname: ");
                    surname = scanner.next();
                    logger.info("Enter phone number: ");
                    phoneNumber = scanner.next();
                    createCustomer(name, surname, phoneNumber, agency);
                    break;

                case 5:

                    logger.info("Enter name: ");
                    name = scanner.next();
                    logger.info("Enter surname: ");
                    surname = scanner.next();
                    createAgent(name, surname, agency);
                    break;

                case 6:
                    customer = getCustomerByPhoneNumber(agency);
                    logger.info("Enter balance to add");
                    double balance = scanner.nextDouble();
                    customer.addBalance(balance);
                    break;
                case 7:
                    customer = getCustomerByPhoneNumber(agency);
                    customer.insuranceReminder(insuranceMap);
                    break;

                case 8:
                    phoneNumber = readPhoneNumber();
                    bookTravelForCustomer(phoneNumber, agency);
                    agency.displayInfo();
                    break;
                case 9:
                    createAndAddTravel(agency);
                    break;
                case 10:
                    exit = true;
                    break;
                default:
                    logger.info("Enter a number between 1 and 10.");
                    logger.warn("Invalid input: {}", choice);
            }

        }
        scanner.close();
        logger.info("End");


    }


}





