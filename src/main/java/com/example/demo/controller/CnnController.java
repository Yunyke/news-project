package com.example.demo.controller;

import com.example.demo.model.entity.News;
import com.example.demo.repository.NewsRepository;
import com.example.demo.service.CnnCrawlerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController; // ⭐️ 修正: 直接用 RestController
import java.util.List;

@RestController                // ⭐️ 修正: 回傳 JSON 就用 RestController
public class CnnController {
//
//    private final CnnCrawlerService cnnCrawlerService;  // 不需要 @Autowired
//    private final NewsRepository newsRepository;
//
//    // ⭐️ 建議改成 Lombok @RequiredArgsConstructor 也行
//    public CnnController(NewsRepository newsRepository,
//                         CnnCrawlerService cnnCrawlerService) {
//        this.newsRepository = newsRepository;
//        this.cnnCrawlerService = cnnCrawlerService;
//    }
//
//    @GetMapping("/api/news/cnn")
//    public List<News> getCnnNewsAsJson() {
//
//    	 List<News> newsList =
//    	            newsRepository.findTop30BySourceOrderByPublishedAtDesc("CNN");
//
//    	        if (newsList.isEmpty()) {
//    	            cnnCrawlerService.fetchAndSaveIfNotExist();
//    	            newsList =
//    	                newsRepository.findTop30BySourceOrderByPublishedAtDesc("CNN");
//    	        }
//
//    	        return newsList;
//    	    }
    	}