package com.jamersc.springboot.hcm_api.controller;

import com.jamersc.springboot.hcm_api.dto.dashboard.DashboardDto;
import com.jamersc.springboot.hcm_api.service.dashboard.DashboardService;
import com.jamersc.springboot.hcm_api.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @PreAuthorize("hasAuthority('VIEW_DASHBOARD')")
    @GetMapping("/")
    public ResponseEntity<ApiResponse<DashboardDto>> getDashboardData() {
        DashboardDto retrievedDashboardData = dashboardService.getDashboardData();
        ApiResponse<DashboardDto> response = ApiResponse.<DashboardDto>builder()
                .success(true)
                .message("Dashboard data retrieved successfully!")
                .data(retrievedDashboardData)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }
}
