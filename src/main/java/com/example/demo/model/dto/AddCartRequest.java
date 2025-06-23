package com.example.demo.model.dto;

import lombok.Data;

@Data
public class AddCartRequest {
    private Integer userId;
    private Long newsId;
}