package com.COSC2440.insuranceclaims.managers;

import com.COSC2440.insuranceclaims.models.Claim;
import com.COSC2440.insuranceclaims.models.Customer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class SimpleClaimProcessManager implements ClaimProcessManager {
    private List<Claim> claims;

    public SimpleClaimProcessManager() {

        this.claims = new ArrayList<>();
        // Optionally, initialize this list from the claims.csv file
        loadClaimsFromCSV();
    }

    @Override
    public void add(Claim claim) {
        // Check for duplicate IDs before adding
        for (Claim existingClaim : claims) {
            if (existingClaim.getId().equals(claim.getId())) {
                System.out.println("A claim with the same ID already exists.");
                return;
            }
        }
        claims.add(claim);
        saveClaimsToCSV();
    }

    @Override
    public List<Claim> getAll() {
        return new ArrayList<>(claims);
    }
    private void saveClaimsToCSV() {
        String filePath = "src/main/resources/claims.csv"; // Adjust the path as necessary.
        try (PrintWriter writer = new PrintWriter(filePath)) {
            writer.println("ID,ClaimDate,InsuredPerson,CardNumber,ExamDate,Documents,ClaimAmount,Status,ReceiverBankingInfo");
            for (Claim claim : claims) {
                List<String> documents = claim.getDocuments();
                String documentsJoined = String.join(";", documents);
                String line = String.format("%s,%s,%s,%s,%s,%s,%f,%s,%s",
                        claim.getId(),
                        new SimpleDateFormat("dd/MM/yyyy").format(claim.getClaimDate()),
                        claim.getInsuredPerson().getId(), // Assuming InsuredPerson is a Customer with an ID.
                        claim.getCardNumber(),
                        new SimpleDateFormat("dd/MM/yyyy").format(claim.getExamDate()),
                        documentsJoined,
                        claim.getClaimAmount(),
                        claim.getStatus(),
                        claim.getReceiverBankingInfo());
                writer.println(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while saving to CSV.");
            e.printStackTrace();
        }
    }
    private void loadClaimsFromCSV() {
        String filePath = "src/main/resources/claims.csv"; // Ensure this path is correct
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        System.out.println("Loading claims from CSV..."); // Indicates the method has started executing

        try (Scanner scanner = new Scanner(new File(filePath))) {
            if (scanner.hasNextLine()) { // Skip header
                scanner.nextLine();
            }
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println("Line read: " + line); // Outputs each line read from the CSV

                if (line.isEmpty() || line.trim().startsWith("ClaimID")) continue; // Adjust if your CSV has a different header

                String[] data = line.split(","); // Ensure your CSV does not contain commas within fields without proper handling

                // Parsing logic here...
                String id = data[0];
                Date claimDate = dateFormat.parse(data[1]);
                // Assuming insuredPerson is identified by ID only for simplicity, adjust as needed.
                Customer insuredPerson = new Customer(data[2], "Placeholder Name"); // Placeholder, replace with actual logic
                String cardNumber = data[3];
                Date examDate = dateFormat.parse(data[4]);
                List<String> documents = Arrays.asList(data[5].split(";"));
                double claimAmount = Double.parseDouble(data[6]);
                String status = data[7];
                String receiverBankingInfo = data[8];

                Claim claim = new Claim(id, claimDate, insuredPerson, cardNumber, examDate, documents, claimAmount, status, receiverBankingInfo);
                this.claims.add(claim);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error reading the CSV file: " + e.getMessage()); // File not found error
        } catch (ParseException e) {
            System.out.println("Error parsing the date from the CSV file: " + e.getMessage()); // Date parsing error
        }
    }
    @Override
    public void update(String id, Claim updatedClaim) {
        for (int i = 0; i < claims.size(); i++) {
            if (claims.get(i).getId().equals(id)) {
                claims.set(i, updatedClaim);
                saveClaimsToCSV(); // Persist changes
                System.out.println("Claim updated successfully.");
                return;
            }
        }
        System.out.println("Claim with ID " + id + " not found.");
    }
    @Override
    public void delete(String id) {
        boolean removed = claims.removeIf(claim -> claim.getId().equals(id));
        if (removed) {
            saveClaimsToCSV(); // Persist changes
            System.out.println("Claim deleted successfully.");
        } else {
            System.out.println("Claim with ID " + id + " not found.");
        }
    }
    @Override
    public Claim getOne(String id) {
        for (Claim claim : claims) {
            if (claim.getId().equals(id)) {
                return claim;
            }
        }
        System.out.println("Claim with ID " + id + " not found.");
        return null;
    }



}