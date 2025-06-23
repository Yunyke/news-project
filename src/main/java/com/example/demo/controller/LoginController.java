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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private CertService certService;

    @GetMapping
    public String loginPage(@RequestParam(defaultValue = "/news") String redirect,
            Model model) {
model.addAttribute("redirect", redirect);
return "login";
}
    @GetMapping("/news")
    public String newsPage(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId != null) {
            model.addAttribute("userId", userId); // ✅ 給 Thymeleaf 用
        } else {
            return "redirect:/login?redirect=/news"; // 🛡️ 未登入跳回登入頁
        }
        return "news"; // 對應 news.html
    }

    @PostMapping
    public String checkLogin(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam(defaultValue = "/news") String redirect, 
            HttpServletRequest req,
            Model model) {

        try {
            // 🔥 清除前一個 session（如果有）
            HttpSession oldSession = req.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }

            // ✅ 建立新 session
            HttpSession session = req.getSession(true);

            // ✅ 登入驗證
            UserCert userCert = certService.getCert(username, password);

            // ✅ 寫入 session
            session.setAttribute("userCert", userCert);
            session.setAttribute("name", userCert.getName());
            session.setAttribute("locale", req.getLocale());
            session.setAttribute("userId", userCert.getUserId());

            return "redirect:" + redirect + "?resetCart=true";

        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "error";
        }
    }
    }