package com.COSC2440.insuranceclaims.models;

import java.util.ArrayList;
import java.util.List;

public class PolicyHolder extends Customer {
    private List<Dependent> dependents = new ArrayList<>();

    // Constructors, getters, and setters

    public List<Dependent> getDependents() {
        return dependents;
    }

    public void setDependents(List<Dependent> dependents) {
        this.dependents = dependents;
    }

    public PolicyHolder(String id, String fullName, InsuranceCard insuranceCard, List<Dependent> dependents) {
        super(id, fullName, insuranceCard);
        this.dependents = dependents;

    }
}
