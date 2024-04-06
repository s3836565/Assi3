package com.COSC2440.insuranceclaims.managers;

import com.COSC2440.insuranceclaims.models.Claim;

import java.util.List;

public interface ClaimProcessManager {
    void add(Claim claim);
    void update(String claimId, Claim updatedClaim);
    void delete(String claimId);
    Claim getOne(String claimId);
    List<Claim> getAll();




}