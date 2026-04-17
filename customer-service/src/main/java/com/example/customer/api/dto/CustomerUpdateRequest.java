package com.example.customer.api.dto;

import jakarta.validation.constraints.Size;

public record CustomerUpdateRequest(
        @Size(max = 100) String addressLine1,
        @Size(max = 100) String addressLine2,
        @Size(max = 50) String city,
        @Size(max = 20) String postalCode,
        @Size(max = 30) String phoneNumber,
        @Size(max = 100) String email
) {}
