package com.jamersc.springboot.hcm_api.repository;

import com.jamersc.springboot.hcm_api.entity.Applicant;
import com.jamersc.springboot.hcm_api.entity.Application;
import com.jamersc.springboot.hcm_api.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long>,
        JpaSpecificationExecutor<Application>
{
    // custom query to check if an applicant has already applied for a specific job
    Optional<Application> findByApplicantAndJob(Applicant applicant, Job job);

    //    List<Application> findApplicantApplicationsById(Long id);

    @Query("SELECT a FROM Application a JOIN FETCH a.applicant b WHERE b.id = :id")
    Page<Application> findApplicantApplicationsById(Pageable pageable, @Param("id") Long id);

    List<Application> findByApplicant(Applicant applicant);

    Optional<Application> findByApplicantAndId(Applicant applicant, Long id);
}
