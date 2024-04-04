package com.COSC2440.insuranceclaims.models;

import java.util.ArrayList;
import java.util.List;

public class PolicyHolder extends Customer {
    private List<Dependent> dependents;

    // Existing constructors, getters, and setters

    // Overloaded constructor for when you don't have InsuranceCard and dependents list upfront
    public PolicyHolder(String id, String fullName) {
        super(id, fullName, null); // Call to super with null for InsuranceCard
        this.dependents = new ArrayList<>(); // Initialize dependents list to be empty
    }

    public PolicyHolder(String id, String fullName, InsuranceCard insuranceCard, List<Dependent> dependents) {
        super(id, fullName, insuranceCard); // Existing constructor
        this.dependents = dependents != null ? dependents : new ArrayList<>();
    }

    // Getters and setters
    public List<Dependent> getDependents() {
        return dependents;
    }

    public void setDependents(List<Dependent> dependents) {
        this.dependents = dependents != null ? dependents : new ArrayList<>();
    }
    @Override
    public String toString() {
        String insuranceCardNumber = (getInsuranceCard() != null) ? getInsuranceCard().getCardNumber() : "No Insurance Card";
        return "PolicyHolder{" +
                "id='" + getId() + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", insuranceCardNumber='" + insuranceCardNumber + '\'' +
                // Include other relevant fields if necessary
                '}';
    }


}
