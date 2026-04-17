package com.example.policy.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record PolicyDto(
        String policyNumber,
        Long customerId,
        String productCode,
        String status,
        PolicyVersionDto latestVersion,
        List<PolicyVersionDto> versions
) {}

public record PolicyVersionDto(
        Integer versionNumber,
        LocalDate validFrom,
        LocalDate validTo,
        BigDecimal premiumAmount,
        BigDecimal coverageAmount,
        String changeReason,
        String status
) {}
