package com.example.demo.service;

import com.example.demo.model.entity.News;
import com.example.demo.repository.NewsRepository;
import com.example.demo.model.dto.BBCNews;

import com.example.demo.model.dto.NHKNews;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private CnnCrawlerService cnnCrawlerService;

    @Autowired
    private BBCRssService bbcRssService;

    @Autowired
    private NHKRssService nhkRssService;

    public void fetchAndSaveAllNews() {
        saveCnnNews();
        saveBbcNews();
        saveNhkNews();
    }

    private void saveCnnNews() {
    	List<News> cnnNewsList = cnnCrawlerService.fetchAndSaveIfNotExist();
        System.out.println("CNN News fetched: " + cnnNewsList.size());

        
    }

    private void saveBbcNews() {
        List<BBCNews> bbcNewsList = bbcRssService.getBbcNews();
        System.out.println("BBC News fetched: " + bbcNewsList.size());

        for (BBCNews item : bbcNewsList) {
            String url = item.getLink();
            if (!newsRepository.findByUrl(url).isPresent()) {
                News news = new News();
                news.setTitle(item.getTitle());
                news.setDescription(item.getDescription());
                news.setUrl(url);
                news.setImageUrl(item.getImageUrl());
                news.setSource("BBC");
                news.setPublishedAt(parseZonedTime(item.getPubDate()));
                news.setContent(item.getContent());
                newsRepository.save(news);
            }
        }
    }


    private void saveNhkNews() {
        List<NHKNews> nhkNewsList = nhkRssService.getNhkNews();
        System.out.println("NHK News fetched: " + nhkNewsList.size());

        for (NHKNews item : nhkNewsList) {
            String url = item.getLink();
            if (!newsRepository.findByUrl(url).isPresent()) {
                News news = new News();
                news.setTitle(item.getTitle());
                news.setDescription(item.getDescription());
                news.setUrl(url);
                news.setImageUrl(item.getImageUrl());
                news.setSource("NHK");
                news.setPublishedAt(parseZonedTime(item.getPubDate())); 
                news.setContent(item.getContent());
                newsRepository.save(news);
            }
        }
    }
    
    public News getNewsById(Long id) {
        return newsRepository.findById(id).orElse(null);
    }

    private ZonedDateTime parseZonedTime(String dateTimeStr) {
        try {
            return ZonedDateTime.parse(dateTimeStr);
        } catch (Exception e) {
            return ZonedDateTime.now(); // fallback 用現在時間
        }
    }
    
    public List<News> getNewsByIds(List<Long> ids) {          
        if (ids == null || ids.isEmpty()) {                   
            return List.of();                                 
        }                                                     
        return newsRepository.findByIdIn(ids);               
    }         
    
    public List<News> searchByKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of();
        }

        List<News> rawResults = newsRepository
            .findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword);

        // ✅ 為每個結果加上高亮（title only，可擴充）
        String highlightKeyword = "(?i)(" + Pattern.quote(keyword) + ")";
        Pattern pattern = Pattern.compile(highlightKeyword);

        for (News news : rawResults) {
            // 這裡假設你想高亮 title，內容可擴充
            String title = news.getTitle();
            if (title != null) {
                String highlighted = pattern.matcher(title)
                    .replaceAll("<mark>$1</mark>");  // ✅ <mark> 是 HTML 高亮標籤
                news.setTitle(highlighted);
            }
        }

        return rawResults;
    }
    
    public List<News> findAllById(List<Long> ids) {
        return newsRepository.findAllById(ids);
    }


    public List<News> findByIds(List<Long> ids) {
        return newsRepository.findByIdIn(ids);
    }
    @Scheduled(cron = "0 */30 * * * *")
    public void autoFetchNews() {
        System.out.println("🕒 自動開始抓新聞...");
        fetchAndSaveAllNews();
    }
}