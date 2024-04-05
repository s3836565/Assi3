package com.COSC2440.insuranceclaims.ui;


import com.COSC2440.insuranceclaims.managers.SimpleClaimProcessManager;
import com.COSC2440.insuranceclaims.models.Claim;
import com.COSC2440.insuranceclaims.models.Customer;
import com.COSC2440.insuranceclaims.utils.FileManager;
import com.COSC2440.insuranceclaims.models.InsuranceCard;
import com.COSC2440.insuranceclaims.models.PolicyHolder;
import com.COSC2440.insuranceclaims.models.Dependent;
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
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.File;

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
            System.out.println("2. Add PolicyHolder");
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
    private void saveCustomersToCSV(String filePath, List<Customer> customers) {
        try (PrintWriter writer = new PrintWriter(new File(filePath))) {
            // Writing the header row
            StringBuilder sb = new StringBuilder();
            sb.append("ID,FullName,Type,PolicyHolderID,InsuranceCardNumber\n");

            for (Customer customer : customers) {
                sb.append(customer.getId());
                sb.append(',');
                sb.append(customer.getFullName());
                sb.append(',');

                if (customer instanceof PolicyHolder) {
                    sb.append("PolicyHolder,");
                    sb.append(","); // Placeholder for PolicyHolderID, always empty for a PolicyHolder
                    if (((PolicyHolder) customer).getInsuranceCard() != null) {
                        sb.append(((PolicyHolder) customer).getInsuranceCard().getCardNumber());
                    }
                    sb.append(","); // Ensuring the structure is maintained even if the number is missing
                } else if (customer instanceof Dependent) {
                    sb.append("Dependent,");
                    sb.append(((Dependent) customer).getPolicyHolder().getId());
                    sb.append(",,"); // Dependent does not have an InsuranceCardNumber, adding placeholders
                }
                sb.append('\n');
            }

            writer.write(sb.toString());
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while saving to CSV.");
            e.printStackTrace();
        }
    }






    public void addCustomer(Scanner scanner) {
        System.out.println("Enter PolicyHolder's ID:");
        String id = scanner.nextLine();

        System.out.println("Enter PolicyHolder's Full Name:");
        String fullName = scanner.nextLine();

        System.out.println("Enter PolicyHolder's Insurance Card Number:");
        String insuranceCardNumber = scanner.nextLine();

        // Assuming you have a way to specify or find a Customer object and PolicyHolder
        // For the purpose of this example, I'll assume the new PolicyHolder is the customer.
        // You'll need to adapt this logic based on your application's requirements.
        PolicyHolder policyHolder = new PolicyHolder(id, fullName); // Simplified constructor call

        // Assuming you have logic to determine these values
        Date cardIssueDate = new Date(); // Example: current date as issue date

        // Create the InsuranceCard with all required parameters
        InsuranceCard insuranceCard = new InsuranceCard(insuranceCardNumber, policyHolder, policyHolder, cardIssueDate);

        // Assuming the policyHolder list and setting the InsuranceCard
        policyHolder.setInsuranceCard(insuranceCard); // Make sure such a setter exists or adjust accordingly
        customers.add(policyHolder); // Add the policyHolder to the list of customers
        saveCustomersToCSV("src/main/resources/customers.csv", customers);
        System.out.println("New PolicyHolder added.");
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