package com.example.demo.model.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {

    
    @EmbeddedId
    private CartItemId id;
    
    
    @MapsId("cartId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Cart cart;

    @MapsId("newsId")
    @ManyToOne(fetch = FetchType.EAGER)
    private News news;

    
    public CartItem(Cart cart, News news) {
        this.cart = cart;
        this.news = news;
        this.id = new CartItemId(cart.getId(), news.getId());
    }
    
}