package com.solvd.agency.place;


import com.solvd.agency.exceptions.CustomerDataException;
import com.solvd.agency.interfaces.Cleanable;
import com.solvd.agency.interfaces.Displayable;
import com.solvd.agency.interfaces.Reviewable;
import com.solvd.agency.other.Destination;
import com.solvd.agency.other.Location;
import com.solvd.agency.person.Agent;
import com.solvd.agency.person.Customer;
import com.solvd.agency.service.Review;
import com.solvd.agency.service.Transport;
import com.solvd.agency.service.Travel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.solvd.agency.utils.Utils.readPhoneNumber;
import static com.solvd.agency.utils.Utils.validatePhoneNUmber;


public class Agency implements Reviewable, Displayable, Cleanable {

    private static final Logger logger = LogManager.getLogger(Agent.class.getName());

    private String name;
    private Location location;
    private Deque<Review> reviews;
    private List<Agent> agents;
    private List<Travel> travels;

    private boolean isClean;


    private List<Customer> customers;


    public Agency(String name, Location location, List<Agent> agents) {
        this.reviews = new ArrayDeque<>();
        this.agents = new LinkedList<>(agents);
        this.travels = new ArrayList<>();
        this.name = name;
        this.location = location;
        this.agents = agents;
        this.customers = new ArrayList<>();
        this.isClean = false;
    }


    public void printAverageRating() {
        System.out.println("Average Rating: " + getAverageRating());
    }


    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Deque<Review> getReview() {
        return reviews;
    }


    public List<Travel> getTravels() {
        return travels;
    }

    public void setTravels(List<Travel> travels) {
        this.travels = travels;
    }

    public void setReview(Deque<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public void setAgents(List<Agent> agents) {
        this.agents = agents;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Deque<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Deque<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }


    @Override
    public float getAverageRating() {
        if (this.reviews.isEmpty()) return 0;

        return (float) reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agency agency = (Agency) o;
        return Objects.equals(name, agency.name) &&
                Objects.equals(location, agency.location) &&
                reviews.equals(((Agency) o).getReview()) &&
                agents.equals(((Agency) o).getAgents()) &&
                travels.equals(((Agency) o).getTravels());
    }


    @Override
    public int hashCode() {
        int result = Objects.hash(name, location);
        result = 31 * result + reviews.hashCode();
        result = 31 * result + agents.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Agency{" +
                "name='" + name + '\'' +
                ", location=" + location +
                '}';
    }

    public boolean findTravelById(int travelId) {
        return travels.stream().anyMatch(travel -> travel.getId() == travelId);
    }

    public Optional<Travel> getTravelById(int travelId) {
        return travels.stream().filter(travel -> travel.getId() == travelId).findFirst();
    }


    public boolean isDestinationAvailable(Destination destination) {
        return travels.stream().anyMatch(travel -> travel.getDestination().equals(destination));
    }


    public Map<String, String> customerPhoneBook() {
        Map<String, String> phoneMap = new HashMap<>();

        for (Travel travel : this.travels) {
            for (Customer customer : travel.getTransport().getSeats()) {
                if (customer != null) {
                    String fullName = customer.getName() + " " + customer.getSurname();
                    phoneMap.put(customer.getPhoneNumber(), fullName);
                }
            }
        }

        return phoneMap;
    }

    @Override
    public void displayInfo() {
        logger.info(customerPhoneBook().toString());
    }

    public void addTravel(Travel travel) {
        this.travels.add(travel);
    }

    public void addCustomer(Customer customer) {
        this.customers.add(customer);
    }

    public void addReview(Review newReview) {
        this.reviews.add(newReview);
    }

    public void addAgent(Agent agent) {
        this.agents.add(agent);
    }

    public Customer findCustomerByPhoneNumber(String phoneNumber) throws CustomerDataException {
        try
        {
            validatePhoneNUmber(phoneNumber);
        }
        catch(CustomerDataException e){
            logger.warn(e.getMessage());
        }
        return getCustomers().stream()
                .filter(c -> c.getPhoneNumber().equals(phoneNumber))
                .findFirst()
                .orElseThrow(() -> new CustomerDataException("Customer not found with phone number: " + phoneNumber));
    }


    @Override
    public void clean() {
        LocalDateTime today = LocalDateTime.now();
        int hour = today.getHour();

        if(hour > 18){
            System.out.println("Cleaning service arrived");
            isClean = true;
        }
        else{
            System.out.println("Cleaning service can't arrive until 6 PM");
        }
    }

    public List<Travel> filterTravels(Predicate<Travel> travelFilter) {
        return this.travels.stream()
                .filter(travelFilter)
                .collect(Collectors.toList());
    }

    public void addReviewSupplier(){
        this.reviews.add(reviewSupplier.get());
    }
    public Supplier<Review> reviewSupplier = () -> {
        Scanner scanner = new Scanner(System.in);

        logger.info("Enter review comment: ");
        String name = scanner.nextLine();

        logger.info("Enter review body");
        String body = scanner.nextLine();

        logger.info("Enter rating (1-5): ");
        int rating = scanner.nextInt();
        scanner.nextLine();

        return new Review(name,body, rating);
    };

    public Consumer<Customer> printCustomer = customer ->
            logger.info("Customer: " + customer.getName() + " " + customer.getSurname() +
                    ", Phone: " + customer.getPhoneNumber() + ", balance" + customer.getBalance());

    public void displayCustomers() {
        if(this.getCustomers().isEmpty())
        {
            logger.info("No customers registered");
        }
        this.customers.forEach(printCustomer);
    }

}

