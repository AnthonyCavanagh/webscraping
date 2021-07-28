package com.cav.webscraper.bookscaper.hmtlunit;

import org.junit.jupiter.api.Test;

import com.cav.webscraper.bookscaper.hmtlunit.BookScaper;

public class BookScaperTest {

	@Test
	public void scapeData() {
		System.out.println("Run Test");
		
		BookScaper scaper = new BookScaper();
		scaper.scapeData("http://books.toscrape.com/?fbclid=IwAR0q3CYrd3gt92PU9ldLgXfcsagh2VtakvHQ2zzHABshjfIToXCxVHpWYGo");
	}
}
