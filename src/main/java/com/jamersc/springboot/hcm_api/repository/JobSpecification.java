package com.jamersc.springboot.hcm_api.repository;

import com.jamersc.springboot.hcm_api.entity.Department;
import com.jamersc.springboot.hcm_api.entity.Job;
import com.jamersc.springboot.hcm_api.entity.JobStatus;
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

public class JobSpecification {

    // ✅ Composable Specifications (best practice)

    /* ===================== SEARCH ===================== */
    public static Specification<Job> search(String search) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(search)) {
                return cb.conjunction();
            }

            String pattern = "%" + search.toLowerCase() + "%";
            Join<Job, Department> departmentJoin = root.join("department", JoinType.LEFT);

            return cb.or(
                    cb.like(cb.lower(root.get("title")), pattern),
                    cb.like(cb.lower(root.get("description")), pattern),
                    cb.like(cb.lower(root.get("location")), pattern),
                    cb.like(cb.lower(departmentJoin.get("name")), pattern)
            );
        };
    }

    /* ===================== DEPARTMENT ===================== */
    public static Specification<Job> hasDepartment(Long departmentId) {
        //explicit and avoid implicit joins (useful in complex queries)
        return (root, query, cb) -> {
          if (departmentId == null) {
              return cb.conjunction();
          }

          Join<Job, Department> departmentJoin = root.join("department", JoinType.LEFT);

          return cb.equal(departmentJoin.get("id"), departmentId);
        };
    }

    /* ===================== STATUS ===================== */
    public static Specification<Job> hasStatus(JobStatus status) {
        return (root, query, cb) ->
                status == null
                        ? cb.conjunction()
                        : cb.equal(root.get("status"), status);
    }

    /* ===================== STATUS - OPEN - FOR APPLICANT ===================== */
    public static Specification<Job> hasStatusOpen() {
        return (root, query, cb) ->
                cb.equal(root.get("status"), JobStatus.OPEN);
    }

    /* ===================== DATE RANGE ===================== */
    public static Specification<Job> dateRange(
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
