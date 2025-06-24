package com.example.demo.service.impl;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

import java.util.Optional;

public class NewsSummarizerTranslator {

    // ✅ OpenAI client 建立
    private static final OpenAIClient client = OpenAIOkHttpClient.builder()
            .apiKey("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") // 這串記得放環境變數！
            .baseUrl("https://openrouter.ai/api/v1")
            .build();

    /**
     * ✅ 呼叫 OpenRouter AI 做摘要
     * @param article 原始新聞內容
     * @param system_prompt 給 AI 的指令，例如「請用繁體中文總結重點」
     * @return 產生的摘要內容，如果失敗會回傳提示文字
     */
    public static String summarize(String article, String system_prompt) {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .addSystemMessage(system_prompt)
                .addUserMessage(article)
                .model("deepseek/deepseek-r1:free")  // ⛏️ 可換其他模型
                .build();

        ChatCompletion chatCompletion = client.chat().completions().create(params);

        Optional<String> response = chatCompletion.choices().get(0).message().content();

        return response.orElse("⚠️ LLM 沒有回傳任何內容");
    }
}
