package com.jamersc.springboot.hcm_api.repository;

import com.jamersc.springboot.hcm_api.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long>,
        JpaSpecificationExecutor<Employee>
{
    // find employee profile by id
    @Query("SELECT e FROM Employee e JOIN FETCH e.user u JOIN FETCH u.roles WHERE e.id =:id")
    Optional<Employee> findEmployeeWithUserAndRolesById(@Param("id") Long id);

    // get employee profile by username
    @Query("SELECT e FROM Employee e JOIN FETCH e.user u JOIN FETCH u.roles WHERE u.username = :username")
    Employee findEmployeeByUsername(@Param("username") String username);

    @Query("SELECT e FROM Employee e JOIN FETCH e.job j JOIN FETCH j.department WHERE e.id =:id")
    Optional<Employee> findEmployeeWithJobAndDepartmentInfoById(@Param("id") Long id);
}
