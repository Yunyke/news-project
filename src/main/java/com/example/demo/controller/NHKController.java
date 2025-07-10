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
public class NHKController {

	@Autowired
	private NewsRepository newsRepository;

	private <T> List<T> limit(List<T> list, int max) {
		return list == null ? List.of() : list.subList(0, Math.min(max, list.size()));
	}

	@GetMapping("/nhk")
	public String nhkPage(Model model, HttpSession session) {

		model.addAttribute("title", "NHK News");

		List<News> newsList = limit(newsRepository.findBySourceOrderByPublishedAtDesc("NHK"), 50);
		model.addAttribute("newsList", newsList);

		model.addAttribute("session", session);

		return "nhk";
	}

}