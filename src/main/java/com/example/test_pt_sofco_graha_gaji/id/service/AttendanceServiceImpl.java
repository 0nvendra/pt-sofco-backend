package com.example.test_pt_sofco_graha_gaji.id.service;

import com.example.test_pt_sofco_graha_gaji.id.exception.ApiException;
import com.example.test_pt_sofco_graha_gaji.id.dto.AttendanceRequest;
import com.example.test_pt_sofco_graha_gaji.id.dto.AttendanceResponse;
import com.example.test_pt_sofco_graha_gaji.id.dto.AttendanceSummaryResponse;
import com.example.test_pt_sofco_graha_gaji.id.model.Attendance;
import com.example.test_pt_sofco_graha_gaji.id.model.User;
import com.example.test_pt_sofco_graha_gaji.id.repository.AttendanceRepository;
import com.example.test_pt_sofco_graha_gaji.id.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public AttendanceResponse submitAttendance(AttendanceRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Submitting attendance for user: {}", username);

        // 1. Manual Validation: Check if user exists
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException("User not found: " + username, HttpStatus.NOT_FOUND));

        // 2. Business Logic Validation: Prevent double submission
        if (attendanceRepository.findByUserAndAttendanceDate(user, LocalDate.now()).isPresent()) {
            throw new ApiException("Attendance already submitted for today", HttpStatus.BAD_REQUEST);
        }

        // 3. Manual Validation: Check location data if required
        if (request.getAttendanceTimeInLatitude() == null || request.getAttendanceTimeInLongitude() == null) {
            throw new ApiException("Location coordinates (latitude and longitude) are mandatory", HttpStatus.BAD_REQUEST);
        }

        try {
            Attendance attendance = Attendance.builder()
                    .user(user)
                    .username(user.getUsername())
                    .attendanceDate(LocalDate.now())
                    .attendanceTimeIn(LocalTime.now())
                    .photoUrl(request.getPhotoUrl())
                    .attendanceTimeInLatitude(request.getAttendanceTimeInLatitude())
                    .attendanceTimeInLongitude(request.getAttendanceTimeInLongitude())
                    .isValid(true)
                    .build();

            attendance = attendanceRepository.save(attendance);
            log.info("Attendance saved successfully for user: {}", username);
            return mapToResponse(attendance);
        } catch (Exception e) {
            log.error("Failed to save attendance for user: {}", username, e);
            throw new ApiException("Failed to save attendance due to a database error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceResponse> listAll() {
        return attendanceRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceResponse> getMyAttendances() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
        
        return attendanceRepository.findByUser(user).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AttendanceSummaryResponse getSummary() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));

        List<Attendance> attendances = attendanceRepository.findByUser(user);
        
        long presentCount = attendances.stream().filter(a -> a.getIsValid()).count();
        long lateCount = attendances.stream().filter(a -> a.getAttendanceTimeIn().isAfter(LocalTime.of(8, 0))).count();

        return AttendanceSummaryResponse.builder()
                .presentCount(presentCount)
                .lateCount(lateCount)
                .absentCount(0) // Logic for absence could be added if roster is available
                .paidLeaveCount(0)
                .overtimeHours("0h")
                .attendanceRate(presentCount > 0 ? (double) presentCount / (presentCount + 1) * 100 : 0) // Placeholder
                .build();
    }

    private AttendanceResponse mapToResponse(Attendance attendance) {
        return AttendanceResponse.builder()
                .id(attendance.getId())
                .username(attendance.getUsername())
                .attendanceDate(attendance.getAttendanceDate())
                .attendanceTimeIn(attendance.getAttendanceTimeIn())
                .attendanceTimeOut(attendance.getAttendanceTimeOut())
                .photoUrl(attendance.getPhotoUrl())
                .attendanceTimeInLatitude(attendance.getAttendanceTimeInLatitude())
                .attendanceTimeInLongitude(attendance.getAttendanceTimeInLongitude())
                .attendanceTimeOutLatitude(attendance.getAttendanceTimeOutLatitude())
                .attendanceTimeOutLongitude(attendance.getAttendanceTimeOutLongitude())
                .isValid(attendance.getIsValid())
                .createdAt(attendance.getCreatedAt())
                .build();
    }
}
