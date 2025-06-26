package com.example.demo.controller;

import com.example.demo.model.entity.News;
import com.example.demo.repository.NewsRepository;
import com.example.demo.service.NewsService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // ✅ 正確的 Model
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class BBCController {

    private final NewsRepository newsRepository;
    private final NewsService newsService; 

    public BBCController(NewsRepository newsRepository, NewsService newsService) {
        this.newsRepository = newsRepository;
        this.newsService = newsService;
    }
    
    private <T> List<T> limit(List<T> list, int max) {
	    return list == null ? List.of() : list.subList(0, Math.min(max, list.size()));
	}
    @GetMapping("/bbc")
    public String bbcPage(Model model, HttpSession session) {
        // 設定頁面標題（前端可用 ${title} 顯示）
        model.addAttribute("title", "BBC News");

        
        List<News> newsList = limit(newsRepository.findBySourceOrderByPublishedAtDesc("BBC"), 50);
        model.addAttribute("newsList", newsList);

        // 讓前端能使用登入資訊（如果你有用 session.name）
        model.addAttribute("session", session);

        return "bbc";
    }
}