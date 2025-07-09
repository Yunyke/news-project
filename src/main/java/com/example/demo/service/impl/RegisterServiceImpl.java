package com.example.demo.service.impl;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.dto.UserDto;
import com.example.demo.model.entity.Cart;
import com.example.demo.model.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.RegisterService;
import com.example.demo.util.HashUtil;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;

@Service
public class RegisterServiceImpl implements RegisterService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserMapper userMapper;

	@Override
	public UserDto registerUser(UserDto userDto) {

		// 檢查是否已存在相同的 username
		User existingUser = userRepository.findByUsername(userDto.getUsername());
		if (existingUser != null) {
			throw new RuntimeException("Username already exists.");
		}
		try {
			String salt = HashUtil.generateSalt();
			String hashedPassword = HashUtil.hashPassword(userDto.getPassword(), salt);
			userDto.setPassword(hashedPassword);
			userDto.setSalt(salt);
			userDto.setActive(false);

			// 將 DTO 轉換成 Entity
			User user = userMapper.toEntity(userDto);

			Cart cart = new Cart();
			cart.setUser(user);
			user.getCarts().add(cart); // 雙向關聯同步
			// 儲存
			User savedUser = userRepository.save(user);

			return userMapper.toDto(savedUser);
		} catch (Exception e) {
			throw new RuntimeException("註冊失敗：" + e.getMessage());
		}
	}

	@Override
	@Transactional
	public void confirmUser(String email) {
		int updated = userRepository.activateUser(email.trim());
		if (updated == 0) {
			throw new RuntimeException("帳號不存在或已啟用：" + email);
		}
	}

	@Override
	public UserDto findByUsername(String username) {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new RuntimeException("User not found");
		}
		return userMapper.toDto(user);
	}
}