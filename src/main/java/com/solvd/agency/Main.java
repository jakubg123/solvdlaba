package com.solvd.agency;


import com.solvd.agency.exceptions.CustomerDataException;
import com.solvd.agency.exceptions.PaymentException;
import com.solvd.agency.exceptions.TravelExistsException;
import com.solvd.agency.other.Destination;
import com.solvd.agency.other.Location;
import com.solvd.agency.person.Agent;
import com.solvd.agency.person.Customer;
import com.solvd.agency.place.Agency;
import com.solvd.agency.service.Insurance;
import com.solvd.agency.service.Transport;
import com.solvd.agency.service.Travel;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.solvd.agency.person.Customer.getCustomerByPhoneNumber;
import static com.solvd.agency.utils.CreationUtils.*;
import static com.solvd.agency.utils.Utils.*;

public class Main {

    public static final Logger logger = LogManager.getLogger(Main.class);


    public static void main(String[] args) throws CustomerDataException, TravelExistsException, PaymentException, ClassNotFoundException {
        Class<?> agentClass = Class.forName("com.solvd.agency.person.Agent");
        Map<Integer, Insurance> insuranceMap = createInsuranceMap();
        Location location = new Location("testStreet", "testCity", "testCountry");
        Scanner scanner = new Scanner(System.in);
        List<Agent> agents = new ArrayList<>();
        Agency agency = new Agency("MyAgency", location, agents);


        boolean exit = false;
        logger.info("Welcome to the Travel Agency");
        File file = new File("target/fileExercise.txt");
        while (!exit) {

            printMenu();
            try {

                int choice = scanner.nextInt();

                String name, surname;
                String phoneNumber;
                Customer customer;

                switch (choice) {
                    case 1:
                        display(agency.getAgents());
                        break;
                    case 2:
                        agency.displayCustomers();
                        break;
                    case 3:
                        displayAllTravels(agency);
                        break;
                    case 4:
                        createCustomer(agency);
                        break;

                    case 5:
                        createAgent(agency);
                        break;
                    case 6:
                        customer = getCustomerByPhoneNumber(agency);
                        customer.addBalance();
                        break;
                    case 7:
                        customer = getCustomerByPhoneNumber(agency);
                        customer.insuranceReminder(insuranceMap);
                        break;

                    case 8:
                        bookTravelForCustomer(agency);
                        agency.displayInfo();
                        break;
                    case 9:
                        createAndAddTravel(agency);
                        break;
                    case 10:
                        Predicate<Travel> travelFilter = travel -> travel.getPrice() < 200;
                        List<Travel> filteredTravels = agency.filterTravels(travelFilter);
                        logger.info(filteredTravels);
                        break;
                    case 11:
                        Function<Travel, String> getHotelNameFromTravel = travel -> travel.getDestination().getHotel().getName();
                        List<String> hotelNames = agency.getTravels().stream()
                                .map(getHotelNameFromTravel)
                                .collect(Collectors.toList());
                        logger.info(hotelNames);
                        break;
                    case 12:

                        agency.addReviewSupplier();
                    case 13:
                        String country = "Poland";
                        Predicate<Travel> isTravelInCountry = travel -> travel.getDestination().getLocation().getCountry().equalsIgnoreCase(country);
                        List<Travel> travelsInPoland = agency.getTravels().stream()
                                .filter(isTravelInCountry)
                                .collect(Collectors.toList());
                        logger.info(travelsInPoland);
                        break;
                    case 14:
                        logger.info("Agent fields (+ super class PERSON):");
                        Stream.concat(Arrays.stream(agentClass.getDeclaredFields()), Arrays.stream(agentClass.getSuperclass().getDeclaredFields()))
                                .forEach(field -> logger.info(field.getName()));
                        logger.info("Constructors:");
                        Arrays.stream(agentClass.getDeclaredConstructors())
                                .forEach(logger::info);
                        logger.info("Methods:");
                        Arrays.stream(agentClass.getDeclaredMethods())
                                .forEach(method -> logger.info(method.getName() + " " + method.getReturnType()));

                        Constructor<?> constructor = agentClass.getConstructor(int.class, String.class, String.class);
                        Object agentInstance = constructor.newInstance(1, "John", "Doe");

                        Method displayInfoMethod = agentClass.getMethod("displayInfo");
                        displayInfoMethod.invoke(agentInstance);

                        break;
                    case 15:
                        exit = true;
                        break;
                    default:
                        FileUtils.write(file, "User inserted: " + choice + "\n", StandardCharsets.UTF_8, true);
                        logger.info("Enter a number between 1 and 10.");
                        logger.warn("Invalid input: {}", choice);
                }
            } catch (java.util.InputMismatchException e) {
                logger.error("Use numbers to move around the console app");
                scanner.nextLine();
            } catch (IOException e) {
                logger.error(e.getMessage());
            } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }


        }
        scanner.close();
        logger.info("End");
        try {
            String storage = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            logger.info(storage);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        try {
            String read = readFirstLineFromFile("target/fileExercise.txt");
            logger.info(read);
        } catch (IOException e) {
            logger.error( e.getMessage());
        }


    }

    static String readFirstLineFromFile(String path) throws IOException {
        try (FileReader fr = new FileReader(path);
             BufferedReader br = new BufferedReader(fr)) {
            return br.readLine();
        }
    }


}





