
package com.rideloop.rideloop.service;

import org.springframework.stereotype.Service;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class RideReportService {

    public void generateReport(String reportData) {

        try (FileWriter writer =
                     new FileWriter("ride-report.txt", true)) {

            writer.write(reportData);
            writer.write(System.lineSeparator());

            System.out.println(
                "Ride report saved successfully!"
            );

        } catch (IOException e) {

            System.out.println(
                "Error generating ride report: "
                + e.getMessage()
            );
        }
    }
}
