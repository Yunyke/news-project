package com.example.demo.model.entity;

import java.io.Serializable;

import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class FavoriteId implements Serializable {
    private Integer userId;
    private Long newsId;
    
    public FavoriteId(Integer userId, Long newsId) {
        this.userId = userId;
        this.newsId = newsId;
    }
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