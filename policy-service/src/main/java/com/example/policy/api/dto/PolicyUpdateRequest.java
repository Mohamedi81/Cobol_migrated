package com.example.policy.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PolicyUpdateRequest(
        LocalDate effectiveDate,
        BigDecimal premiumAmount,
        BigDecimal coverageAmount,
        String status,
        String changeReason
) {}
