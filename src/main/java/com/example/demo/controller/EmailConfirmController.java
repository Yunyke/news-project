package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.service.RegisterService;

import org.springframework.ui.Model; 

@Controller
@RequestMapping("/user")
public class EmailConfirmController {
	 @Autowired
	    private RegisterService registerService;

	    // ✅ 用來處理驗證連結
	    @GetMapping("/confirm")
	    public String confirmUser(@RequestParam("email") String email, Model model) {
	        try {
	            registerService.confirmUser(email);
	            model.addAttribute("email", email); // ✅ 傳 email 給 view
	            return "confirm"; // ✅ Thymeleaf 會找 templates/confirm-success.html
	        } catch (Exception e) {
	            model.addAttribute("error", e.getMessage());
	            return "confirm-fail";
	        }
	    }
}