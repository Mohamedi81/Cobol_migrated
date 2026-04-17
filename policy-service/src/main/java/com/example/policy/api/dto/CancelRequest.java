package com.example.policy.api.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CancelRequest(
        @NotNull LocalDate cancellationDate,
        String reason
) {}
