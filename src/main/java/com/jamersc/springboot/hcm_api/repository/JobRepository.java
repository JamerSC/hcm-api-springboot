package com.jamersc.springboot.hcm_api.repository;

import com.jamersc.springboot.hcm_api.entity.Job;
import com.jamersc.springboot.hcm_api.entity.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface JobRepository extends JpaRepository<Job, Long>,
        JpaSpecificationExecutor<Job>
{

    Page<Job> findByStatus(Pageable pageable, JobStatus status);

    Long countByStatus(JobStatus jobStatus);

//    @Query("SELECT j FROM Job j WHERE j.status IN ('OPEN','CLOSED')")
//    @Query("SELECT j FROM Job j WHERE j.status = 'OPEN'")
//    List<Job> findJobsWithStatusOpen();
}
