package com.cav.webscraper;

public class Data {
	String title;
	String[] bookLinks;

	public Data(String title, String[] bookLinks) {
		super();
		this.title = title;
		this.bookLinks = bookLinks;
	}

	public String getTitle() {
		return title;
	}

	public String[] getBookLinks() {
		return bookLinks;
	}
}
