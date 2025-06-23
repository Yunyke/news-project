package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.News;
import java.util.List;
import java.time.ZonedDateTime;



@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    Optional<News> findByUrl(String url); 
    List<News> findByIdIn(List<Long> ids);
    
    boolean existsByUrl(String url);
    List<News> findBySource(String source);
    List<News> findBySourceOrderByPublishedAtDesc(String source);
    List<News> findByPublishedAtBetween(ZonedDateTime start, ZonedDateTime end);
    List<News> findByTitleContaining(String keyword);
    List<News> findTop30BySourceOrderByPublishedAtDesc(String source);
    List<News> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content);

}