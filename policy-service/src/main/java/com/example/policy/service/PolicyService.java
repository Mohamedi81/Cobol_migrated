package com.example.policy.service;

import com.example.policy.api.dto.*;
import com.example.policy.domain.Policy;
import com.example.policy.domain.PolicyVersion;
import com.example.policy.repository.PolicyRepository;
import com.example.policy.repository.PolicyVersionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
public class PolicyService {

    private final PolicyRepository policyRepository;
    private final PolicyVersionRepository versionRepository;

    public PolicyService(PolicyRepository policyRepository,
                         PolicyVersionRepository versionRepository) {
        this.policyRepository = policyRepository;
        this.versionRepository = versionRepository;
    }

    @Transactional
    public PolicyDto createPolicy(PolicyCreateRequest request) {
        policyRepository.findByPolicyNumber(request.policyNumber())
                .ifPresent(p -> { throw new PolicyBusinessException("Policy number already exists"); });

        Policy policy = new Policy();
        policy.setPolicyNumber(request.policyNumber());
        policy.setCustomerId(request.customerId());
        policy.setProductCode(request.productCode());
        policy.setStatus(request.status() != null ? request.status() : "ACTIVE");

        PolicyVersion version = new PolicyVersion();
        version.setPolicy(policy);
        version.setVersionNumber(1);
        version.setValidFrom(request.effectiveDate());
        version.setValidTo(null);
        version.setPremiumAmount(request.premiumAmount());
        version.setCoverageAmount(request.coverageAmount());
        version.setChangeReason("Initial creation");
        version.setStatus(policy.getStatus());

        policy.getVersions().add(version);

        Policy saved = policyRepository.save(policy);
        return toDto(saved, true);
    }

    @Transactional(readOnly = true)
    public PolicyDto getPolicy(String policyNumber) {
        Policy policy = policyRepository.findByPolicyNumber(policyNumber)
                .orElseThrow(() -> new PolicyNotFoundException(policyNumber));
        return toDto(policy, true);
    }

    @Transactional
    public PolicyDto updatePolicy(String policyNumber, PolicyUpdateRequest request) {
        Policy policy = policyRepository.findByPolicyNumber(policyNumber)
                .orElseThrow(() -> new PolicyNotFoundException(policyNumber));

        int nextVersion = policy.getVersions().stream()
                .map(PolicyVersion::getVersionNumber)
                .max(Integer::compareTo)
                .orElse(0) + 1;

        PolicyVersion latest = latestVersion(policy);

        LocalDate newEffectiveDate = request.effectiveDate() != null
                ? request.effectiveDate()
                : latest.getValidFrom();

        if (request.effectiveDate() != null && request.effectiveDate().isBefore(latest.getValidFrom())) {
            throw new PolicyBusinessException("New effective date cannot be before current effective date");
        }

        latest.setValidTo(newEffectiveDate.minusDays(1));

        PolicyVersion newVersion = new PolicyVersion();
        newVersion.setPolicy(policy);
        newVersion.setVersionNumber(nextVersion);
        newVersion.setValidFrom(newEffectiveDate);
        newVersion.setValidTo(null);
        newVersion.setPremiumAmount(
                request.premiumAmount() != null ? request.premiumAmount() : latest.getPremiumAmount());
        newVersion.setCoverageAmount(
                request.coverageAmount() != null ? request.coverageAmount() : latest.getCoverageAmount());
        newVersion.setChangeReason(request.changeReason());
        newVersion.setStatus(request.status() != null ? request.status() : policy.getStatus());

        policy.getVersions().add(newVersion);
        if (request.status() != null) {
            policy.setStatus(request.status());
        }

        return toDto(policy, true);
    }

    @Transactional
    public PolicyDto cancelPolicy(String policyNumber, CancelRequest request) {
        Policy policy = policyRepository.findByPolicyNumber(policyNumber)
                .orElseThrow(() -> new PolicyNotFoundException(policyNumber));

        PolicyVersion latest = latestVersion(policy);
        LocalDate cancellationDate = request.cancellationDate();

        if (latest.getValidFrom().isAfter(cancellationDate)) {
            throw new PolicyBusinessException("Cancellation date before policy effective date");
        }

        latest.setValidTo(cancellationDate);
        policy.setStatus("CANCELLED");

        return toDto(policy, true);
    }

    @Transactional(readOnly = true)
    public List<PolicyVersionDto> listVersions(String policyNumber) {
        Policy policy = policyRepository.findByPolicyNumber(policyNumber)
                .orElseThrow(() -> new PolicyNotFoundException(policyNumber));

        return policy.getVersions().stream()
                .sorted(Comparator.comparing(PolicyVersion::getVersionNumber))
                .map(PolicyService::toVersionDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public PolicyVersionDto getAsOf(String policyNumber, LocalDate asOfDate) {
        Policy policy = policyRepository.findByPolicyNumber(policyNumber)
                .orElseThrow(() -> new PolicyNotFoundException(policyNumber));

        return policy.getVersions().stream()
                .filter(v -> !v.getValidFrom().isAfter(asOfDate) &&
                        (v.getValidTo() == null || !v.getValidTo().isBefore(asOfDate)))
                .min(Comparator.comparing(PolicyVersion::getVersionNumber))
                .map(PolicyService::toVersionDto)
                .orElseThrow(() -> new PolicyBusinessException("No policy version effective on " + asOfDate));
    }

    private static PolicyVersion latestVersion(Policy policy) {
        return policy.getVersions().stream()
                .max(Comparator.comparing(PolicyVersion::getVersionNumber))
                .orElseThrow(() -> new IllegalStateException("Policy has no versions"));
    }

    private static PolicyDto toDto(Policy policy, boolean includeVersions) {
        PolicyVersion latest = latestVersion(policy);
        List<PolicyVersionDto> versionDtos = includeVersions
                ? policy.getVersions().stream()
                    .sorted(Comparator.comparing(PolicyVersion::getVersionNumber))
                    .map(PolicyService::toVersionDto)
                    .toList()
                : List.of();

        return new PolicyDto(
                policy.getPolicyNumber(),
                policy.getCustomerId(),
                policy.getProductCode(),
                policy.getStatus(),
                toVersionDto(latest),
                versionDtos
        );
    }

    private static PolicyVersionDto toVersionDto(PolicyVersion v) {
        return new PolicyVersionDto(
                v.getVersionNumber(),
                v.getValidFrom(),
                v.getValidTo(),
                v.getPremiumAmount(),
                v.getCoverageAmount(),
                v.getChangeReason(),
                v.getStatus()
        );
    }
}
