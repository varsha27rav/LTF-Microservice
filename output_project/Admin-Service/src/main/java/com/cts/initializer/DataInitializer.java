package com.cts.initializer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cts.entity.User;
import com.cts.repository.AdminRepository;
import com.cts.constants.Role;
import com.cts.constants.Status;

@Configuration
public class DataInitializer {

	@Bean
	CommandLineRunner initAdmin(AdminRepository userRepository,
	                           PasswordEncoder passwordEncoder) {

	    return args -> {

	        if (userRepository.findByEmailIgnoreCase("admin@logitrack.com").isPresent()) {
	            System.out.println("Admin account already exists");
	            return;
	        }

	        User admin = new User();
	        admin.setName("Admin");
	        admin.setEmail("admin@logitrack.com");
	        admin.setPassword(passwordEncoder.encode("admin@123"));
	        admin.setPhoneNumber("9840141448");
	        admin.setRole(Role.ADMIN);
	        admin.setStatus(Status.ACTIVE);

	        userRepository.save(admin);

	        System.out.println("Default Admin Created:");
	        System.out.println("Email: admin@logitrack.com");
	        System.out.println("Password: admin@123");
	    };
	}
}