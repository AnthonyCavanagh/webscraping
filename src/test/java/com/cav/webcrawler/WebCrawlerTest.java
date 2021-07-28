package com.cav.webcrawler;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

public class WebCrawlerTest {
	
	@Test
	public void getPageLinksTest() {
		WebCrawler crawler = new WebCrawler();
		HashSet<String> links = crawler.getPageLinks("https://books.toscrape.com/");
		 links.forEach(l -> System.out.println(l));
	}

}
