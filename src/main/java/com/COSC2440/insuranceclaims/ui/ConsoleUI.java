package com.COSC2440.insuranceclaims.ui;

import com.COSC2440.insuranceclaims.managers.SimpleClaimProcessManager;
import com.COSC2440.insuranceclaims.models.Claim;
import com.COSC2440.insuranceclaims.models.Customer; // Ensure this is correct based on your package structure
import com.COSC2440.insuranceclaims.utils.FileManager;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class ConsoleUI {
    private final SimpleClaimProcessManager claimManager;


    //Constructors
    public ConsoleUI(SimpleClaimProcessManager claimManager) {
        this.claimManager = claimManager;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\nInsurance Claims Management System");
            System.out.println("1. View Claims");
            System.out.println("2. Add Claim");
            System.out.println("3. Find and Display Claim by ID"); // New option added
            System.out.println("4. Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    viewClaims();
                    break;
                case 2:
                    addClaim(scanner);
                    break;
                case 3:
                    findAndDisplayClaim(scanner); // Implementing the method call
                    break;
                case 4:
                    running = false;
                    System.out.println("Exiting the system. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }


    private void viewClaims() {
        List<Claim> claims = claimManager.getAll();
        if (claims.isEmpty()) {
            System.out.println("No claims available.");
        } else {
            claims.forEach(claim -> {
                // Simplified display, expand according to Claim attributes
                System.out.println("Claim ID: " + claim.getId() + ", Amount: " + claim.getClaimAmount());
            });
        }
    }

    private void addClaim(Scanner scanner) {
        System.out.println("Enter Claim ID:");
        String id = scanner.nextLine();

        // Simplify date handling for the example; consider using a date parsing utility for real inputs
        Date claimDate = new Date(); // Assuming current date for simplicity
        Customer insuredPerson = null; // You'll need to fetch or create the Customer object based on your application logic
        System.out.println("Enter Card Number:");
        String cardNumber = scanner.nextLine();
        Date examDate = new Date(); // Same simplification as for claimDate
        List<String> documents = new ArrayList<>(); // You might prompt the user to enter documents, separated by some delimiter, then parse
        System.out.println("Enter Claim Amount:");
        double claimAmount = Double.parseDouble(scanner.nextLine());
        System.out.println("Enter Status:");
        String status = scanner.nextLine();
        System.out.println("Enter Receiver Banking Info:");
        String receiverBankingInfo = scanner.nextLine();

        Claim claim = new Claim(id, claimDate, insuredPerson, cardNumber, examDate, documents, claimAmount, status, receiverBankingInfo);
        claimManager.add(claim);
        System.out.println("Claim added.");
    }


    private void findAndDisplayClaim(Scanner scanner) {
        System.out.println("Enter Claim ID to search:");
        String claimId = scanner.nextLine();

        // Assuming the path to your claims CSV file - adjust as necessary
        String claimsFilePath = "path/to/your/claims.csv";
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