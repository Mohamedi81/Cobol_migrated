package com.example.policy.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PolicyCreateRequest(
        @NotBlank @Size(max = 30) String policyNumber,
        @NotNull Long customerId,
        @NotBlank @Size(max = 20) String productCode,
        @NotNull LocalDate effectiveDate,
        BigDecimal premiumAmount,
        BigDecimal coverageAmount,
        String status
) {}
