package com.solvd.agency.utils;

import com.solvd.agency.exceptions.CustomerDataException;
import com.solvd.agency.exceptions.TravelExistsException;
import com.solvd.agency.other.Destination;
import com.solvd.agency.other.Location;
import com.solvd.agency.person.Agent;
import com.solvd.agency.person.Customer;
import com.solvd.agency.place.Agency;
import com.solvd.agency.place.Hotel;
import com.solvd.agency.service.Insurance;
import com.solvd.agency.service.Transport;
import com.solvd.agency.service.Travel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import static com.solvd.agency.utils.Utils.*;

public class CreationUtils {
    private static final Logger logger = LogManager.getLogger(CreationUtils.class);

    public static void createCustomer(String name, String surname, String phoneNumber, Agency agency, Map<Integer, Insurance> insuranceMap) throws CustomerDataException {
        try {
            validateData(name);
            validateData(surname);
            validatePhoneNUmber(phoneNumber);
        }
        catch(CustomerDataException e){
            e.getMessage();
        }
            Customer customer = new Customer(name, surname, phoneNumber);
            agency.addCustomer(customer);

    }

    public static void createAgent(String name, String surname, Agency agency) throws CustomerDataException {
        try{
        validateData(name);
        validateData(surname);
        }
        catch(CustomerDataException e){
            e.getMessage();
        }
        Agent agent = new Agent(agency.getAgents().size() + 1, name, surname);
        agency.addAgent(agent);
    }

    public static Location createLocation(Scanner scanner) {
        logger.info("Enter destination country:");
        String destinationCountry = scanner.nextLine();
        scanner.nextLine(); // \n consume
        logger.info("Enter destination city:");
        String destinationCity = scanner.nextLine();
        logger.info("Enter destination street:");
        String destinationStreet = scanner.nextLine();

        return new Location(destinationCountry, destinationCity, destinationStreet);

    }

    public static Hotel createHotel(Scanner scanner) {
        System.out.print("Enter Hotel name: ");
        String name = scanner.nextLine();

        Map<Integer, Boolean> roomReservationStatus = new HashMap<>();
        System.out.println("Enter the number of rooms in the hotel:");
        int numberOfRooms = scanner.nextInt();
        for (int i = 1; i <= numberOfRooms; i++) {
            roomReservationStatus.put(i, false);
        }

        return new Hotel(name, roomReservationStatus);
    }
    public static Destination createDestination(Scanner scanner) {
        Location location = createLocation(scanner);
        Hotel hotel = createHotel(scanner);
        return new Destination(location, hotel);
    }

    // too much initialization in the console app, I decided to predefine these in a map

    public static Transport createTransport(Scanner scanner) {
        System.out.println("Enter transport name");
        String name = scanner.nextLine();
        System.out.println("Enter available seats");
        int seats = scanner.nextInt();

        return new Transport(name, seats);
    }

    public static void createTravel(Scanner scanner, Agency agency) throws TravelExistsException, CustomerDataException {
        logger.info("Starting travel creation process");

        int id;
        boolean isCreated = true;
        while (isCreated) {
            id = Math.abs(new Random().nextInt());
            logger.debug("Generated travel ID: {}", id);

            try {
                if (agency.findTravelById(id)) {
                    throw new TravelExistsException("Travel exists with ID: " + id);
                } else {
                    Map<Integer, Destination> destinationMap = createDestinationMap();
                    logger.debug("Destination map created");

                    System.out.println("Choose your destination:\n");
                    displayMap(destinationMap);
                    int key = scanner.nextInt();
                    if (!destinationMap.containsKey(key)) {
                        logger.warn("Invalid destination key selected: {}", key);
                        continue;
                    }

                    Destination destination = destinationMap.get(key);
                    logger.info("Destination selected: {}", destination);

                    Map<Integer, Transport> transportMap = createTransportMap();
                    logger.debug("Transport map created");

                    System.out.println("Choose your transport:\n");
                    displayMap(transportMap);
                    key = scanner.nextInt();
                    if (!transportMap.containsKey(key)) {
                        logger.warn("Invalid transport key selected: {}", key);
                        continue;
                    }

                    Transport transport = transportMap.get(key);
                    logger.info("Transport selected: {}", transport);

                    System.out.println("Enter +days to the travel date");
                    int days = scanner.nextInt();
                    LocalDate travelDate = LocalDate.now().plusDays(days);
                    logger.debug("Travel date set to: {}", travelDate);

                    System.out.println("Set the price");
                    double price = scanner.nextDouble();
                    logger.debug("Price set to: {}", price);

                    Travel travel = new Travel(id, transport, destination, travelDate, price);
                    agency.addTravel(travel);
                    logger.info("Travel created successfully: {}", travel);
                    isCreated = false;
                }

            } catch (TravelExistsException e) {
                logger.error("Travel creation failed due to existing travel ID: {}", id, e);
                System.out.println(e.getMessage());
            }
        }
    }

    public static Map<Integer, Destination> createDestinationMap() {
        Hotel hotel1 = new Hotel("Hotel A", createLocation(s));
        Hotel hotel2 = new Hotel("Hotel B");

        Location location1 = new Location("testStreet", "testCity", "testCountry");
        Location location2 = new Location("Street", "City", "Country");

        Destination destination1 = new Destination(location1, hotel1);
        Destination destination2 = new Destination(location2, hotel2);

        Map<Integer, Destination> map = new HashMap<>();

        map.put(1, destination1);
        map.put(2, destination2);

        return map;
    }


    public static Map<Integer, Transport> createTransportMap() {
        Transport transport1 = new Transport("Transport A", 50);
        Transport transport2 = new Transport("Transport B", 100);

        Map<Integer, Transport> map = new HashMap<>();

        map.put(1, transport1);
        map.put(2, transport2);

        return map;
    }

    public static Map<Integer, Insurance> createInsuranceMap() {
        Map<Integer, Insurance> insuranceMap = new HashMap<>();

        Insurance insurance1 = new Insurance("INS001", LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31), 100.0);
        insuranceMap.put(1, insurance1);

        Insurance insurance2 = new Insurance("INS002", LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31), 150.0);
        insuranceMap.put(2, insurance2);

        return insuranceMap;
    }


}