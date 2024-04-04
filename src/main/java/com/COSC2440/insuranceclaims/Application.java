package com.COSC2440.insuranceclaims;

import com.COSC2440.insuranceclaims.managers.SimpleClaimProcessManager;
import com.COSC2440.insuranceclaims.models.Customer;
import com.COSC2440.insuranceclaims.models.Dependent;
import com.COSC2440.insuranceclaims.models.InsuranceCard;
import com.COSC2440.insuranceclaims.ui.ConsoleUI;
import com.COSC2440.insuranceclaims.utils.FileManager;
import com.COSC2440.insuranceclaims.models.PolicyHolder;
import java.util.List;

public class Application {

    public static void main(String[] args) {
        List<Customer> customers = FileManager.readCustomers("src/main/resources/customers.csv");
        List<InsuranceCard> insuranceCards = FileManager.readInsuranceCards("src/main/resources/insurance_cards.csv", customers);

        // Associate insurance cards with their respective customers
        associateInsuranceCardsWithCustomers(customers, insuranceCards); // Assuming this exists to initially set cards
        associateInsuranceCardsWithDependents(customers); // Ensure dependents have their policyholder's card

        SimpleClaimProcessManager claimManager = new SimpleClaimProcessManager();
        ConsoleUI ui = new ConsoleUI(claimManager, customers, insuranceCards);
        ui.start();
    }

    private static void associateInsuranceCardsWithCustomers(List<Customer> customers, List<InsuranceCard> insuranceCards) {
        for (InsuranceCard card : insuranceCards) {
            // Assuming InsuranceCard has a method or field for getting the customer ID (getCardHolderId())
            // This example assumes a direct match by ID for simplicity
            customers.stream()
                    .filter(customer -> customer.getId().equals(card.getCardHolder().getId()))
                    .findFirst()
                    .ifPresent(customer -> customer.setInsuranceCard(card));
        }
    }

    private static void associateInsuranceCardsWithDependents(List<Customer> customers) {
        for (Customer customer : customers) {
            if (customer instanceof Dependent) {
                Dependent dependent = (Dependent) customer;
                // Correctly filter for the policyHolder using dependent's policyHolderId
                Customer policyHolder = customers.stream()
                        .filter(c -> c instanceof PolicyHolder && c.getId().equals(dependent.getPolicyHolderId()))
                        .findFirst()
                        .orElse(null);

                if (policyHolder != null && policyHolder instanceof PolicyHolder) {
                    dependent.setInsuranceCard(((PolicyHolder) policyHolder).getInsuranceCard());
                }
            }
        }
    }


}
