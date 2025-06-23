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

    // ✅ 推薦用「建構式注入」(也可改用 @Autowired)
    private final NewsService newsService;               // <-- added field

    public SearchController(NewsService newsService) {   // <-- added constructor
        this.newsService = newsService;
    }

    @GetMapping("/search")
    public String searchNews(@RequestParam("keyword") String keyword,
                             Model model) {              // <-- use correct Model

        // ✅ 基本防呆：Keyword 為空就直接回傳空結果
        if (keyword == null || keyword.trim().isEmpty()) { // <-- added
            model.addAttribute("keyword", "");
            model.addAttribute("results", List.of());      // Java 9+ List.of()
            return "search-results";
        }

        // ✅ 使用真正的 NewsService 進行搜尋
        List<News> searchResults = newsService.searchByKeyword(keyword); // <-- fixed

        model.addAttribute("keyword", keyword);
        model.addAttribute("results", searchResults);

        return "search-results";
    }
}