package com.example.test_pt_sofco_graha_gaji.id.controller;

import com.example.test_pt_sofco_graha_gaji.id.dto.AuthRequest;
import com.example.test_pt_sofco_graha_gaji.id.dto.AuthResponse;
import com.example.test_pt_sofco_graha_gaji.id.dto.WebResponse;
import com.example.test_pt_sofco_graha_gaji.id.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public WebResponse<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request);
        return WebResponse.<AuthResponse>builder()
                .status("success")
                .message("Login successful")
                .data(response)
                .build();
    }
}
