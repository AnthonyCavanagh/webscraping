package com.cav.webcrawler;

import java.util.HashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebCrawler {

	
	private HashSet<String> links;
	
	
	
	public WebCrawler() {
		links = new HashSet<String>();
	}

	
	public HashSet<String>  getPageLinks(String rootUrl) {
		getLinks(rootUrl);
		return links;
	}


	public void getLinks(String url) {
		if(!links.contains(url)) {
			
			try {
				links.add(url);
				Document doc = Jsoup.connect(url).get();
				// Parse the HTML to extract links to other URLs
                Elements linksOnPage = doc.select("a[href]");

                // For each extracted URL... go back to Step 4.
                for (Element page : linksOnPage) {
                	getLinks(page.attr("abs:href"));
                }
				
			} catch (Exception e) {

			}
		}
	}
}
