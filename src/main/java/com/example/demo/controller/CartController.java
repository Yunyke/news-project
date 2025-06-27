package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import com.example.demo.model.dto.FavoriteToggleResponse;
import com.example.demo.model.entity.Favorite;
import com.example.demo.model.entity.News;
import com.example.demo.repository.FavoriteRepository;
import com.example.demo.service.CartService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private FavoriteRepository favoriteRepository;

    @PostMapping("/users/{userId}/cart")
    public ResponseEntity<String> addToCart(@PathVariable Integer userId,
                                            @RequestBody Map<String, Long> payload) {
        Long newsId = payload.get("newsId");
        cartService.addNewsToCart(userId, newsId);
        return ResponseEntity.ok("已加入購物車！");
    }

    @GetMapping("/users/{userId}/cart")
    public ResponseEntity<List<Long>> getUserCart(@PathVariable Integer userId) {
        return ResponseEntity.ok(cartService.findNewsIdsByUser(userId));
    }

    @GetMapping("/{cartId}/news")
    public ResponseEntity<List<News>> getNewsByCartId(@PathVariable Long cartId) {
        List<News> newsList = cartService.getNewsByCartId(cartId);
        return ResponseEntity.ok(newsList);
    }

    @DeleteMapping("/users/{userId}/cart/{newsId}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Integer userId,
                                               @PathVariable Long newsId) {
        cartService.removeNewsFromCart(userId, newsId);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/dailynews/users/{userId}/favorites/{newsId}")
    public ResponseEntity<FavoriteToggleResponse> toggleFavorite(
        @PathVariable Integer userId,
        @PathVariable Long newsId) {

        boolean exists = favoriteRepository.existsByUserIdAndNewsId(userId, newsId);
        
        if (exists) {
            favoriteRepository.deleteByUserIdAndNewsId(userId, newsId);
            return ResponseEntity.ok(new FavoriteToggleResponse(newsId, false));
        } else {
            Favorite f = new Favorite();
            f.setUserId(userId);
            f.setNewsId(newsId);
            favoriteRepository.save(f);
            return ResponseEntity.ok(new FavoriteToggleResponse(newsId, true));
        }
    }
    
    

    // 一次加入多筆新聞到購物車
    @PostMapping("/users/{userId}/bulk")
    public ResponseEntity<String> addMultipleToCart(@PathVariable Integer userId,
                                                    @RequestBody Map<String, List<Long>> payload) {
        List<Long> newsIds = payload.get("newsIds");

        if (newsIds == null || newsIds.isEmpty()) {
            return ResponseEntity.badRequest().body("❌ 沒有提供任何新聞 ID");
        }

        int successCount = 0;
        for (Long newsId : newsIds) {
            try {
                cartService.addNewsToCart(userId, newsId);
                successCount++;
            } catch (RuntimeException ex) {
                System.out.println("⚠️ 略過重複或無效 ID：" + newsId + "（" + ex.getMessage() + "）");
            }
        }

        return ResponseEntity.ok("✅ 成功加入 " + successCount + " 則新聞到購物車");
    }
}