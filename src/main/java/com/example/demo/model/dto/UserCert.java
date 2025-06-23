package com.example.demo.model.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCert {
	private Integer userId;
	private String name;
	private String username;
	private String password;
	private LocalDate birthdate;
	private String gender;
	private String email;
	private Boolean active;
	
}
 