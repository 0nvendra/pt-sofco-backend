package com.example.test_pt_sofco_graha_gaji.id.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceSummaryResponse {
    private long presentCount;
    private long lateCount;
    private long absentCount;
    private long paidLeaveCount;
    private String overtimeHours;
    private double attendanceRate;
}
