package com.example.test_pt_sofco_graha_gaji.id.service;

import com.example.test_pt_sofco_graha_gaji.id.dto.AuthRequest;
import com.example.test_pt_sofco_graha_gaji.id.dto.AuthResponse;
import com.example.test_pt_sofco_graha_gaji.id.model.User;
import com.example.test_pt_sofco_graha_gaji.id.model.UserRole;
import com.example.test_pt_sofco_graha_gaji.id.repository.UserRepository;
import com.example.test_pt_sofco_graha_gaji.id.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthServiceImpl Unit Tests")
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    private User mockUser;
    private AuthRequest authRequest;

    @BeforeEach
    void setUp() {
        mockUser = User.builder()
                .id(UUID.randomUUID())
                .username("johndoe")
                .email("john@example.com")
                .password("encoded_password")
                .fullName("John Doe")
                .role(UserRole.USER)
                .isActive(true)
                .build();

        authRequest = AuthRequest.builder()
                .username("johndoe")
                .password("secret123")
                .build();
    }

    @Test
    @DisplayName("login() - should return AuthResponse with token when credentials are valid")
    void login_withValidCredentials_shouldReturnAuthResponse() {
        // Arrange
        String expectedToken = "eyJhbGciOiJIUzI1NiJ9.mocktoken";
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(mockUser));
        when(jwtService.generateToken(mockUser)).thenReturn(expectedToken);

        // Act
        AuthResponse response = authService.login(authRequest);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo(expectedToken);
        assertThat(response.getUsername()).isEqualTo("johndoe");
        assertThat(response.getFullName()).isEqualTo("John Doe");

        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).findByUsername("johndoe");
        verify(jwtService, times(1)).generateToken(mockUser);
    }

    @Test
    @DisplayName("login() - should throw UsernameNotFoundException when user does not exist")
    void login_withNonExistentUser_shouldThrowUsernameNotFoundException() {
        // Arrange
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> authService.login(authRequest))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found");

        verify(authenticationManager, times(1)).authenticate(any());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    @DisplayName("login() - should propagate BadCredentialsException from AuthenticationManager")
    void login_withWrongPassword_shouldThrowBadCredentialsException() {
        // Arrange
        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        // Act & Assert
        assertThatThrownBy(() -> authService.login(authRequest))
                .isInstanceOf(BadCredentialsException.class);

        verify(userRepository, never()).findByUsername(any());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    @DisplayName("login() - should call authenticate with correct username and password")
    void login_shouldCallAuthenticateWithCorrectCredentials() {
        // Arrange
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(mockUser));
        when(jwtService.generateToken(mockUser)).thenReturn("token");

        // Act
        authService.login(authRequest);

        // Assert
        verify(authenticationManager).authenticate(
                argThat(auth ->
                        auth instanceof UsernamePasswordAuthenticationToken &&
                        auth.getName().equals("johndoe")
                )
        );
    }
}
