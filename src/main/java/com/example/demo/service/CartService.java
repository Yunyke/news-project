package com.example.demo.service;

import java.util.List;

import com.example.demo.model.entity.News;

public interface CartService {
	   void addNewsToCart(Integer userId, Long newsId);
	   List<News> getFavoritedNewsInCart(Integer userId);
	   List<Long> findNewsIdsByUser(Integer userId);
	   List<News> getNewsByCartId(Long cartId);
	    void removeNewsFromCart(Integer userId, Long newsId);
	    
	  
}