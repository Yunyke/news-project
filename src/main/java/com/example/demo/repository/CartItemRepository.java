package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.entity.CartItem;
import com.example.demo.model.entity.CartItemId;
import com.example.demo.model.entity.News;

public interface CartItemRepository extends JpaRepository<CartItem, CartItemId> {

    // 🆕 只要 cart.id = :cartId，就把對應的 News 撈出來
	 @Query("select ci.news from CartItem ci where ci.cart.id = :cartId")
	    List<News> findNewsByCartId(@Param("cartId") Long cartId);
	 @Query("select ci.news.id from CartItem ci where ci.cart.id = :cartId")
	    List<Long> findNewsIdsByCartId(@Param("cartId") Long cartId);
}