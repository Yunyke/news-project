package com.example.demo.controller;

import com.example.demo.model.dto.CNNNews;
import com.example.demo.model.dto.UserCert;
import com.example.demo.repository.NewsRepository;
import com.example.demo.service.BBCRssService;
import com.example.demo.service.CnnCrawlerService;
import com.example.demo.service.NHKRssService;
import com.example.demo.service.NewsService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // 處理 HTTP 請求。當使用者打開網站 /，會由這支 class 來處理。
@Slf4j // Lombok 自動幫你加上 log 變數
public class NewsScraperController {

	private final NewsService newsService;
	private final NewsRepository newsRepository;

	// 建構子注入，把務注入進來給這個 Controller 使用。
	public NewsScraperController(NewsService newsService, NewsRepository newsRepository) {
		this.newsService = newsService;
		this.newsRepository = newsRepository;
	}

	private <T> List<T> limit(List<T> list, int max) {
		return list == null ? List.of() : list.subList(0, Math.min(max, list.size()));
	}

	// 1.加載各新聞來源的資料
	// 2.裝進 model 裡供前端（Thymeleaf）使用
	// 3.處理登入使用者的顯示邏輯
	@GetMapping({ "/", "/index" })
	public String showNews(Model model, HttpSession session) {

		model.addAttribute("title", "Daily News");

		

		// 依 source 撈出新聞
		model.addAttribute("cnnNewsList", limit(newsRepository.findBySourceOrderByPublishedAtDesc("CNN"), 30));
		model.addAttribute("bbcNewsList", limit(newsRepository.findBySourceOrderByPublishedAtDesc("BBC"), 30));
		model.addAttribute("nhkNewsList", limit(newsRepository.findBySourceOrderByPublishedAtDesc("NHK"), 30));

		return "index";
	}
}