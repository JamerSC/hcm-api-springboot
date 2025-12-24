package com.jamersc.springboot.hcm_api.repository;

import com.jamersc.springboot.hcm_api.entity.Attendance;
import com.jamersc.springboot.hcm_api.entity.AttendanceStatus;
import com.jamersc.springboot.hcm_api.entity.Employee;
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

public class AttendanceSpecification {

    // ✅ Composable Specifications (best practice)

    /* ===================== SEARCH ===================== */
    public static Specification<Attendance> search(String search) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(search)) {
                return cb.conjunction();
            }

            String pattern = "%" + search.toLowerCase() + "%";
            Join<Attendance, Employee> employeeJoin =
                    root.join("employee", JoinType.LEFT);

            return cb.or(
                    cb.like(cb.lower(employeeJoin.get("firstName")), pattern),
                    cb.like(cb.lower(employeeJoin.get("lastName")), pattern)
            );
        };
    }

    /* ===================== STATUS ===================== */
    public static Specification<Attendance> hasStatus(AttendanceStatus status) {
        return (root, query, cb) ->
                status == null
                        ? cb.conjunction()
                        : cb.equal(root.get("status"), status);
    }

    /* ===================== DATE RANGE ===================== */
    public static Specification<Attendance> dateRange(
            LocalDate dateFrom,
            LocalDate dateTo
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (dateFrom != null) {
                OffsetDateTime start =
                        dateFrom.atStartOfDay().atOffset(ZoneOffset.UTC);

                predicates.add(cb.greaterThanOrEqualTo(
                        root.get("attendanceDate"), start
                ));
            }

            if (dateTo != null) {
                OffsetDateTime end =
                        dateTo.plusDays(1).atStartOfDay().atOffset(ZoneOffset.UTC);

                predicates.add(cb.lessThan(
                        root.get("attendanceDate"), end
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    /* ===================== EMPLOYEE ===================== */
    public static Specification<Attendance> byEmployee(Employee employee) {
        return (root, query, cb) ->
                employee == null
                        ? cb.conjunction()
                        : cb.equal(root.get("employee"), employee);
    }
}
