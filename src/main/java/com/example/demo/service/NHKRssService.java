package com.example.demo.service;

import com.example.demo.model.dto.NHKNews;
import org.springframework.stereotype.Service;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.service.NHKCrawlerService;
@Service
public class NHKRssService {
	  private final NHKCrawlerService crawler;

	    public NHKRssService(NHKCrawlerService crawler) {
	        this.crawler = crawler;
	    }

    public List<NHKNews> getNhkNews() {
        List<NHKNews> newsList = new ArrayList<>();

        try {
            String url = "https://www.nhk.or.jp/rss/news/cat6.xml";
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new URL(url).openStream());

            NodeList items = doc.getElementsByTagName("item");

            // 限制最多 50 筆
            for (int i = 0; i < items.getLength() && i < 50; i++) {
                Element item = (Element) items.item(i);

                String title = getTagValue("title", item);
                String description = getTagValue("description", item);
                String pubDate = getTagValue("pubDate", item);
                String link = getTagValue("link", item);

                String imageUrl = "";

                // 1️⃣ 優先抓 <media:thumbnail>
                NodeList mediaList = item.getElementsByTagName("media:thumbnail");
                if (mediaList.getLength() > 0) {
                    Element media = (Element) mediaList.item(0);
                    imageUrl = media.getAttribute("url");
                }

                // 2️⃣ fallback：抓 <enclosure type=\"image/jpeg\">
                if (imageUrl.isEmpty()) {
                    NodeList enclosureList = item.getElementsByTagName("enclosure");
                    if (enclosureList.getLength() > 0) {
                        Element enclosure = (Element) enclosureList.item(0);
                        if ("image/jpeg".equals(enclosure.getAttribute("type"))) {
                            imageUrl = enclosure.getAttribute("url");
                        }
                    }
                }

                // ✅ 建立 DTO
                NHKNews news = new NHKNews();
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