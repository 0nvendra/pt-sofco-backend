package com.example.test_pt_sofco_graha_gaji.id.controller;

import com.example.test_pt_sofco_graha_gaji.id.dto.AttendanceRequest;
import com.example.test_pt_sofco_graha_gaji.id.dto.AttendanceResponse;
import com.example.test_pt_sofco_graha_gaji.id.dto.AttendanceSummaryResponse;
import com.example.test_pt_sofco_graha_gaji.id.dto.WebResponse;
import com.example.test_pt_sofco_graha_gaji.id.service.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendances")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/submit")
    @ResponseStatus(HttpStatus.CREATED)
    public WebResponse<AttendanceResponse> submit(@Valid @RequestBody AttendanceRequest request) {
        AttendanceResponse response = attendanceService.submitAttendance(request);
        return WebResponse.<AttendanceResponse>builder()
                .status("success")
                .message("Attendance submitted successfully")
                .data(response)
                .build();
    }

    @GetMapping
    public WebResponse<List<AttendanceResponse>> list() {
        List<AttendanceResponse> responses = attendanceService.listAll();
        return WebResponse.<List<AttendanceResponse>>builder()
                .status("success")
                .message("Attendances listed successfully")
                .data(responses)
                .build();
    }

    @GetMapping("/me")
    public WebResponse<List<AttendanceResponse>> listMe() {
        List<AttendanceResponse> responses = attendanceService.getMyAttendances();
        return WebResponse.<List<AttendanceResponse>>builder()
                .status("success")
                .message("User attendances listed successfully")
                .data(responses)
                .build();
    }

    @GetMapping("/summary")
    public WebResponse<AttendanceSummaryResponse> getSummary() {
        AttendanceSummaryResponse response = attendanceService.getSummary();
        return WebResponse.<AttendanceSummaryResponse>builder()
                .status("success")
                .message("User attendance summary fetched successfully")
                .data(response)
                .build();
    }
}
