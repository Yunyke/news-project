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
public class CnnController {

	private final NewsRepository newsRepository;
	private final NewsService newsService;

	public CnnController(NewsRepository newsRepository, NewsService newsService) {
		this.newsRepository = newsRepository;
		this.newsService = newsService;
	}

	private <T> List<T> limit(List<T> list, int max) {
		return list == null ? List.of() : list.subList(0, Math.min(max, list.size()));
	}

	@GetMapping("/cnn")
	public String cnnPage(Model model, HttpSession session) {
		// 頁面標題
		model.addAttribute("title", "CNN News");

		// 將 CNN 資料存進 Model
		List<News> newsList = limit(newsRepository.findBySourceOrderByPublishedAtDesc("CNN"), 50);
		model.addAttribute("newsList", newsList);

		// 讓前端能使用登入資訊
		model.addAttribute("session", session);

		return "cnn";
	}
}