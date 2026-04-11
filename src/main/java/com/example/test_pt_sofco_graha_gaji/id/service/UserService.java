package com.example.test_pt_sofco_graha_gaji.id.service;

import com.example.test_pt_sofco_graha_gaji.id.dto.UserRequest;
import com.example.test_pt_sofco_graha_gaji.id.dto.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponse create(UserRequest request);
    UserResponse get(UUID id);
    List<UserResponse> list();
    UserResponse getProfile();
    UserResponse update(UserRequest request);
}
