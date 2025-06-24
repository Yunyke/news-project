package com.example.demo;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class NewsSummarizerTranslator {

    /* ---------- 載入環境變數 ---------- */
    private static OpenAIClient client = OpenAIOkHttpClient.builder()
    	    .apiKey("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")
    	    .baseUrl("https://openrouter.ai/api/v1")
    	    .build();                  // ★ converted
    
    /**
     * 以中立口吻總結中文新聞
     * @param article 原文內容（任意語言）
     * @return 中文重點摘要
     */
    public static String summarize(String article, String system_prompt) {              // ★ converted
    	ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
    			.addSystemMessage(system_prompt)
    	        .addUserMessage(article)
    	        .model("deepseek/deepseek-r1:free")
    	        .build();
//    	System.out.println(client.responses().create(params));
    	ChatCompletion chatCompletion = client.chat().completions().create(params);
    	Optional<String> response = chatCompletion.choices().get(0).message().content();
    	if (response.isPresent()) {
            return response.get(); // result is "Hello"
        }else {
        	return "LLM does not response anything";
        }
    }

    /* ---------- Google Translate（非同步） ---------- */
    private static final Translate translator =                   // ★ converted
            TranslateOptions.newBuilder()
                    .build()
                    .getService();

    /**
     * 將簡體中文 ⟶ 繁體中文（非同步版）
     */
    public static CompletableFuture<String> translateToZhTwAsync(String message) { // ★ converted
        return CompletableFuture.supplyAsync(() -> {
            Translation translation = translator.translate(
                    message,
                    Translate.TranslateOption.sourceLanguage("zh-CN"),
                    Translate.TranslateOption.targetLanguage("zh-TW")
            );
            return translation.getTranslatedText();
        });
    }

    /* ---------- Quick demo ---------- */
    public static void main(String[] args) throws Exception {
        String article = "Amber heat health alerts have been issued across all of England, with temperatures set to rise above 30C.\r\n"
        		+ "\r\n"
        		+ "It means the whole population is potentially at risk from the effects of the heat - and the NHS may see increased demand.\r\n"
        		+ "\r\n"
        		+ "The alerts are in place from 12:00 BST on 19 June until 09:00 BST on Monday 23 June.\r\n"
        		+ "\r\n"
        		+ "How does the weather health alert system work?\r\n"
        		+ "The weather alert service warns the public in England when high or low temperatures could damage their health.\r\n"
        		+ "\r\n"
        		+ "The system is run by the UK Health Security Agency and the Met Office.\r\n"
        		+ "\r\n"
        		+ "It includes both heat health and cold health alerts\r\n"
        		+ "\r\n"
        		+ "Heat health alerts are issued between 1 June and 30 September, and cold health alerts are published between 1 November and 30 March.";

        // 測試摘要
//        String summary = summarize(article);
        String system_prompt = "你是一個專業而且中立的新聞專欄作家，你的任務是不帶任何立場的用繁體中文總結新聞裡面的重點";
        System.out.println("test paul");
        System.out.println("Summary:\n" + summarize(article, system_prompt));

        // 測試翻譯
//        String simplified = "中国大陆今天天气很好。";
//        translateToZhTwAsync(simplified)
//                .thenAccept(tw -> System.out.println("繁體翻譯: " + tw))
//                .join();
    }
}