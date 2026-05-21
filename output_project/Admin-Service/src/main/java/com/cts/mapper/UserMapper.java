package com.cts.mapper;

import org.springframework.stereotype.Component;

import com.cts.dto.UserDTO;
import com.cts.entity.User;

@Component
public class UserMapper {

	public User toEntity(UserDTO dto) {
		User user = new User();
		user.setName(dto.getName());
		user.setEmail(dto.getEmail());
		user.setPassword(dto.getPassword());
		user.setPhoneNumber(dto.getPhoneNumber());
		return user;
	}
	
	public UserDTO toDto(User user) {
		UserDTO dto = new UserDTO();
		dto.setName(user.getName());
		dto.setEmail(user.getEmail());
		dto.setPassword(user.getPassword());
		dto.setPhoneNumber(user.getPhoneNumber());
		return dto;
	}
}
