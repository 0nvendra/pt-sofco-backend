package com.example.test_pt_sofco_graha_gaji.id.service;

import com.example.test_pt_sofco_graha_gaji.id.dto.AttendanceRequest;
import com.example.test_pt_sofco_graha_gaji.id.dto.AttendanceResponse;
import com.example.test_pt_sofco_graha_gaji.id.dto.AttendanceSummaryResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AttendanceService {
    AttendanceResponse submitAttendance(AttendanceRequest request, MultipartFile file);

    List<AttendanceResponse> listAll();

    List<AttendanceResponse> getMyAttendances();

    AttendanceSummaryResponse getSummary();
}
