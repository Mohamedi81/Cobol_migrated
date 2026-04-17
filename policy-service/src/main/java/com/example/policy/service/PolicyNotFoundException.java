package com.example.policy.service;

public class PolicyNotFoundException extends RuntimeException {
    public PolicyNotFoundException(String policyNumber) {
        super("Policy not found: " + policyNumber);
    }
}
