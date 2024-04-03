package com.COSC2440.insuranceclaims.managers;

import com.COSC2440.insuranceclaims.models.Claim;

import java.util.ArrayList;
import java.util.List;

public class SimpleClaimProcessManager implements ClaimProcessManager {
    private List<Claim> claims = new ArrayList<>();

    @Override
    public void add(Claim claim) {
        claims.add(claim);
    }

    @Override
    public void update(String claimId, Claim updatedClaim) {
        claims.replaceAll(claim -> claim.getId().equals(claimId) ? updatedClaim : claim);
    }

    @Override
    public void delete(String claimId) {
        claims.removeIf(claim -> claim.getId().equals(claimId));
    }

    @Override
    public Claim getOne(String claimId) {
        return claims.stream().filter(claim -> claim.getId().equals(claimId)).findFirst().orElse(null);
    }

    @Override
    public List<Claim> getAll() {
        return new ArrayList<>(claims);
    }
}
