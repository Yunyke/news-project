package com.example.demo.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.CartService;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepo;
    
    @Autowired
    private FavoriteRepository favoriteRepository;


    /**
     * 🆕 新增新聞進購物車
     */
    @Transactional
    @Override
    public void addNewsToCart(Integer userId, Long newsId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("找不到使用者 ID: " + userId));

        // 一人一車邏輯：抓第一個 cart，沒有就建
        Cart cart = user.getCarts().stream().findFirst().orElse(null);
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            user.getCarts().add(cart); // 雙向綁定
            cart = cartRepository.save(cart); // ⚠️ 記得 persist 一次以便拿到 cartId
        }

        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new RuntimeException("找不到新聞 ID: " + newsId));
        
        if (news.getAiSummary() == null || news.getAiSummary().isBlank()) {
            String systemPrompt = "你是一個專業而且中立的新聞專欄作家，你的任務是不帶任何立場的用繁體中文總結新聞裡面的重點";
            try {
                String summary = NewsSummarizerTranslator.summarize(news.getContent(), systemPrompt);
                news.setAiSummary(summary);
                newsRepository.save(news); // ⚠️ 要存起來
                System.out.println("🤖 AI 摘要完成：" + summary);
            } catch (Exception e) {
                System.out.println("⚠️ AI 摘要失敗：" + e.getMessage());
            }
        }
        
        
        
        // 檢查是否重複加入
        boolean exists = cart.getItems().stream()
                .anyMatch(item -> item.getNews().getId().equals(newsId));
        if (exists) {
            throw new RuntimeException("此新聞已加入購物車");
        }

        // 建立並加入 CartItem（雙向同步）
        CartItem item = new CartItem(cart, news);
        cart.getItems().add(item);

        cartRepository.save(cart); // 只存 cart 就能 cascade 到 item
        System.out.println("✅ 加入新聞 ID: " + newsId + " 到 Cart ID: " + cart.getId());
    }

    /**
     * 🆕 查詢使用者購物車中所有新聞 ID
     */
    @Transactional(readOnly = true)
    @Override
    public List<Long> findNewsIdsByUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("找不到使用者 ID: " + userId));

        Cart cart = user.getCarts().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("找不到購物車"));

        return cart.getItems().stream()
                .map(item -> item.getNews().getId())
                .collect(Collectors.toList());
    }

    /**
     * 🆕 從購物車移除某則新聞
     */
    @Transactional
    @Override
    public void removeNewsFromCart(Integer userId, Long newsId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("找不到使用者 ID: " + userId));

        Cart cart = user.getCarts().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("找不到購物車"));

        CartItem toRemove = cart.getItems().stream()
                .filter(item -> item.getNews().getId().equals(newsId))
                .findFirst()
                .orElse(null);

        if (toRemove != null) {
            cart.getItems().remove(toRemove);  // 移除 item
            toRemove.setCart(null);            // 雙向解除（安全清除）
            toRemove.setNews(null);
            cartRepository.save(cart);         // orphanRemoval 生效
            System.out.println("🗑️ 移除新聞 ID: " + newsId + " 從購物車 ID: " + cart.getId());
        } else {
            System.out.println("⚠️ 購物車中無此新聞，無需移除");
        }
    }

    
     // 根據 cartId 拿出購物車裡的所有 News 詳細資料
     
    @Transactional(readOnly = true)
    @Override
    public List<News> getNewsByCartId(Long cartId) {
        return cartItemRepo.findNewsByCartId(cartId);
    }
    @Override
    public List<News> getFavoritedNewsInCart(Integer userId) {
        //  找出購物車裡的所有新聞 ID
        List<Long> newsIdsInCart = cartRepository.findNewsIdsByUserId(userId);
        if (newsIdsInCart.isEmpty()) {
            return List.of(); // 沒有新聞，不用查
        }

        //  找出有哪些新聞 ID 是有被加入最愛的
        List<Long> favoritedNewsIds = favoriteRepository.findFavoriteNewsIds(userId, newsIdsInCart);

        //  把這些新聞資料撈出（假設你要用 News 資料）
        return newsRepository.findAllById(favoritedNewsIds);
    }
    @Override
    public List<News> getNewsByIds(List<Long> ids) {
        return newsRepository.findAllById(ids);
    }
}