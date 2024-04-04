package com.COSC2440.insuranceclaims.utils;

import com.COSC2440.insuranceclaims.models.Claim;
import com.COSC2440.insuranceclaims.models.Customer;
import com.COSC2440.insuranceclaims.models.Dependent;
import com.COSC2440.insuranceclaims.models.PolicyHolder;
import com.COSC2440.insuranceclaims.models.InsuranceCard;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class FileManager {

    // Generic method to read lines from a CSV file
    public static List<String> readLines(String filePath) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return lines;
    }

    // Generic method to write lines to a CSV file
    public static void writeLines(String filePath, List<String> lines) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    // Method to read a claim by ID
    public static Claim readClaimById(String filePath, String claimId) {
        List<String> lines = readLines(filePath);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        for (String line : lines) {
            String[] data = line.split(",");
            if (data[0].equals(claimId)) {
                try {
                    Date claimDate = dateFormat.parse(data[1]);
                    Date examDate = dateFormat.parse(data[4]);
                    List<String> documents = Arrays.asList(data[5].split(";"));
                    double claimAmount = Double.parseDouble(data[6]);

                    // Placeholder for Customer object; actual implementation needed
                    return new Claim(data[0], claimDate, null, data[3], examDate, documents, claimAmount, data[7], data[8]);
                } catch (ParseException e) {
                    System.err.println("Error parsing date: " + e.getMessage());
                    return null;
                }
            }
        }
        return null; // Return null if no claim with the given ID is found
    }

    // Method to read customers from a CSV file
    public static List<Customer> readCustomers(String filePath) {
        List<Customer> customers = new ArrayList<>();
        int lineNumber = 0; // Initialize before the loop
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // Skip the header, assuming the first line is always a header
            lineNumber++; // Increment since we've read the header line
            String line;
            while ((line = br.readLine()) != null) {
                lineNumber++; // Increment for each line read
                String[] data = line.split(",");
                if (data.length < 4) { // Ensure at least ID, fullName, type, and policyHolderId (for dependents) are present
                    System.err.println("Skipping malformed line at " + lineNumber + ": " + line);
                    continue;
                }

                String id = data[0];
                String fullName = data[1];
                String type = data[2];

                // Directly creating PolicyHolder or Dependent without insurance card number handling here
                if ("PolicyHolder".equals(type)) {
                    PolicyHolder policyHolder = new PolicyHolder(id, fullName, null, new ArrayList<>());
                    customers.add(policyHolder);
                } else if ("Dependent".equals(type)) {
                    String policyHolderId = data[3]; // Dependent must have a policyHolderId
                    PolicyHolder policyHolder = findPolicyHolderById(customers, policyHolderId);
                    if (policyHolder != null) {
                        Dependent dependent = new Dependent(id, fullName, policyHolder.getInsuranceCard(), policyHolder);
                        customers.add(dependent);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading customer file: " + e.getMessage());
        }
        return customers;
    }



    public static PolicyHolder findPolicyHolderById(List<Customer> customers, String policyHolderId) {
        for (Customer customer : customers) {
            if (customer instanceof PolicyHolder && customer.getId().equals(policyHolderId)) {
                return (PolicyHolder) customer;
            }
        }
        return null; // No matching policy holder found
    }

    public static List<InsuranceCard> readInsuranceCards(String filePath, List<Customer> customers) {
        List<InsuranceCard> insuranceCards = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Skip empty lines
                String[] data = line.split(",");
                if (data.length < 4 || "ExpirationDate".equals(data[3])) continue; // Skip malformed lines or the header repeated

                String cardNumber = data[0];
                String cardHolderId = data[1];
                String policyOwnerId = data[2];
                Date expirationDate;
                try {
                    expirationDate = dateFormat.parse(data[3]);
                } catch (ParseException e) {
                    System.err.println("Skipping line due to date parse error: " + line);
                    continue;
                }

                // Find the corresponding card holder (customer)
                Customer cardHolder = customers.stream()
                        .filter(customer -> customer.getId().equals(cardHolderId))
                        .findFirst()
                        .orElse(null);

                // Create and associate the insurance card with the card holder
                if (cardHolder != null) {
                    InsuranceCard insuranceCard = new InsuranceCard(cardNumber, cardHolder, null, expirationDate);
                    insuranceCards.add(insuranceCard);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading insurance cards file: " + e.getMessage());
        }
        return insuranceCards;
    }





}
