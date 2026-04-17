package com.example.customer.api.dto;

public record CustomerDto(
        Long id,
        String customerCode,
        String firstName,
        String lastName,
        String status,
        String addressLine1,
        String addressLine2,
        String city,
        String postalCode,
        String phoneNumber,
        String email
) {}
