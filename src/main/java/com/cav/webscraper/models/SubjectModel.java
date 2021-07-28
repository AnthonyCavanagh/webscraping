package com.cav.webscraper.models;

import java.util.List;

public class SubjectModel {
	String subject;
	List <BookModel> books;
	
	public SubjectModel(String subject, List<BookModel> books) {
		super();
		this.subject = subject;
		this.books = books;
	}

	public String getSubject() {
		return subject;
	}

	public List<BookModel> getBooks() {
		return books;
	}
	
	
}
