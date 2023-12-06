package com.solvd.agency.utils;

import com.solvd.agency.enums.Country;
import com.solvd.agency.enums.Month;
import com.solvd.agency.enums.Season;
import com.solvd.agency.enums.TransportType;
import com.solvd.agency.exceptions.CustomerDataException;
import com.solvd.agency.exceptions.InvalidDateException;
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
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

import static com.solvd.agency.enums.Season.getSeasonFromMonth;
import static com.solvd.agency.utils.Utils.*;

public class CreationUtils {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Logger logger = LogManager.getLogger(CreationUtils.class);

    public CreationUtils() throws CustomerDataException {
    }

    public static void createCustomer(Agency agency) throws CustomerDataException {
        try {

            logger.info("Enter name: ");
            String name = StringUtils.strip(scanner.nextLine());
            validateData(name);

            logger.info("Enter surname: ");
            String surname = StringUtils.strip(scanner.nextLine());
            validateData(surname);

            logger.info("Enter phone number (9 digits is a must!): ");
            String phoneNumber = scanner.next();
            validatePhoneNUmber(phoneNumber);

            Customer customer = new Customer(name, surname, phoneNumber);
            logger.info("Customer created");
            agency.addCustomer(customer);
            scanner.nextLine();

        } catch (CustomerDataException e) {
            logger.info(e.getMessage());
        }


    }

    public static void createAgent(Agency agency) throws CustomerDataException {
        logger.info("Creating a new agent...");
        try {
            logger.info("Enter name: ");
            String name = StringUtils.strip(scanner.nextLine());
            validateData(name);

            logger.info("Enter surname: ");
            String surname = scanner.next();
            validateData(surname);

            Agent agent = new Agent(agency.getAgents().size() + 1, name, surname);
            logger.info("Agent created");
            agency.addAgent(agent);

        } catch (CustomerDataException e) {
            logger.warn(e.getMessage());
        }

    }

//    public static Location createLocation() {
//        logger.info("Enter destination country:");
//        String destinationCountry = scanner.nextLine();
//        logger.info("Enter destination city:");
//        String destinationCity = scanner.nextLine();
//        logger.info("Enter destination street:");
//        String destinationStreet = scanner.nextLine();
//
//        return new Location(destinationCountry, destinationCity, destinationStreet);
//    }
//
//    public static Hotel createHotel() {
//        System.out.print("Enter Hotel name: ");
//        String name = scanner.nextLine();
//
//        Map<Integer, Boolean> roomReservationStatus = new HashMap<>();
//        System.out.println("Enter the number of rooms in the hotel:");
//        int numberOfRooms = Integer.parseInt(scanner.nextLine());
//        for (int i = 1; i <= numberOfRooms; i++) {
//            roomReservationStatus.put(i, false);
//        }
//
//        return new Hotel(name, roomReservationStatus);
//    }
//    public static Destination createDestination() {
//        Location location = createLocation();
//        Hotel hotel = createHotel();
//        return new Destination(location, hotel);
//    }


//    public static Transport createTransport() {
//        logger.info("Enter transport name");
//        String name = scanner.nextLine();
//        logger.info("Enter available seats");
//        int seats = Integer.parseInt(scanner.nextLine());
//
//        return new Transport(name, seats);
//    }

    private static int generateUniqueTravelId(Agency agency) {
        int id;
        while (true) {
            id = Math.abs(new Random().nextInt());
            logger.debug("Generated travel ID: {}", id);
            if (!agency.findTravelById(id)) {
                return id;
            }
            logger.debug("Travel ID already exists, generating a new one.");
        }
    }

    private static Destination selectDestination() {
        Map<Integer, Destination> destinationMap = createDestinationMap();
        logger.debug("Destination map created");

        while (true) {
            try {
                logger.info("Choose your destination:\n");
                displayMap(destinationMap);
                int key = scanner.nextInt();
                scanner.nextLine();
                if (destinationMap.containsKey(key)) {
                    Destination destination = destinationMap.get(key);
                    logger.info("Destination selected: {}", destination);
                    return destination;
                }
                break;
            } catch (InputMismatchException e) {
                logger.warn("Invalid destination selected");
            } finally {
                scanner.nextLine();
            }
        }
        return null;
    }

    private static Transport selectTransport() {
        Map<Integer, Transport> transportMap = createTransportMap();
        logger.debug("Transport map created");

        while (true) {
            System.out.println("Choose your transport:\n");
            displayMap(transportMap);
            int key = scanner.nextInt();
            scanner.nextLine();
            if (transportMap.containsKey(key)) {
                Transport transport = transportMap.get(key);
                logger.info("Transport selected: {}", transport);
                return transport;
            }
            logger.warn("Invalid transport key selected: {}", key);
        }
    }

    private static LocalDate setTravelDate() {
        logger.info("Enter a date (yyyy-mm-dd): ");
        String dateString = scanner.nextLine().trim(); // Use trim to remove leading/trailing whitespace
        LocalDate travelDate = LocalDate.parse(dateString);
        logger.debug("Travel date set to: {}", travelDate);
        return travelDate;
    }

    private static double setTravelPrice() {
        logger.info("Set the price");
        String priceString = scanner.nextLine().trim(); // Use trim here as well
        double price = Double.parseDouble(priceString);
        logger.debug("Price set to: {}", price);
        return price;
    }

    public static void createAndAddTravel(Agency agency) {
        try {
            int id = generateUniqueTravelId(agency);
            Destination destination = selectDestination();

            assert destination != null;
            String countryName = destination.getLocation().getCountry().toUpperCase();
            Country country = Country.valueOf(countryName);
            Transport transport = selectTransport();
            LocalDate travelDate = setTravelDate();
            LocalDate endDate = setTravelDate();
            if (endDate.isBefore(travelDate)) {
                throw new InvalidDateException("Wrong date");
            }
            double price = setTravelPrice();
            Season travelSeason = getSeasonFromMonth(travelDate.getMonth());
            if (country.getBestVacationSeason() == travelSeason) {
                price *= 1.1;
            }


            Travel travel = new Travel(id, transport, destination, travelDate, endDate, price);
            agency.addTravel(travel);
            logger.info("Travel created successfully: {}", travel);
        } catch (CustomerDataException | InvalidDateException | InputMismatchException e) {
            logger.error(e.getMessage());
        }
    }





    public static Map<Integer, Destination> createDestinationMap() {
        Map<Integer, Boolean> roomReservationStatus1 = new HashMap<>();

        Hotel hotel1 = new Hotel("Hotel A", 15);
        Hotel hotel2 = new Hotel("Hotel B", 10);

        Location location1 = new Location("testStreet", "testCity", "Poland");
        Location location2 = new Location("Street", "City", "Czech_Republic");

        Destination destination1 = new Destination(location1, hotel1);
        Destination destination2 = new Destination(location2, hotel2);

        Map<Integer, Destination> map = new HashMap<>();
        map.put(1, destination1);
        map.put(2, destination2);

        return map;
    }

    public static Map<Integer, Transport> createTransportMap() {
        Transport transport1 = new Transport(50, TransportType.BUS);
        Transport transport2 = new Transport(20, TransportType.MINIBUS);

        Map<Integer, Transport> map = new HashMap<>();
        map.put(1, transport1);
        map.put(2, transport2);

        return map;
    }

    public static Map<Integer, Insurance> createInsuranceMap() {
        Map<Integer, Insurance> insuranceMap = new HashMap<>();

        Insurance insurance1 = new Insurance(StringUtils.capitalize("ins001"), LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31), 100.0);
        insuranceMap.put(1, insurance1);

        Insurance insurance2 = new Insurance("INS002", LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31), 200.0);
        insuranceMap.put(2, insurance2);

        Insurance insurance3 = new Insurance("INS002", LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31), 400);
        insuranceMap.put(3, insurance3);

        return insuranceMap;
    }


    public static void createFile(String filePath) {
        try {
            File file = new File(filePath);
            file.getParentFile().mkdirs();
            if (file.createNewFile()) {
                logger.info("File created: " + file.getAbsolutePath());
            } else {
                logger.info("File already exists: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            logger.info("Could not create file: " + e.getMessage());
        }
    }
    public static boolean isFileEmpty(String filePath) {
        File file = new File(filePath);

        if (!file.exists() || file.length() == 0) {
            return true;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return reader.readLine() == null;
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
    }






}