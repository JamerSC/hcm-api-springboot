package com.jamersc.springboot.hcm_api.repository;

import com.jamersc.springboot.hcm_api.entity.Job;
import com.jamersc.springboot.hcm_api.entity.JobStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class JobSpecification {

    /**
     * For Manager/Admin
     */
    public static Specification<Job> getJobs(
            String search,
            Long departmentId,
            JobStatus status,
            LocalDate dateFrom,
            LocalDate dateTo
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Search Logic (Title or Description)
            if (StringUtils.hasText(search)) {
                String searchPattern = "%" + search.toLowerCase() + "%";
                Predicate titlePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), searchPattern);
                Predicate descPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), searchPattern);
                Predicate descLocation = criteriaBuilder.like(criteriaBuilder.lower(root.get("location")), searchPattern);
                predicates.add(criteriaBuilder.or(titlePredicate, descPredicate, descLocation));
            }

            // 2. Department Filter (Relationship Join)
            if (departmentId != null) {
                // "department" matches the field name in Job entity: private Department department;
                // "id" matches the field name in Department entity: private Long id;
                predicates.add(criteriaBuilder.equal(root.get("department").get("id"), departmentId));
            }

            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            if (dateFrom != null) {
                OffsetDateTime startDateTime =
                        dateFrom.atStartOfDay()
                                .atOffset(ZoneOffset.UTC);

                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), startDateTime)
                );
            }

            if (dateTo != null) {
                OffsetDateTime endDateTime =
                        dateTo.plusDays(1)
                                .atStartOfDay()
                                .atOffset(ZoneOffset.UTC);

                predicates.add(
                        criteriaBuilder.lessThan(root.get("createdAt"), endDateTime)
                );
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * For Applicants
     */
    public static Specification<Job> getOpenJobs(
            String search,
            LocalDate dateFrom,
            LocalDate dateTo
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(
                    criteriaBuilder.equal(root.get("status"), JobStatus.OPEN)
            );


            // 1. Search Logic (Title or Description)
            if (StringUtils.hasText(search)) {
                String searchPattern = "%" + search.toLowerCase() + "%";
                Predicate titlePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), searchPattern);
                Predicate descPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), searchPattern);
                Predicate descLocation = criteriaBuilder.like(criteriaBuilder.lower(root.get("location")), searchPattern);
                predicates.add(criteriaBuilder.or(titlePredicate, descPredicate, descLocation));
            }

            if (dateFrom != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                                criteriaBuilder.function(
                                        "date",
                                        LocalDate.class,
                                        root.get("postedDate") // OffsetDateTime column
                                ),
                                dateFrom
                        )
                );
            }

            if (dateTo != null) {
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(
                                criteriaBuilder.function(
                                        "date",
                                        LocalDate.class,
                                        root.get("postedDate")
                                ),
                                dateTo
                        )
                );
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
