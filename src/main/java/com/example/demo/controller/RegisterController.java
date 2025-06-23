package com.example.demo.controller;

import com.example.demo.model.dto.UserCert;
import com.example.demo.model.dto.UserDto;
import com.example.demo.service.EmailService;
import com.example.demo.service.RegisterService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private RegisterService registerService;
    
    @Autowired  // ✅ 注入剛剛變成 @Service 的 EmailService
    private EmailService emailService;

    // 顯示註冊表單頁面
    @GetMapping
    public String showRegisterForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "register"; 
    }

    @PostMapping
    public String registerUser(
        @ModelAttribute("userDto") UserDto userDto,
        Model model,
        HttpServletRequest request
    ) {
        try {
            // ✅ 呼叫註冊服務
            registerService.registerUser(userDto);

            // ✅ 傳送 Email 確認信
            String email = userDto.getEmail();  // 假設你的 UserDto 有 getEmail()
            String confirmUrl = "http://localhost:8008/user/confirm?email=" + email;
            emailService.sendEmail(email, confirmUrl);

            // ✅ 建立登入憑證
            UserCert userCert = new UserCert();
            userCert.setUsername(userDto.getUsername());
            userCert.setName(userDto.getName()); // ✅ 給歡迎畫面用

            // ✅ 儲存 Session
            HttpSession session = request.getSession();
            session.setAttribute("userCert", userCert);
            session.setAttribute("name", userDto.getName());
            session.setAttribute("locale", request.getLocale());

            // ✅ 導向首頁
            return "redirect:/news";

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}