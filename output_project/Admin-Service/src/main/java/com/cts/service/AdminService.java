package com.cts.service;

import java.util.List;

import com.cts.dto.ForgotPasswordRequest;
import com.cts.dto.ForgotUsernameRequest;
import com.cts.dto.LoginDTO;
import com.cts.dto.UserDTO;
import com.cts.dto.UserPromotionDTO;
import com.cts.entity.User;

public interface AdminService {

    User getUserById(Long userId);
    User getByUsername(String username);
    List<User> getAllUsers();
    User addUser(UserDTO user);
    void updateRole(Long userId, UserPromotionDTO dto);
    void deactivateUser(Long userId);
    void reactivateUser(Long userId);

    String validateUser(LoginDTO loginRequest);

    User findByEmail(String email);
    String forgotUsername(ForgotUsernameRequest request);
    String forgotPassword(ForgotPasswordRequest request);
}
