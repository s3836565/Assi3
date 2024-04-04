package com.COSC2440.insuranceclaims.ui;


import com.COSC2440.insuranceclaims.managers.SimpleClaimProcessManager;
import com.COSC2440.insuranceclaims.models.Claim;
import com.COSC2440.insuranceclaims.models.Customer;
import com.COSC2440.insuranceclaims.utils.FileManager;
import com.COSC2440.insuranceclaims.models.InsuranceCard;
import com.COSC2440.insuranceclaims.models.PolicyHolder;

import java.text.ParseException;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.io.BufferedReader;

public class ConsoleUI {
    private final SimpleClaimProcessManager claimManager;
    private List<Customer> customers = new ArrayList<>();
    private List<InsuranceCard> insuranceCards = new ArrayList<>();

    public ConsoleUI(SimpleClaimProcessManager claimManager, List<Customer> customers, List<InsuranceCard> insuranceCards) {
        this.claimManager = claimManager;
        this.customers = customers;
        this.insuranceCards = insuranceCards;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\nInsurance Claims Management System");
            System.out.println("1. View Customers");
            System.out.println("2. Add Customer");
            System.out.println("3. View Claims");
            System.out.println("4. Add Claim");
            System.out.println("5. Find and Display Claim by ID");
            System.out.println("6. Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    viewCustomers();
                    break;
                case 2:
                    addCustomer(scanner);
                    break;
                case 3:
                    viewClaims();
                    break;
                case 4:
                    addClaim(scanner);
                    break;
                case 5:
                    findAndDisplayClaim(scanner);
                    break;
                case 6:
                    running = false;
                    System.out.println("Exiting the system. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }

    public void viewCustomers() {
        System.out.println("Viewing Customers:");
        if (customers.isEmpty()) {
            System.out.println("No customers loaded from the file.");
        } else {
            for (Customer customer : customers) {
                System.out.println(customer); // Assuming toString() is overridden in Customer class
            }
        }
    }


    private void addCustomer(Scanner scanner) {
        // Implementation remains the same
    }

    private void viewClaims() {
        // Assuming implementation exists for claimManager.getAll()
        List<Claim> claims = claimManager.getAll();
        if (claims.isEmpty()) {
            System.out.println("No claims available.");
        } else {
            for (Claim claim : claims) {
                System.out.println("Claim ID: " + claim.getId() + ", Amount: " + claim.getClaimAmount() + ", Status: " + claim.getStatus());
                // Additional details can be added here
            }
        }
    }

    private void addClaim(Scanner scanner) {
        System.out.println("Please enter the following details for the new claim:");
        System.out.print("Claim ID: ");
        String id = scanner.nextLine();
        System.out.print("Card Number (associated with the claim): ");
        String cardNumber = scanner.nextLine();
        // Simplify for example; consider real date input and parsing
        Date claimDate = new Date(); // Current date for simplicity
        double claimAmount = getClaimAmount(scanner);
        String status = getClaimStatus(scanner);
        // More complex logic needed to correctly set insuredPerson and other data

        // Assuming a matching constructor or setters to properly set these fields
        Claim claim = new Claim(id, claimDate, null, cardNumber, new Date(), new ArrayList<>(), claimAmount, status, "");
        claimManager.add(claim);
        System.out.println("Claim added successfully.");
    }

    private double getClaimAmount(Scanner scanner) {
        System.out.print("Claim Amount: ");
        return Double.parseDouble(scanner.nextLine());
    }

    private String getClaimStatus(Scanner scanner) {
        System.out.print("Claim Status (New/Processing/Done): ");
        return scanner.nextLine();
    }


    private void findAndDisplayClaim(Scanner scanner) {
        System.out.println("Enter Claim ID to search:");
        String claimId = scanner.nextLine();

        // Assuming the path to your claims CSV file - adjust as necessary
        String claimsFilePath = "src/main/resources/claims.csv";
        Claim claim = FileManager.readClaimById(claimsFilePath, claimId);

        if (claim != null) {
            // Display the claim details
            System.out.println("Claim found: ");
            System.out.println("ID: " + claim.getId() + ", Amount: " + claim.getClaimAmount() + ", Status: " + claim.getStatus());
            // Add further details as per your Claim class structure
        } else {
            System.out.println("No claim found with ID: " + claimId);
        }
    }


}