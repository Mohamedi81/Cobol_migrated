package com.example.customer.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CustomerCreateRequest(
        @NotBlank @Size(max = 20) String customerCode,
        @NotBlank @Size(max = 50) String firstName,
        @NotBlank @Size(max = 50) String lastName
) {}
