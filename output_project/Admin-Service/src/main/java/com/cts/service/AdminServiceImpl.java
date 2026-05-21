package com.cts.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cts.constants.Role;
import com.cts.constants.Status;
import com.cts.dto.ForgotPasswordRequest;
import com.cts.dto.ForgotUsernameRequest;
import com.cts.dto.LoginDTO;
import com.cts.dto.UserDTO;
import com.cts.dto.UserPromotionDTO;
import com.cts.entity.User;
import com.cts.repository.AdminRepository;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final AdminRepository adminRepository;

    public AdminServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    private static final List<String> PRIVILEGED_ROLES = List.of(
            "ADMIN", "DISPATCHER", "FLEET_MANAGER", "DRIVER", "AUDITOR"
    );

    private static final List<String> VALID_ROLES = List.of(
            "USER", "ADMIN", "CUSTOMER", "DISPATCHER", "FLEET_MANAGER", "DRIVER", "AUDITOR"
    );

    @Override
    public User getUserById(Long userId) {
        return adminRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

    @Override
    public User getByUsername(String username) {
        return adminRepository.findUserByName(username)
                .orElseThrow(() -> new RuntimeException("User not found with name: " + username));
    }

    @Override
    public List<User> getAllUsers() {
        return adminRepository.findAll();
    }

    @Override
    public User addUser(UserDTO dto) {

        if (adminRepository.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new RuntimeException("User already exists with this email");
        }

        if (adminRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
            throw new RuntimeException("Phone number already registered");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail().toLowerCase().trim());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setRole(Role.CUSTOMER);
        user.setStatus(Status.ACTIVE);

        return adminRepository.save(user);
    }

    @Override
    public void updateRole(Long userId, UserPromotionDTO dto) {

        User user = adminRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        if (user.getStatus() == Status.INACTIVE) {
            throw new RuntimeException("Cannot update role of an inactive user. Reactivate first.");
        }

        if (!VALID_ROLES.contains(dto.getNewRole())) {
            throw new RuntimeException(
                "Invalid role: " + dto.getNewRole() +
                ". Valid roles: " + String.join(", ", VALID_ROLES)
            );
        }

        user.setRole(Role.valueOf(dto.getNewRole()));
        user.setStatus(Status.ACTIVE);
        adminRepository.save(user);
    }

    @Override
    public void deactivateUser(Long userId) {
        User user = adminRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        user.setStatus(Status.INACTIVE);
        user.setRole(Role.CUSTOMER);
        adminRepository.save(user);
    }

    @Override
    public void reactivateUser(Long userId) {
        User user = adminRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        user.setStatus(Status.ACTIVE);
        adminRepository.save(user);
    }

    @Override
    public String validateUser(LoginDTO request) {

        User user = adminRepository
                .findByEmailIgnoreCase(request.getEmail().toLowerCase().trim())
                .orElseThrow(() -> new RuntimeException("No account found with this email"));

        if (user.getStatus() == Status.INACTIVE) {
            throw new RuntimeException("Account is deactivated. Contact your administrator.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return user.getRole().name();
    }

    @Override
    public User findByEmail(String email) {
        return adminRepository
                .findByEmailIgnoreCase(email.toLowerCase().trim())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    @Override
    public String forgotUsername(ForgotUsernameRequest request) {
        User user = adminRepository.findByEmailIgnoreCase(request.getEmail().toLowerCase().trim())
                .orElseThrow(() -> new RuntimeException("No account found with this email"));

        if (user.getStatus() == Status.INACTIVE) {
            throw new RuntimeException("Account is inactive. Contact your administrator.");
        }

        return "Your username is: " + user.getName();
    }

    @Override
    public String forgotPassword(ForgotPasswordRequest request) {
        User user = adminRepository.findByEmailIgnoreCase(request.getEmail().toLowerCase().trim())
                .orElseThrow(() -> new RuntimeException("No account found with this email"));

        if (user.getStatus() == Status.INACTIVE) {
            throw new RuntimeException("Account is inactive. Contact your administrator.");
        }

        if (request.getNewPassword() == null || request.getNewPassword().length() < 5) {
            throw new RuntimeException("New password must be at least 5 characters");
        }

        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new RuntimeException("New password cannot be the same as the current password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        adminRepository.save(user);

        return "Password updated successfully";
    }
}
