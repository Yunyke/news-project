package com.example.demo.service;

import com.example.demo.model.dto.NHKNews;
import org.apache.commons.text.StringEscapeUtils;            
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;                            
import java.util.regex.Pattern;                           

@Service
public class NHKCrawlerService {

    private static final Pattern DETAIL_PROP_MORE =         
            Pattern.compile("more:\\s*'([^']+)'", Pattern.DOTALL);

    public void enrich(NHKNews news) {
        try {
            Document doc = Jsoup.connect(news.getLink())
                    .userAgent("Mozilla/5.0")
                    .timeout(10000)
                    .get();

            /* ---------- 圖片 ---------- */
            if (news.getImageUrl() == null || news.getImageUrl().isEmpty()) {
                Element ogImage = doc.selectFirst("meta[property=og:image]");
                if (ogImage != null) {
                    news.setImageUrl(ogImage.attr("content"));
                }
            }

            /* ---------- 內文 ---------- */
            StringBuilder sb = new StringBuilder();

            // 1. 摘要
            Element summary = doc.selectFirst("p.content--summary");
            if (summary != null) {
                sb.append(summary.text()).append("\n\n");
            }

            
            Elements bodyParagraphs = doc.select(
                    "div.content--detail-body div.body-text > p");

            // 如果上面抓不到，解析 __DetailProp__
            if (bodyParagraphs.isEmpty()) {
                Element detailScript = doc.selectFirst(
                        "script:containsData(__DetailProp__)");
                if (detailScript != null) {
                    Matcher m = DETAIL_PROP_MORE.matcher(detailScript.data());
                    if (m.find()) {
                        String escaped = m.group(1);              // HTML 字串被 JS 轉義過
                        String html = StringEscapeUtils.unescapeHtml4(escaped);
                        Document tmp = Jsoup.parseBodyFragment(html);
                        bodyParagraphs = tmp.select("p");
                    }
                }
            }

            // 3. 組字串
            for (Element p : bodyParagraphs) {
                sb.append(p.text()).append("\n\n");
            }
            news.setContent(sb.toString().trim());

        } catch (Exception e) {
            e.printStackTrace();
            news.setContent("");
        }
    }
}