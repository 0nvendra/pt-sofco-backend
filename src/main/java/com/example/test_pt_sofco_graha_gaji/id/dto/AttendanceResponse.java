package com.example.test_pt_sofco_graha_gaji.id.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendanceResponse {
    private UUID id;
    private String username;
    private LocalDate attendanceDate;
    private LocalTime attendanceTimeIn;
    private LocalTime attendanceTimeOut;
    private String photoUrl;
    private BigDecimal attendanceTimeInLatitude;
    private BigDecimal attendanceTimeInLongitude;
    private BigDecimal attendanceTimeOutLatitude;
    private BigDecimal attendanceTimeOutLongitude;
    private Boolean isValid;
    private OffsetDateTime createdAt;
}
