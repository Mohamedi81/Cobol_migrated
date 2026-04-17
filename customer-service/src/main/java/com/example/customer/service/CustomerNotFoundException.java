package com.example.customer.service;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(Long id) {
        super("Customer not found: " + id);
    }

    public CustomerNotFoundException(String code) {
        super("Customer not found: " + code);
    }
}
