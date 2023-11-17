package com.solvd.agency.person;


import com.solvd.agency.exceptions.CustomerDataException;
import com.solvd.agency.other.Location;
import com.solvd.agency.place.Agency;
import com.solvd.agency.service.Insurance;
import com.solvd.agency.service.Transport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

import static com.solvd.agency.utils.Utils.displayMap;
import static com.solvd.agency.utils.Utils.readPhoneNumber;

public class Customer extends Person {
    private static final Scanner scanner = new Scanner(System.in);

    private static final Logger logger = LogManager.getLogger(Customer.class.getName());
    private String phoneNumber;
    private Insurance insurance;
    private double balance;
    private double income;


    public Customer(String name, String surname, String phoneNumber) {

        super(name, surname);
        this.phoneNumber = phoneNumber;
        this.balance = 0;
        this.income = 0;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    @Override
    public String getIdentifier() {
        return getPhoneNumber();
    }

    @Override
    public void setIdentifier(String identifier) {
        setPhoneNumber(identifier);
    }

    @Override
    public String toString() {
        return super.toString() + " " + phoneNumber + " " + balance;
    }

    public final void printInsuranceDetails() {
        logger.info("Insurance Number: " + insurance.getInsuranceNumber());
    }

    @Override
    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o))
            return false;
        Customer customer = (Customer) o;
        return Objects.equals(getPhoneNumber(), customer.getPhoneNumber()) &&
                Objects.equals(insurance.getInsuranceNumber(), customer.insurance.getInsuranceNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getPhoneNumber());
    }


    @Override
    public void displayInfo() {
        if (this.insurance != null) {
            logger.info(this.toString() + " " + insurance.getInsuranceNumber());
        } else {
            logger.info(this.toString());
        }
    }

    public void purchaseInsurance(Map<Integer, Insurance> insuranceMap) throws CustomerDataException {
        try {
            displayMap(insuranceMap);
            logger.info("Enter the insurance ID you want to purchase:");
            int choice = scanner.nextInt();
            Insurance selectedInsurance = insuranceMap.get(choice);
            if (selectedInsurance == null) {
                throw new CustomerDataException("Invalid insurance id");
            } else {
                if (this.balance >= selectedInsurance.getPrice()) {
                    this.balance -= selectedInsurance.getPrice();
                    this.insurance = selectedInsurance;
                    logger.info("Insurance purchased.");
                } else {
                    logger.info("Invalid choice or not enough balance!");
                }
            }
        } catch (CustomerDataException e) {
            logger.error(e.getMessage());
        }

    }


    public void insuranceReminder(Map<Integer, Insurance> insuranceMap) throws CustomerDataException {
        if (insurance == null || !insurance.isPolicyActive()) {
            logger.info("Would you like to purchase insurance? (Type 'Yes' to proceed)");
            String choice = scanner.next();

            if ("Yes".equalsIgnoreCase(choice)) {
                purchaseInsurance(insuranceMap);
            }else {
                logger.info("Back to lobby");
            }
        } else {
           logger.info("You already have an active insurance. Happy travels!");
        }
    }
    public void pay(double price) {
        this.balance -= price;
    }


    public static Customer getCustomerByPhoneNumber(Agency agency) throws CustomerDataException {
        String phoneNumber = readPhoneNumber();
        return agency.findCustomerByPhoneNumber(phoneNumber);
    }

    public void addBalance() {
        BigDecimal money = null;
        while (true) {
            try {
                logger.info("Enter balance:");
                String input = scanner.nextLine();
                money = new BigDecimal(input);

                if (money.compareTo(BigDecimal.ZERO) < 0) {
                    throw new IllegalArgumentException("Negative value");
                }
                double verifiedBalance = money.doubleValue();
                this.balance = this.balance + verifiedBalance;
                break;
            } catch (NumberFormatException e) {
                logger.warn("Invalid value.");

            }
            catch (InputMismatchException | IllegalArgumentException e) {
                logger.warn(e.getMessage());
            }
            finally{
                scanner.nextLine();
            }

        }
    }


}

