package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.entity.*;
import com.example.demo.repository.FavoriteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    public void addFavorite(User user, News news) {
        FavoriteId id = new FavoriteId(user.getId(), news.getId());
        boolean exists = favoriteRepository.existsById(id);
        if (!exists) {
            Favorite favorite = new Favorite();
            favorite.setUserId(user.getId());
            favorite.setNewsId(news.getId());
            favorite.setUser(user);
            favorite.setNews(news);
            favoriteRepository.save(favorite);
        }
    }

    public void removeFavorite(Integer userId, Long newsId) {
        favoriteRepository.deleteByUserIdAndNewsId(userId, newsId);
    }

    public List<Favorite> getFavoritesByUser(Integer userId) {
        return favoriteRepository.findByUserId(userId);
    }

    public boolean isNewsFavoritedByUser(Integer userId, Long newsId) {
        return favoriteRepository.findByUserIdAndNewsId(userId, newsId).isPresent();
    }
    public List<Long> getFavoriteNewsIdsByUser(Integer userId, List<Long> newsIds) {
        return favoriteRepository.findFavoriteNewsIds(userId, newsIds);
    }
}
