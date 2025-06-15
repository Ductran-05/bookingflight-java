package com.bookingflight.app.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bookingflight.app.dto.response.APIResponse;
import com.bookingflight.app.dto.response.DashboardResponse;
import com.bookingflight.app.dto.response.YearlyTicketResponse;
import com.bookingflight.app.service.DashboardReportService;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DashboardReportController {
    final DashboardReportService dashboardReportService;

    @GetMapping
    public ResponseEntity<APIResponse<DashboardResponse>> getDashboardReport() {
        return dashboardReportService.getDashboardReport();
    }

    @GetMapping("/{year}")
    public ResponseEntity<APIResponse<YearlyTicketResponse>> getYearlyTicketReport(@PathVariable int year) {
        return dashboardReportService.getYearlyTicketReport(year);
    }
}