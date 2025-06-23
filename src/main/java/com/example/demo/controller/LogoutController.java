package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.dto.UserCert;
import com.example.demo.service.CertService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class LogoutController {
	
	@Autowired
	private CertService certService;
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
	    session.invalidate(); // 清除所有 session
	    return "redirect:/news"; // 回登入頁
	}
}