package com.cts.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.cts.dto.ForgotPasswordRequest;
import com.cts.dto.ForgotUsernameRequest;
import com.cts.dto.LoginRequest;
import com.cts.dto.RegisterRequest;
import com.cts.dto.UserDTO;

import java.util.Map;

@FeignClient(name = "Admin-Service", url = "http://localhost:8078/api/admin")
public interface APIClient {

    @PostMapping("/addUsers")
    UserDTO registerUser(@RequestBody RegisterRequest dto);

    @PostMapping("/getUserDetails")
    Map<String, String> getUserDetails(@RequestBody LoginRequest dto);

    @PostMapping("/auth/forgot-username")
    String forgotUsername(@RequestBody ForgotUsernameRequest dto);

    @PostMapping("/auth/forgot-password")
    String forgotPassword(@RequestBody ForgotPasswordRequest dto);
}
