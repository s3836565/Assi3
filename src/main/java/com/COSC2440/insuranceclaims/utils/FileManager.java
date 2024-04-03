package com.COSC2440.insuranceclaims.utils;

import com.COSC2440.insuranceclaims.models.Claim;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }
    public static Claim readClaimById(String filePath, String claimId) {
        List<String> lines = readLines(filePath);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        for (String line : lines) {
            String[] data = line.split(",");
            if (data[0].equals(claimId)) { // Assuming the ID is the first column
                try {
                    // Assuming order: id, claimDate, insuredPersonId, cardNumber, examDate, documents, claimAmount, status, receiverBankingInfo
                    // This will need adjustments based on your actual CSV structure and how you manage related objects like Customer
                    Date claimDate = dateFormat.parse(data[1]);
                    Date examDate = dateFormat.parse(data[4]);
                    List<String> documents = Arrays.asList(data[5].split(";")); // Assuming documents are separated by semicolons
                    double claimAmount = Double.parseDouble(data[6]);
                    // You'll need to fetch or create a Customer object for insuredPerson based on insuredPersonId (data[2])
                    return new Claim(data[0], claimDate, null, data[3], examDate, documents, claimAmount, data[7], data[8]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null; // Return null if no claim with the given ID is found
    }
}
