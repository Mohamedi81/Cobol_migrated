package com.example.policy.api;

import com.example.policy.api.dto.*;
import com.example.policy.service.PolicyBusinessException;
import com.example.policy.service.PolicyNotFoundException;
import com.example.policy.service.PolicyService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/policies")
public class PolicyController {

    private final PolicyService service;

    public PolicyController(PolicyService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PolicyDto> create(@Valid @RequestBody PolicyCreateRequest request) {
        PolicyDto dto = service.createPolicy(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/{policyNumber}")
    public PolicyDto get(@PathVariable String policyNumber) {
        return service.getPolicy(policyNumber);
    }

    @PatchMapping("/{policyNumber}")
    public PolicyDto update(@PathVariable String policyNumber,
                            @Valid @RequestBody PolicyUpdateRequest request) {
        return service.updatePolicy(policyNumber, request);
    }

    @PostMapping("/{policyNumber}:cancel")
    public PolicyDto cancel(@PathVariable String policyNumber,
                            @Valid @RequestBody CancelRequest request) {
        return service.cancelPolicy(policyNumber, request);
    }

    @GetMapping("/{policyNumber}/versions")
    public List<PolicyVersionDto> listVersions(@PathVariable String policyNumber) {
        return service.listVersions(policyNumber);
    }

    @GetMapping("/{policyNumber}/versions:asOf")
    public PolicyVersionDto asOf(@PathVariable String policyNumber,
                                 @RequestParam("date")
                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                 LocalDate date) {
        return service.getAsOf(policyNumber, date);
    }

    @ExceptionHandler(PolicyNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(PolicyNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(PolicyBusinessException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public String handleBusiness(PolicyBusinessException ex) {
        return ex.getMessage();
    }
}
