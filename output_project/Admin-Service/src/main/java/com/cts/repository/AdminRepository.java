package com.cts.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.entity.User;

public interface AdminRepository extends JpaRepository<User, Long> {
	Optional<User> findUserByName(String username);
	Optional<User> findByEmailIgnoreCase(String email);
	Optional<User> findUserByPassword(String password);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByPhoneNumber(String phoneNumber);

}
