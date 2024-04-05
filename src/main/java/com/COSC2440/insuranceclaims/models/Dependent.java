package com.COSC2440.insuranceclaims.models;

public class Dependent extends Customer {
    private PolicyHolder policyHolder;

    public Dependent(String id, String fullName, InsuranceCard insuranceCard, PolicyHolder policyHolder) {
        super(id, fullName, insuranceCard);
        this.policyHolder = policyHolder;
    }
    public Dependent(String id, String fullName, PolicyHolder policyHolder) {
        super(id, fullName);
        this.policyHolder = policyHolder;
        this.insuranceCard = null; // Initially, there's no insurance card
    }

    // Getters and setters for policyHolder
    public PolicyHolder getPolicyHolder() {
        return policyHolder;
    }

    public void setPolicyHolder(PolicyHolder policyHolder) {
        this.policyHolder = policyHolder;
    }
    public String getPolicyHolderId() {
        return this.policyHolder.getId(); // Or just return this.policyHolderId; if storing the ID directly
    }

    @Override
    public String toString() {
        // Assuming the Customer class (superclass of Dependent) holds an InsuranceCard instance named 'insuranceCard'.
        // This instance is accessed via a getter method defined in Customer or Dependent (getInsuranceCard()).
        String cardNumber = (this.getInsuranceCard() != null) ? this.getInsuranceCard().getCardNumber() : "None";
        return "Dependent{" +
                "id='" + getId() + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", insuranceCardNumber='" + cardNumber + '\'' +
                ", policyHolder='" + (this.getPolicyHolder() != null ? this.getPolicyHolder().getFullName() : "No policy holder") + '\'' +
                '}';
    }

}
