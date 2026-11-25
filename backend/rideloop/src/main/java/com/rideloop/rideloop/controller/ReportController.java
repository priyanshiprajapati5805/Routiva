
package com.rideloop.rideloop.controller;

import com.rideloop.rideloop.service.RideReportService;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportController {

    private final RideReportService rideReportService;

    public ReportController(RideReportService rideReportService) {
        this.rideReportService = rideReportService;
    }

    @PostMapping
    public Map<String, String> generateReport(
            @RequestBody Map<String, String> request) {

        String reportData = request.get("reportData");

        rideReportService.generateReport(reportData);

        return Map.of(
                "message",
                "Ride report generated successfully!"
        );
    }
}
