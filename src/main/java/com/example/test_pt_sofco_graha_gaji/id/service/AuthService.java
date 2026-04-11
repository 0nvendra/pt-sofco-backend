package com.example.test_pt_sofco_graha_gaji.id.service;

import com.example.test_pt_sofco_graha_gaji.id.dto.AuthRequest;
import com.example.test_pt_sofco_graha_gaji.id.dto.AuthResponse;

public interface AuthService {
    AuthResponse login(AuthRequest request);
}
