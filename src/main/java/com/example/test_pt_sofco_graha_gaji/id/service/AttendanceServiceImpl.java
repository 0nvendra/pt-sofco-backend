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

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final FirebaseStorageService firebaseStorageService;

    @Override
    @Transactional
    public AttendanceResponse submitAttendance(AttendanceRequest request, MultipartFile file) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Submitting attendance for user: {}", username);

        // 1. Manual Validation: Check if user exists
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException("User not found: " + username, HttpStatus.NOT_FOUND));

        // 2. Business Logic Validation: Prevent double submission
        if (attendanceRepository.findByUserAndAttendanceDate(user, LocalDate.now()).isPresent()) {
            throw new ApiException("Attendance already submitted for today", HttpStatus.BAD_REQUEST);
        }

        // 3. Manual Validation: Check location data
        if (request.getAttendanceTimeInLatitude() == null || request.getAttendanceTimeInLongitude() == null) {
            throw new ApiException("Location coordinates are mandatory", HttpStatus.BAD_REQUEST);
        }

        // 4. Validasi File: Pastikan file tidak kosong
        if (file == null || file.isEmpty()) {
            throw new ApiException("Photo is mandatory for attendance", HttpStatus.BAD_REQUEST);
        }

        try {
            // 5. Upload ke Firebase Storage
            // Kita lakukan ini SEBELUM save ke DB agar jika upload gagal, DB tidak terisi
            String uploadedUrl = firebaseStorageService.uploadImage(file);
            log.info("Photo uploaded to Firebase: {}", uploadedUrl);

            // 6. Build Entity dengan URL dari Firebase
            Attendance attendance = Attendance.builder()
                    .user(user)
                    .username(user.getUsername())
                    .attendanceDate(LocalDate.now())
                    .attendanceTimeIn(LocalTime.now())
                    .photoUrl(uploadedUrl) // Menggunakan URL hasil upload tadi
                    .attendanceTimeInLatitude(request.getAttendanceTimeInLatitude())
                    .attendanceTimeInLongitude(request.getAttendanceTimeInLongitude())
                    .isValid(true)
                    .build();

            attendance = attendanceRepository.save(attendance);
            log.info("Attendance saved successfully for user: {}", username);

            return mapToResponse(attendance);

        } catch (IOException e) {
            log.error("Firebase upload failed for user: {}", username, e);
            throw new ApiException("Failed to upload photo to storage", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error("Failed to save attendance for user: {}. Cause: {}", username, e.getMessage(), e);
            throw new ApiException("Failed to save attendance: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
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
