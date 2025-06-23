package com.example.demo.model.entity;

import java.io.Serializable;

import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Embeddable
public class FavoriteId implements Serializable {
    private Integer userId;
    private Long newsId;
    
    //定義了兩個 FavoriteId 物件何時被認為是「相等」的
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FavoriteId)) return false;
        FavoriteId that = (FavoriteId) o;
        return Objects.equals(userId, that.userId) &&
               Objects.equals(newsId, that.newsId);
    }
    @Override
    public int hashCode() {
        return Objects.hash(userId, newsId);
    }
}