package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.w3c.dom.*;

import com.example.demo.model.dto.BBCNews;

import javax.xml.parsers.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BBCRssService {
	 private final BBCCrawlerService crawler;

	    public BBCRssService(BBCCrawlerService crawler) {
	        this.crawler = crawler;
	    }
    public List<BBCNews> getBbcNews() {
        List<BBCNews> newsList = new ArrayList<>();

        try {
            String url = "https://feeds.bbci.co.uk/news/rss.xml";  //RSS feed 格式（XML 檔）
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  // 建立一個 DocumentBuilderFactory 實體
            DocumentBuilder builder = factory.newDocumentBuilder(); //從工廠產生出一個 DocumentBuilder。這個 builder 負責把 XML「讀進來 → 解析 → 變成 DOM 文件」。            
            Document doc = builder.parse(new URL(url).openStream()); //開啟 url 對應的網址並把資料讀進來，再交給 builder 解析

            NodeList items = doc.getElementsByTagName("item");   //擷取 <item> 區塊

            for (int i = 0; i < items.getLength(); i++) {
                Element item = (Element) items.item(i);

                String title = getTagValue("title", item);
                String description = getTagValue("description", item);
                String pubDate = getTagValue("pubDate", item);
                String link = getTagValue("link", item);

                String imageUrl = "";
                NodeList mediaList = item.getElementsByTagName("media:thumbnail");
                if (mediaList.getLength() > 0) {
                    Element media = (Element) mediaList.item(0);
                    imageUrl = media.getAttribute("url");
                }
                
                
                //建立 DTO 並放入清單
                BBCNews news = new BBCNews(); 
                news.setTitle(title);
                news.setDescription(description);
                news.setPubDate(pubDate);
                news.setImageUrl(imageUrl);
                news.setLink(link);
                crawler.enrich(news);
                newsList.add(news);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return newsList;
    }

    private String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList != null && nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            if (node != null) {
                return node.getTextContent();
            }
        }
        return "";
    }
}