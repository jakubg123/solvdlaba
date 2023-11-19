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
import java.util.*;

import static com.solvd.agency.utils.Utils.*;

public class CreationUtils {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Logger logger = LogManager.getLogger(CreationUtils.class);

    public static void createCustomer(Agency agency) throws CustomerDataException {
        try {

            logger.info("Enter name: ");
            String name = scanner.next();
            validateData(name);

            logger.info("Enter surname: ");
            String surname = scanner.next();
            validateData(surname);

            logger.info("Enter phone number: ");
            String phoneNumber = scanner.next();
            validatePhoneNUmber(phoneNumber);

            Customer customer = new Customer(name, surname, phoneNumber);
            logger.info("Customer created");
            agency.addCustomer(customer);

        } catch (CustomerDataException e) {
            logger.info(e.getMessage());
        }


    }

    public static void createAgent(Agency agency) throws CustomerDataException {
        logger.info("Creating a new agent...");
        try {
            logger.info("Enter name: ");
            String name = scanner.next();
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
            }
            catch(InputMismatchException e) {
                logger.warn("Invalid destination selected");
            }
            finally
            {
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
        System.out.println("Enter +days to the travel date");
        int days = scanner.nextInt();
        scanner.nextLine();
        LocalDate travelDate = LocalDate.now().plusDays(days);
        logger.debug("Travel date set to: {}", travelDate);
        return travelDate;
    }

    private static double setTravelPrice() {
        System.out.println("Set the price");
        double price = scanner.nextDouble();
        scanner.nextLine();
        logger.debug("Price set to: {}", price);
        return price;
    }

    public static void createAndAddTravel(Agency agency) {
        try {
            int id = generateUniqueTravelId(agency);
            Destination destination = selectDestination();
            Transport transport = selectTransport();
            LocalDate travelDate = setTravelDate();
            double price = setTravelPrice();

            Travel travel = new Travel(id, transport, destination, travelDate, price);
            agency.addTravel(travel);
            logger.info("Travel created successfully: {}", travel);
        } catch (CustomerDataException e) {
            logger.error(e.getMessage());
        }
    }

    public static Map<Integer, Destination> createDestinationMap() {
        Map<Integer, Boolean> roomReservationStatus1 = new HashMap<>();
        for (int i = 1; i <= 10; i++) {
            roomReservationStatus1.put(i, false);
        }

        Map<Integer, Boolean> roomReservationStatus2 = new HashMap<>();
        for (int i = 1; i <= 15; i++) {
            roomReservationStatus2.put(i, false);
        }

        Hotel hotel1 = new Hotel("Hotel A", roomReservationStatus1);
        Hotel hotel2 = new Hotel("Hotel B", roomReservationStatus2);

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

        Insurance insurance2 = new Insurance("INS002", LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31), 200.0);
        insuranceMap.put(2, insurance2);

        Insurance insurance3 = new Insurance("INS002", LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31), 400);
        insuranceMap.put(3, insurance3);

        return insuranceMap;
    }


}