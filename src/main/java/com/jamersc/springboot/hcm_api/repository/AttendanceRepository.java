package com.jamersc.springboot.hcm_api.repository;

import com.jamersc.springboot.hcm_api.entity.Attendance;
import com.jamersc.springboot.hcm_api.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long>,
        JpaSpecificationExecutor<Attendance>
{
    Optional<Attendance> findByEmployeeAndAttendanceDate(Employee employee, LocalDate today);
    Page<Attendance> findByEmployee(Pageable pageable, Employee employee);
}
