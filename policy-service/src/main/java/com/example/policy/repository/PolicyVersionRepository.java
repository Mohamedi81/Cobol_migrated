package com.example.policy.repository;

import com.example.policy.domain.PolicyVersion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PolicyVersionRepository extends JpaRepository<PolicyVersion, Long> {

    List<PolicyVersion> findByPolicyIdOrderByVersionNumberAsc(Long policyId);

    Optional<PolicyVersion> findFirstByPolicyIdAndValidFromLessThanEqualAndValidToGreaterThanEqual(
            Long policyId, LocalDate from, LocalDate to);

    Optional<PolicyVersion> findFirstByPolicyIdAndValidFromLessThanEqualAndValidToIsNullOrderByVersionNumberDesc(
            Long policyId, LocalDate asOf);
}
