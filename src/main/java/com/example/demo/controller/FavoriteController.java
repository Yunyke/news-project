package com.example.demo.controller;
//
//import java.util.Set;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.example.demo.model.dto.FavoriteToggleResponse;
//import com.example.demo.service.FavoriteService;
//
//import jakarta.servlet.http.HttpSession;
//import lombok.RequiredArgsConstructor;
//
//@RestController
//@RequestMapping("/dailynews/users/{userId}/favorites")
//				
//@RequiredArgsConstructor
public class FavoriteController {
//	
//	private final FavoriteService service;
//
//	@GetMapping
//	public ResponseEntity<Set<Long>> getFavorites(@PathVariable Integer userId) {
//		Set<Long> favoriteNewsIds = service.getFavoriteNewsIds(userId);
//		return ResponseEntity.ok(favoriteNewsIds);
//	}
//
//	@PostMapping("/{newsId}")
//	public ResponseEntity<FavoriteToggleResponse> toggle(@PathVariable Integer userId, @PathVariable Long newsId, HttpSession session) { // 🟨 新增參數
//		
//		
//	    System.out.println("⭐ 收藏請求: userId=" + userId + ", newsId=" + newsId); // ✅ 移到這裡
//
//	    Integer sessionUserId = (Integer) session.getAttribute("userId");
//	    if (sessionUserId == null || !sessionUserId.equals(userId)) {
//	        return ResponseEntity.status(401).build(); // 未授權
//	    }
//
//	    boolean nowFav = service.toggleFavorite(userId, newsId);
//	    return ResponseEntity.ok(new FavoriteToggleResponse(newsId, nowFav));
//	}
	}