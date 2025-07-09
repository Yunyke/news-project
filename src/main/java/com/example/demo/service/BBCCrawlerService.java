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
			Document doc = Jsoup.connect(news.getLink()).userAgent("Mozilla/5.0").timeout(10000).get();

			StringBuilder sb = new StringBuilder();

			Elements paras = doc.select("article div[data-component=text-block] > p");

			if (paras.isEmpty()) {
				paras = doc.select("div[data-testid=live-text-block] p");
			}

			if (paras.isEmpty()) {
				paras = doc.select("article p");
			}

			for (Element p : paras) {
				sb.append(p.text()).append("\n\n");
			}

			news.setContent(sb.toString().trim());

		} catch (Exception e) {

			news.setContent("");
			e.printStackTrace();
		}

	}
}