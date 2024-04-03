package com.COSC2440.insuranceclaims.models;

public class Dependent extends Customer {
    public Dependent(String id, String fullName, InsuranceCard insuranceCard) {
        super(id, fullName, insuranceCard); // Explicitly calling the superclass constructor
    }
}
