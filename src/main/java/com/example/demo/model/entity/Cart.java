package com.example.demo.model.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne; // ✅ 加上這個 import
import jakarta.persistence.OneToMany;
import jakarta.persistence.JoinColumn; // ✅ 加這個才能明確指定 FK
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Cart {
    @Id 
    @GeneratedValue
    private Long id;

    // ✅ 新增：關聯 user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // FK 名稱
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    // 工具方法：把商品丟進來
    public void addNews(News n) {
    	
        CartItem item = new CartItem(this, n); // CartItem 會用到 cart.getId()
        items.add(item);
    }
    
}