package com.example.test_pt_sofco_graha_gaji.id.service;

import com.example.test_pt_sofco_graha_gaji.id.dto.UserRequest;
import com.example.test_pt_sofco_graha_gaji.id.dto.UserResponse;
import com.example.test_pt_sofco_graha_gaji.id.exception.ApiException;
import com.example.test_pt_sofco_graha_gaji.id.model.User;
import com.example.test_pt_sofco_graha_gaji.id.model.UserRole;
import com.example.test_pt_sofco_graha_gaji.id.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserServiceImpl Unit Tests")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRequest userRequest;
    private User mockUser;

    @BeforeEach
    void setUp() {
        userRequest = UserRequest.builder()
                .username("newuser")
                .email("new@example.com")
                .password("password123")
                .fullName("New User")
                .role(UserRole.USER)
                .build();

        mockUser = User.builder()
                .id(UUID.randomUUID())
                .username("newuser")
                .email("new@example.com")
                .fullName("New User")
                .role(UserRole.USER)
                .isActive(true)
                .build();
    }

    @Test
    @DisplayName("create() - should save user when request is valid")
    void create_withValidRequest_shouldReturnUserResponse() {
        // Arrange
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed_password");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // Act
        UserResponse response = userService.create(userRequest);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getUsername()).isEqualTo("newuser");
        assertThat(response.getEmail()).isEqualTo("new@example.com");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("create() - should throw ApiException when username exists")
    void create_withExistingUsername_shouldThrowApiException() {
        // Arrange
        when(userRepository.existsByUsername("newuser")).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> userService.create(userRequest))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("Username already exists");
    }

    @Test
    @DisplayName("get() - should return user when id exists")
    void get_withValidId_shouldReturnUserResponse() {
        // Arrange
        UUID userId = UUID.randomUUID();
        mockUser.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // Act
        UserResponse response = userService.get(userId);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("get() - should throw ApiException when user not found")
    void get_withInvalidId_shouldThrowException() {
        // Arrange
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userService.get(userId))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("User not found");
    }
}
