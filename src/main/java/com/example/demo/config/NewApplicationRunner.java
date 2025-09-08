package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.example.demo.service.NewsService;

@Component
public class NewApplicationRunner implements ApplicationRunner {

	@Autowired
	private NewsService newsService;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		newsService.fetchAndSaveAllNews();
		System.out.println("抓取新聞功能啟動~");
	}

}
