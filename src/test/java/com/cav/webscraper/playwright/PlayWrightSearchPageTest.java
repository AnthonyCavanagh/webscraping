package com.cav.webscraper.playwright;

import org.junit.jupiter.api.Test;

public class PlayWrightSearchPageTest {
	
	@Test
	public void launchChrome() {
		PlayWrightSearchPage playwright = new PlayWrightSearchPage();
		playwright.loadChromeBrowser("https://www.google.com");
	}

}
