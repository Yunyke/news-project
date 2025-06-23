package com.example.demo.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CNNNews {
	private Long id;
	private String title;
	private String author;
	private String urlToImage;
	private String description;
	private String url;
	private String content;
	private String publishedAt;
}