package com.example.demo.service;

import com.example.demo.model.dto.BBCNews;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class BBCCrawlerService {

    public void enrich(BBCNews news) {

        try {
            Document doc = Jsoup.connect(news.getLink())
                                .userAgent("Mozilla/5.0")
                                .timeout(10000)
                                .get();

            /* ---------- 內文 ---------- */
            StringBuilder sb = new StringBuilder();

            // ⭐️ 修改處 #1: 將 selector 由固定 ID 改成較寬鬆的 selector
            Elements paras = doc.select("article div[data-component=text-block] > p");

            // ⭐️ 修改處 #2: 若為直播型頁面，改抓 live-text 區塊
            if (paras.isEmpty()) {
                paras = doc.select("div[data-testid=live-text-block] p");
            }

            // ⭐️ 修改處 #3: 最後備援策略，抓所有 article 下的段落
            if (paras.isEmpty()) {
                paras = doc.select("article p");
            }

            for (Element p : paras) {
                sb.append(p.text()).append("\n\n");
            }

            news.setContent(sb.toString().trim());

        } catch (Exception e) {
            // TODO: 換 logger
            news.setContent("");
            e.printStackTrace();
        }

        
    }
}