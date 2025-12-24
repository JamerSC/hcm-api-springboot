package com.jamersc.springboot.hcm_api.repository;

import com.jamersc.springboot.hcm_api.entity.Employee;
import com.jamersc.springboot.hcm_api.entity.Job;
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

public class EmployeeSpecification {

    // ✅ Composable Specifications (best practice)

    /* ===================== SEARCH ===================== */
    public static Specification<Employee> search(String search) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(search)) {
                return cb.conjunction();
            }

            String pattern = "%" + search.toLowerCase() + "%";
            Join<Employee, Job> jobJoin = root.join("job", JoinType.LEFT);

            return cb.or(
                    cb.like(cb.lower(root.get("firstName")), pattern),
                    cb.like(cb.lower(root.get("lastName")), pattern),
                    cb.like(cb.lower(root.get("email")), pattern),
                    cb.like(cb.lower(jobJoin.get("title")), pattern)
            );
        };
    }

    /* ===================== JOB ===================== */
    public static Specification<Employee> hasJob(Long jobId) {
        //explicit and avoid implicit joins (useful in complex queries)
        return (root, query, cb) -> {
            if (jobId == null) {
                return cb.conjunction();
            }

            Join<Employee, Job> jobJoin = root.join("job", JoinType.LEFT);

            return cb.equal(jobJoin.get("id"), jobId);
        };
    }

    /* ===================== DATE RANGE ===================== */
    public static Specification<Employee> dateRange(
            LocalDate dateFrom,
            LocalDate dateTo
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (dateFrom != null) {
                OffsetDateTime start =
                        dateFrom.atStartOfDay()
                                .atOffset(ZoneOffset.UTC);

                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("createdAt"), start
                        ));
            }

            if (dateTo != null) {
                OffsetDateTime end =
                        dateTo.plusDays(1)
                                .atStartOfDay()
                                .atOffset(ZoneOffset.UTC);

                predicates.add(
                        cb.lessThan(
                                root.get("createdAt"), end
                        ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
