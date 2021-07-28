package com.cav.webscraper.models;

public class BookModel {
	String title;
	String link;
	String price;
	
	
	public BookModel(String link) {
		super();
		this.link = link;
	}

	public String getTitle() {
		return title;
	}

	public String getLink() {
		return link;
	}

	public String getPrice() {
		return price;
	}
	
	

	public void setTitle(String title) {
		this.title = title;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "BookModel [title=" + title + ", link=" + link + ", price=" + price + "]";
	}

}
