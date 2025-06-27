package com.example.demo.config;

import java.util.Optional;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

public class NewsAiAssistant {
	private static OpenAIClient client = OpenAIOkHttpClient.builder()
    	    .apiKey("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")
    	    .baseUrl("https://openrouter.ai/api/v1")
    	    .build(); 
	
	public static String summarize(String article, String system_prompt) {             
	    try {
	    	ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
	    			.addSystemMessage(system_prompt)
	    	        .addUserMessage(article)
	    	        .model("meta-llama/llama-3.3-70b-instruct:free")
	    	        .build();

	    	ChatCompletion chatCompletion = client.chat().completions().create(params);
	    	Optional<String> response = chatCompletion.choices().get(0).message().content();
	    	if (response.isPresent())
	    		return response.get(); 
	    	else {
	        	return "LLM does not response anything";
	        }
		} catch (Exception e) {
			e.printStackTrace(); 
		    return "Error: " + e.getMessage();
	            
	        }
	    }

}
