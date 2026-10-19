package com.jamersc.springboot.hcm_api.entity;

import com.jamersc.springboot.hcm_api.config.StringListJsonConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.*;

@Entity
@Table(name = "applications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id", nullable = false, referencedColumnName = "id")
    private Applicant applicant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false, referencedColumnName = "id")
    private Job job;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status = ApplicationStatus.NEW;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private OffsetDateTime appliedAt;

    private String email;
    private String phone;
    private String mobile;
    private String linkedInProfile;
    private String degree;

    // FIX: Change Set<String> to List<String> to match the serialized ArrayList in the database
//    @Column(name = "employees")
    @Convert(converter = StringListJsonConverter.class)
    @Column(name = "employees", columnDefinition = "TEXT")
    private List<String> employees = new ArrayList<>();

    private String source;
    private Date availability;
    private Double expectedSalary;
    private Double proposedSalary;
    private String applicationSummary;
    private String skills;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id", referencedColumnName = "id")
    private User createdBy;

    @UpdateTimestamp
    @Column(nullable = false)
    private OffsetDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by_user_id", referencedColumnName = "id")
    private User updatedBy;
}
