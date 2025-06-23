package com.example.demo.model.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CnnApiResponse {
	
    private int totalResults;
    private List<CNNNews> articles;
    
}