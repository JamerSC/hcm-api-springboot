package com.jamersc.springboot.hcm_api.repository;

import com.jamersc.springboot.hcm_api.entity.Applicant;
import com.jamersc.springboot.hcm_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ApplicantRepository extends JpaRepository<Applicant, Long>,
        JpaSpecificationExecutor<Applicant>
{
    // Best practice: Add a method to find Applicant by User ID or User username
    // Since Applicant has a @OneToOne relationship with User, you can query by User's properties.
    // This is crucial for retrieving the *current authenticated user's* applicant profile.
    //Applicant findByUser_Username(String username);
    // Or, if you prefer to use the User entity directly:
    //Optional<Applicant> findByUser(User user);
    @Query("SELECT a FROM Applicant a " +
           "JOIN FETCH a.user u " +
           "WHERE u.username = :username")
    Applicant findApplicantByUsername(@Param("username") String username);

    @Query("SELECT a FROM Applicant a JOIN FETCH" +
           " a.user WHERE a.user = :user")
    Optional<Applicant> findByApplicantUser(@Param("user") User user);
}
