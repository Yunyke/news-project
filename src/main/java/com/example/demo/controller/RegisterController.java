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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private RegisterService registerService;
    
    @Autowired  
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
        HttpServletRequest request,
        RedirectAttributes redirectAttributes
    ) {
        try {
            //  呼叫註冊服務
            registerService.registerUser(userDto);

            //  傳送 Email 確認信
            String email = userDto.getEmail();  
            String confirmUrl = "http://localhost:8008/user/confirm?email=" + email;
            emailService.sendEmail(email, confirmUrl);

            //  建立登入憑證
            UserCert userCert = new UserCert();
            userCert.setUsername(userDto.getUsername());
            userCert.setName(userDto.getName()); 

            //  儲存 Session
            HttpSession session = request.getSession();
            session.setAttribute("userCert", userCert);
            session.setAttribute("name", userDto.getName());
            session.setAttribute("locale", request.getLocale());

            //  導向首頁
           // redirectAttributes.addFlashAttribute("message", "我們已寄出驗證信至 " + email + "，請登入信箱驗證！");
            
            return "confirm-not-finish";

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}