package com.example.demo.controller;

import com.example.demo.model.dto.FavoriteToggleResponse;
import com.example.demo.model.entity.Favorite;
import com.example.demo.model.entity.News;
import com.example.demo.model.entity.User;
import com.example.demo.service.FavoriteService;
import com.example.demo.repository.NewsRepository;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final UserRepository userRepository;
    private final NewsRepository newsRepository;


@PostMapping("/{newsId}")
public ResponseEntity<FavoriteToggleResponse> toggleFavorite(@PathVariable Long newsId,
                                                              @RequestParam("userId") Integer userId) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
    News news = newsRepository.findById(newsId)
            .orElseThrow(() -> new RuntimeException("News not found"));

    boolean alreadyFavorited = favoriteService.isNewsFavoritedByUser(userId, newsId);

    if (alreadyFavorited) {
        favoriteService.removeFavorite(userId, newsId);
    } else {
        favoriteService.addFavorite(user, news);
    }

    return ResponseEntity.ok(new FavoriteToggleResponse(newsId, !alreadyFavorited));
}
    @DeleteMapping("/{newsId}")
    public ResponseEntity<String> removeFavorite(@PathVariable Long newsId, @RequestParam("userId")Integer userId ) {
        favoriteService.removeFavorite(userId, newsId);
        return ResponseEntity.ok("News unfavorited successfully.");
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getFavorites( @RequestParam("userId")Integer userId) {
        List<Favorite> favorites = favoriteService.getFavoritesByUser(userId);

        // 把收藏對象轉前端
        List<Map<String, Object>> result = favorites.stream().map(fav -> {
            Map<String, Object> map = new HashMap<>();
            map.put("newsId", fav.getNews().getId());
            map.put("title", fav.getNews().getTitle());
            map.put("favoritedAt", fav.getCreatedAt());
            map.put("url", fav.getNews().getUrl());
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/status")
    public ResponseEntity<List<Long>> getFavoritedNewsIds(@RequestParam List<Long> newsIds, @RequestParam("userId")Integer userId) {
        List<Long> favorited = favoriteService
                .getFavoriteNewsIdsByUser(userId, newsIds);
        return ResponseEntity.ok(favorited);
        
        
    }
}
