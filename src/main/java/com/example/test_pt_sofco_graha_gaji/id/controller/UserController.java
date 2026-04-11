package com.example.test_pt_sofco_graha_gaji.id.controller;

import com.example.test_pt_sofco_graha_gaji.id.dto.UserRequest;
import com.example.test_pt_sofco_graha_gaji.id.dto.UserResponse;
import com.example.test_pt_sofco_graha_gaji.id.dto.WebResponse;
import com.example.test_pt_sofco_graha_gaji.id.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WebResponse<UserResponse> create(@Valid @RequestBody UserRequest request) {
        UserResponse userResponse = userService.create(request);
        return WebResponse.<UserResponse>builder()
                .status("success")
                .message("User created successfully")
                .data(userResponse)
                .build();
    }

    @GetMapping("/{id}")
    public WebResponse<UserResponse> get(@PathVariable UUID id) {
        UserResponse userResponse = userService.get(id);
        return WebResponse.<UserResponse>builder()
                .status("success")
                .message("User retrieved successfully")
                .data(userResponse)
                .build();
    }

    @GetMapping
    public WebResponse<List<UserResponse>> list() {
        List<UserResponse> userResponses = userService.list();
        return WebResponse.<List<UserResponse>>builder()
                .status("success")
                .message("Users listed successfully")
                .data(userResponses)
                .build();
    }

    @GetMapping("/profile")
    public WebResponse<UserResponse> getProfile() {
        UserResponse userResponse = userService.getProfile();
        return WebResponse.<UserResponse>builder()
                .status("success")
                .message("User profile retrieved successfully")
                .data(userResponse)
                .build();
    }

    @PutMapping("/update")
    public WebResponse<UserResponse> update(@Valid @RequestBody UserRequest request) {
        UserResponse userResponse = userService.update(request);
        return WebResponse.<UserResponse>builder()
                .status("success")
                .message("User profile updated successfully")
                .data(userResponse)
                .build();
    }
}
