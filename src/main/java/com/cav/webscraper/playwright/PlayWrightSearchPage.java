package com.cav.webscraper.playwright;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class PlayWrightSearchPage {
	
	
	public void loadChromeBrowser(String url) {
		Playwright playWright = Playwright.create();
	    LaunchOptions options = new BrowserType.LaunchOptions().withHeadless(false);
		BrowserType chrome = playWright.chromium();
		Browser browser = chrome.launch(options);
		Page page = browser.newPage();
		page.navigate(url);
		System.out.println(page.title());
		browser.close();
		
	}

}
