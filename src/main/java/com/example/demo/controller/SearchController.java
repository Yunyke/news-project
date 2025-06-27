package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

// ✅ 正確的 Model
import org.springframework.ui.Model;                       // <-- fixed import

import com.example.demo.model.entity.News;
import com.example.demo.service.NewsService;             // <-- added import

@Controller
public class SearchController {

    
    private final NewsService newsService;            

    public SearchController(NewsService newsService) {   
        this.newsService = newsService;
    }

    @GetMapping("/search")
    public String searchNews(@RequestParam("keyword") String keyword,
                             Model model) {          

        // Keyword 為空就直接回傳空結果
        if (keyword == null || keyword.trim().isEmpty()) { 
            model.addAttribute("keyword", "");
            model.addAttribute("results", List.of());      
            return "search-results";
        }

        
        List<News> searchResults = newsService.searchByKeyword(keyword); 

        model.addAttribute("keyword", keyword);
        model.addAttribute("results", searchResults);

        return "search-results";
    }
}