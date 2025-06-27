// src/main/java/com/example/demo/controller/CartPageController.java
package com.example.demo.controller;

import java.util.List;
import java.util.Set;



import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.entity.News;
import com.example.demo.service.NewsService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CartPageController {

    private final NewsService newsService;
  //  private final FavoriteService favoriteService;     

    public CartPageController(NewsService newsService
                             ) {  
        this.newsService = newsService;
       
    }

   
    @GetMapping("/cart")
    public String cartPage(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        model.addAttribute("userId", userId);
        return "cart"; 
        }

    
    @PostMapping("/cart/load")
    public String loadCart(@RequestBody List<Long> newsIds,
                           HttpSession session,
                           Model model) {

        List<News> cartNews = newsService.findAllById(newsIds);
        model.addAttribute("cartNews", cartNews);
        
        
        return "cart :: cartFragment";
        
    }
    
}