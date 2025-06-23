package com.example.demo.model.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
	private Integer userId;
	private String name;
	private String username;
	private String password;
	private String salt; 
	private LocalDate birthdate;
	private String gender;
	private String email;
	private Boolean active;
}