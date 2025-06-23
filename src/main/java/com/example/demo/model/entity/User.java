package com.example.demo.model.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //自動產生主鍵
	@Column(name = "user_id")
	private Integer id;
	private String name;
	private String username;
	private String password;	
	private String salt; 
	private LocalDate birthdate;
	private String gender;
	private String email;
	@Column(nullable = false, columnDefinition = "tinyint(1) default 0")
	private Boolean active;
		
	@OneToMany( mappedBy = "user",
		    cascade = CascadeType.ALL,      
		    orphanRemoval = true,          
		    fetch = FetchType.LAZY)
	
	private List<Cart> carts = new ArrayList<>();
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Favorite> favorites = new HashSet<>();

}