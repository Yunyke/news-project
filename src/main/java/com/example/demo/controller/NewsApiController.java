package com.example.demo.controller;

import com.example.demo.model.dto.NewsDto;
import com.example.demo.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = {"http://localhost:5341"})
@RestController
@RequestMapping("/dailynews/news")
@RequiredArgsConstructor
public class NewsApiController {

    private final NewsRepository newsRepository;

    @GetMapping
    public List<NewsDto> getNewsBySource(
            @RequestParam(defaultValue = "CNN") String source,
            @RequestParam(defaultValue = "30") int limit) {

        return newsRepository.findBySourceOrderByPublishedAtDesc(source)
                .stream()                                         
                .limit(limit)                                       // ⭐ 加上限制筆數
                .map(news -> NewsDto.builder()
                        .id(news.getId())
                        .title(news.getTitle())
                        .description(news.getDescription())
                        .url(news.getUrl())
                        .imageUrl(news.getImageUrl())
                        .source(news.getSource())
                        .author(news.getAuthor())
                        .content(news.getContent())
                        .publishedAt(news.getPublishedAt())
                        .build())
                .collect(java.util.stream.Collectors.toList());    
    }
}