package com.COSC2440.insuranceclaims.models;

public class Dependent extends Customer {
    private PolicyHolder policyHolder;

    public Dependent(String id, String fullName, InsuranceCard insuranceCard, PolicyHolder policyHolder) {
        super(id, fullName, insuranceCard);
        this.policyHolder = policyHolder;
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
        String cardNumber = (this.getInsuranceCard() != null) ? this.getInsuranceCard().getCardNumber() : "None";
        return "Dependent{" +
                "id='" + getId() + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", insuranceCardNumber='" + cardNumber + '\'' +
                ", policyHolder='" + (this.getPolicyHolder() != null ? this.getPolicyHolder().getFullName() : "No policy holder") + '\'' +
                '}';
    }
    // Other methods...
}
