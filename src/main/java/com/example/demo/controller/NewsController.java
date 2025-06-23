package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.NewsService;

@RestController
@RequestMapping("/api/news")
public class NewsController {

//	 @Autowired
//	    private NewsService newsService;
//
//	    @PostMapping("/fetch")
//	    public String fetchNews() {
//	        newsService.fetchAndSaveAllNews();
//	        return "新聞已抓取並儲存！";
//	    }
	}