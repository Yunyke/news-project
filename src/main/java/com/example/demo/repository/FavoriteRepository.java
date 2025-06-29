package com.example.demo.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.entity.Favorite;
import com.example.demo.model.entity.FavoriteId;

import jakarta.transaction.Transactional;

public interface FavoriteRepository extends JpaRepository<Favorite, FavoriteId>{

	boolean existsByUserIdAndNewsId(Integer userId, Long newsId);
	
	    @Modifying
	    @Transactional
	    @Query("delete from Favorite f where f.userId = :uid and f.newsId = :nid")
	    void deleteByUserIdAndNewsId(@Param("uid") Integer uid, @Param("nid") Long nid);
	    
	    @Query("SELECT f.newsId FROM Favorite f WHERE f.userId = :userId AND f.newsId IN :newsIds")
	    List<Long> findFavoriteNewsIds(@Param("userId") Integer userId, @Param("newsIds") List<Long> newsIds);

	    Optional<Favorite> findByUserIdAndNewsId(Integer userId, Long newsId);
	    List<Favorite> findByUserId(Integer userId);
	    List<Favorite> findByUserIdOrderByCreatedAtDesc(Integer userId);
	    
	}