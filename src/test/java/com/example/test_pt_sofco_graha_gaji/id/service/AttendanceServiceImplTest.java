package com.example.test_pt_sofco_graha_gaji.id.service;

import com.example.test_pt_sofco_graha_gaji.id.dto.AttendanceRequest;
import com.example.test_pt_sofco_graha_gaji.id.dto.AttendanceResponse;
import com.example.test_pt_sofco_graha_gaji.id.exception.ApiException;
import com.example.test_pt_sofco_graha_gaji.id.model.Attendance;
import com.example.test_pt_sofco_graha_gaji.id.model.User;
import com.example.test_pt_sofco_graha_gaji.id.repository.AttendanceRepository;
import com.example.test_pt_sofco_graha_gaji.id.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AttendanceServiceImpl Unit Tests")
class AttendanceServiceImplTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FirebaseStorageService firebaseStorageService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AttendanceServiceImpl attendanceService;

    private User mockUser;
    private AttendanceRequest attendanceRequest;
    private MockMultipartFile mockFile;

    @BeforeEach
    void setUp() {
        mockUser = User.builder()
                .id(UUID.randomUUID())
                .username("johndoe")
                .fullName("John Doe")
                .build();

        attendanceRequest = AttendanceRequest.builder()
                .attendanceTimeInLatitude(new BigDecimal("-6.17511"))
                .attendanceTimeInLongitude(new BigDecimal("106.86503"))
                .build();

        mockFile = new MockMultipartFile(
                "image", "test.jpg", "image/jpeg", "test image content".getBytes());

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("submitAttendance() - should save attendance when valid")
    void submitAttendance_withValidData_shouldReturnResponse() throws IOException {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("johndoe");
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(mockUser));
        when(attendanceRepository.findByUserAndAttendanceDate(any(), any())).thenReturn(Optional.empty());
        when(firebaseStorageService.uploadImage(any())).thenReturn("https://firebase.com/photo.jpg");

        Attendance savedAttendance = Attendance.builder()
                .id(UUID.randomUUID())
                .user(mockUser)
                .username("johndoe")
                .attendanceDate(LocalDate.now())
                .attendanceTimeIn(LocalTime.now())
                .photoUrl("https://firebase.com/photo.jpg")
                .attendanceTimeInLatitude(attendanceRequest.getAttendanceTimeInLatitude())
                .attendanceTimeInLongitude(attendanceRequest.getAttendanceTimeInLongitude())
                .isValid(true)
                .build();
        when(attendanceRepository.save(any(Attendance.class))).thenReturn(savedAttendance);

        // Act
        AttendanceResponse response = attendanceService.submitAttendance(attendanceRequest, mockFile);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getPhotoUrl()).isEqualTo("https://firebase.com/photo.jpg");
        assertThat(response.getUsername()).isEqualTo("johndoe");
        verify(attendanceRepository, times(1)).save(any(Attendance.class));
    }

    @Test
    @DisplayName("submitAttendance() - should throw ApiException when user not found")
    void submitAttendance_withInvalidUser_shouldThrowApiException() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("invalid_user");
        when(userRepository.findByUsername("invalid_user")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> attendanceService.submitAttendance(attendanceRequest, mockFile))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    @DisplayName("submitAttendance() - should throw ApiException when already submitted today")
    void submitAttendance_withDoubleSubmission_shouldThrowApiException() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("johndoe");
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(mockUser));
        when(attendanceRepository.findByUserAndAttendanceDate(eq(mockUser), any()))
                .thenReturn(Optional.of(new Attendance()));

        // Act & Assert
        assertThatThrownBy(() -> attendanceService.submitAttendance(attendanceRequest, mockFile))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("Attendance already submitted for today");
    }

    @Test
    @DisplayName("submitAttendance() - should throw ApiException when file is missing")
    void submitAttendance_withMissingFile_shouldThrowApiException() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("johndoe");
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(mockUser));
        when(attendanceRepository.findByUserAndAttendanceDate(any(), any())).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> attendanceService.submitAttendance(attendanceRequest, null))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("Photo is mandatory");
    }
}
