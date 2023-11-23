package com.solvd.agency.utils;

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

import java.time.LocalDate;
import java.util.*;

import static com.solvd.agency.utils.Utils.*;

public class CreationUtils {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Logger logger = LogManager.getLogger(CreationUtils.class);

    public static void createCustomer(Agency agency) throws CustomerDataException {
        try {

            logger.info("Enter name: ");
            String name = StringUtils.strip(scanner.nextLine());
            validateData(name);

            logger.info("Enter surname: ");
            String surname = StringUtils.strip(scanner.nextLine());
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
        String dateString = scanner.nextLine();
        LocalDate travelDate = LocalDate.parse(dateString);
        logger.debug("Travel date set to: {}", travelDate);
        return travelDate;
    }

    private static double setTravelPrice() {
        logger.info("Set the price");
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
            LocalDate endDate = setTravelDate();
            if (endDate.isBefore(travelDate)) {
                throw new InvalidDateException("Wrong date");
            }
            double price = setTravelPrice();

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

        Insurance insurance1 = new Insurance(StringUtils.capitalize("ins001"), LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31), 100.0);
        insuranceMap.put(1, insurance1);

        Insurance insurance2 = new Insurance("INS002", LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31), 200.0);
        insuranceMap.put(2, insurance2);

        Insurance insurance3 = new Insurance("INS002", LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31), 400);
        insuranceMap.put(3, insurance3);

        return insuranceMap;
    }

    public static List<Transport> inProgressTransport() {
        Map<String, Transport> transportMap = new HashMap<>();
        transportMap.put("Transport C", new Transport("big-bus", 2));
        transportMap.put("Transport D", new Transport("mini-bus", 1));

        List<Transport> transportList = new ArrayList<>(transportMap.values());
        transportList.get(0).addCustomer(new Customer("a","a","111111111"));
        transportList.get(0).addCustomer(new Customer("b","b","111111112"));
        transportList.get(1).addCustomer(new Customer("c","c","111111113"));

        return transportList;
    }

    public static List<Destination> inProgressDestination() {
        Location location = new Location("TestStreet", "Warsaw", "Poland");
        Location location2 = new Location("TestStreet", "Cracow", "Poland");
        Hotel hotel1 = new Hotel("Hotel C", 20);
        Hotel hotel2 = new Hotel("Hotel D", 25);

        Destination destination = new Destination(location, hotel1);
        Destination destination2 = new Destination(location2, hotel2);


        List<Destination> destinationList = new ArrayList<>();
        destinationList.add(destination);
        destinationList.add(destination2);

        return destinationList;
    }


    public static List<Travel> inProgressTravel() throws CustomerDataException {
        int id = 10;
        int id1 = 20;
        List<Transport> transport = inProgressTransport();
        List<Destination> destination = inProgressDestination();
        LocalDate startTravel = LocalDate.parse("2023-10-11");
        LocalDate endTravel = LocalDate.parse("2023-10-20");

        destination.get(0).getHotel().reserve(1);
        destination.get(0).getHotel().reserve(2);
        destination.get(1).getHotel().reserve(1);

        LocalDate startTravel1 = LocalDate.parse("2023-11-20");
        LocalDate endTravel1 = LocalDate.parse("2023-11-27");

        double price = 200;

        Travel travel = new Travel(id, transport.get(0), destination.get(0),startTravel,endTravel, price);
        travel.setStatus();
        Travel travel2 = new Travel(id1, transport.get(1), destination.get(1),startTravel1,endTravel1, price);

        List<Travel> travels = new ArrayList<>();
        travels.add(travel);
        travels.add(travel2);

        return travels;
    }


}