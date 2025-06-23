package com.example.demo.service;

import com.example.demo.model.dto.UserDto;

public interface RegisterService {
	UserDto findByUsername(String username);

    UserDto registerUser(UserDto userDto);
    
    void confirmUser(String email);
}