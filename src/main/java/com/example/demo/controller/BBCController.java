package com.example.demo.controller;

import com.example.demo.model.entity.News;
import com.example.demo.repository.NewsRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; 
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class BBCController {

	@Autowired
	private NewsRepository newsRepository;

	// 當資料庫回傳的 List 為 null 時，回傳空 List , 限制 List 最多只取前 max 筆
	private <T> List<T> limit(List<T> list, int max) {
		return list == null ? List.of() : list.subList(0, Math.min(max, list.size()));
	}

	@GetMapping("/bbc") // Spring MVC 提供的 Model，用於傳遞資料到前端。
	public String bbcPage(Model model, HttpSession session) {

		model.addAttribute("title", "BBC News");

		List<News> newsList = limit(newsRepository.findBySourceOrderByPublishedAtDesc("BBC"), 50);
		model.addAttribute("newsList", newsList);

		// 讓前端能使用session登入資訊
		model.addAttribute("session", session);

		return "bbc";
	}
}