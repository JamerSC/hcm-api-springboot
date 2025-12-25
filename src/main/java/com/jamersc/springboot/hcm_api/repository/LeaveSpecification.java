package com.jamersc.springboot.hcm_api.repository;

import com.jamersc.springboot.hcm_api.entity.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class LeaveSpecification {

    // ✅ Composable Specifications (best practice)

    /* ===================== SEARCH ===================== */
    public static Specification<Leave> search(String search) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(search)) {
                return cb.conjunction();
            }

            String pattern = "%" + search.toLowerCase() + "%";
            Join<Leave, Employee> employeeJoin =
                    root.join("employee", JoinType.LEFT);

            return cb.or(
                    cb.like(cb.lower(employeeJoin.get("firstName")), pattern),
                    cb.like(cb.lower(employeeJoin.get("lastName")), pattern)
            );
        };
    }

    /* ===================== TYPE ===================== */
    public static Specification<Leave> hasType(LeaveType leaveType) {
        return (root, query, cb) ->
               leaveType == null
                        ? cb.conjunction()
                        : cb.equal(root.get("leaveType"), leaveType);
    }


    /* ===================== STATUS ===================== */
    public static Specification<Leave> hasStatus(LeaveStatus leaveStatus) {
        return (root, query, cb) ->
                leaveStatus == null
                        ? cb.conjunction()
                        : cb.equal(root.get("status"), leaveStatus);
    }

    /* ===================== DATE RANGE ===================== */
    public static Specification<Leave> dateRange(
            LocalDate dateFrom,
            LocalDate dateTo
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (dateFrom != null) {
                OffsetDateTime start =
                        dateFrom.atStartOfDay().atOffset(ZoneOffset.UTC);

                predicates.add(cb.greaterThanOrEqualTo(
                        root.get("createdAt"), start
                ));
            }

            if (dateTo != null) {
                OffsetDateTime end =
                        dateTo.plusDays(1).atStartOfDay().atOffset(ZoneOffset.UTC);

                predicates.add(cb.lessThan(
                        root.get("createdAt"), end
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    /* ===================== EMPLOYEE ===================== */
    public static Specification<Leave> byEmployee(Employee employee) {
        return (root, query, cb) ->
                employee == null
                        ? cb.conjunction()
                        : cb.equal(root.get("employee"), employee);
    }
}
