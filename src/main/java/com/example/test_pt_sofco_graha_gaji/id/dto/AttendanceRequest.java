package com.example.test_pt_sofco_graha_gaji.id.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendanceRequest {

    private String username;

    private String photoUrl;

    private BigDecimal attendanceTimeInLatitude;

    private BigDecimal attendanceTimeInLongitude;

    private BigDecimal attendanceTimeOutLatitude;

    private BigDecimal attendanceTimeOutLongitude;
}
