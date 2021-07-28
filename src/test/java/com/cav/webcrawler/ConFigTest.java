package com.cav.webcrawler;

import java.net.URL;

import org.junit.Test;

public class ConFigTest {
	
	@Test
	public void testYML() {
		URL configFileURL = ConFigTest.class.getResource("/app.yml");
		String file = configFileURL.getFile();
		System.out.println(file);
	}

}
