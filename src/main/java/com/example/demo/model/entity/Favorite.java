package com.example.demo.model.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(FavoriteId.class)
@Table(name = "favorites")
public class Favorite {
	@Id
    @Column(name = "user_id")
    private Integer userId;

    @Id
    @Column(name = "news_id")
    private Long newsId;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    // 新增關聯到 User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    //  新增關聯到 News
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id", insertable = false, updatable = false)
    private News news;
}