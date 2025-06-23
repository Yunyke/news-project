package com.example.demo.model.dto;
import java.time.ZonedDateTime;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder; 
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewsDto {
		private Long id;
	    private String title;
	    private String description;
	    private String url;
	    private String imageUrl;
	    private String source;
	    private String author;
	    private String content;
	    private ZonedDateTime publishedAt;
}