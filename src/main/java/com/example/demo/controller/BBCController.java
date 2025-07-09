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

		model.addAttribute("title", "BBC News");

		List<News> newsList = limit(newsRepository.findBySourceOrderByPublishedAtDesc("BBC"), 50);
		model.addAttribute("newsList", newsList);

		// 讓前端能使用session登入資訊
		model.addAttribute("session", session);

		return "bbc";
	}
}