package com.example.demo.controller;
import com.example.demo.config.NewsAiAssistant;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController // 用 RestController 直接回傳字串
@RequestMapping("/api/openai") 
public class NewsAiController {
	
	 @PostMapping("/ask")
	    public String askAi(@RequestBody Map<String, String> body) {
	        String userInput = body.get("message");
	        if (userInput == null || userInput.trim().isEmpty()) {
	            return "❌ 請輸入問題";
	        }

	        String systemPrompt = "你是一個專業而且中立的新聞翻譯，不帶任何立場的用繁體中文且條列式的列出新聞重點";
	        
	        return NewsAiAssistant.summarize(userInput, systemPrompt);
	    }

}
