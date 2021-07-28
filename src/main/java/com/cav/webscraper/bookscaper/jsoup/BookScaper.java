package com.cav.webscraper.bookscaper.jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.cav.webscraper.models.BookModel;
import com.cav.webscraper.models.SubjectModel;

public class BookScaper {
	
	private static final String HTTP_URL_SUBJECT = "http://books.toscrape.com/";
	private static final String HTTP_URL_BOOK = "http://books.toscrape.com/catalogue/";
	
	private static final String REMOVE_3 = "../../../";
	private static final String REMOVE_2 ="../../";

	public void scapeData(String searchUrl) {
		
		try {
			Document doc = Jsoup.connect(searchUrl).get();
			List<String> subjectLinks = scapeSubjectLinks(doc);
			List<SubjectModel> subjectData = scrapeSubjectWebPages(subjectLinks);
			scrapeBookWebPages(subjectData);
			printBookDetails(subjectData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/** 
	 * Scrape subject links of main page
	 * @param doc
	 * @return
	 */
	private List<String> scapeSubjectLinks(Document doc) {
		Elements navList = doc.getElementsByClass("nav nav-list");
		List <String> links = navList.stream().map(e -> scapeSubjectLink(e)).collect(ArrayList::new, List::addAll, List::addAll);
		
		return links;
	}
	
	private List<String> scapeSubjectLink(Element element) {
		Elements elements = element.getElementsByAttribute("href");
		List<String> links = elements.stream().map(e->scrapeLink(e)).collect(Collectors.toList());
		return links;
	}
	
	private String scrapeLink(Element element) {
		return HTTP_URL_SUBJECT+element.attr("href");
	}
	
	private List <SubjectModel> scrapeSubjectWebPages(List<String> subjectLinks) {
		return subjectLinks.stream().map(l ->scrapeSubjectWebPage(l)).collect(Collectors.toList());
	}
	
	/**
	 * Scrape book links of subject page
	 * @param link
	 * @return
	 */
    private SubjectModel scrapeSubjectWebPage(String link) {
    	try {
			Document doc = Jsoup.connect(link).get();
			List<BookModel> bookLinks = scrapeBookLinks(doc);
			return new SubjectModel(doc.title(), bookLinks);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
    
    private  List<BookModel>  scrapeBookLinks(Document doc) {
    	Elements productLinks = doc.getElementsByClass("product_pod");
    	return productLinks.stream().map(e->scrapeBookLinks(e)).collect(ArrayList::new, List::addAll, List::addAll);
    }
    
    private List<BookModel> scrapeBookLinks(Element element) {
    	Elements elements = element.getElementsByAttribute("href");
    	List<BookModel> links = elements.stream().map(e->scrapeBookLink(e)).collect(Collectors.toList());
		return links;
    }
    
    private BookModel scrapeBookLink(Element element) {
		return new BookModel(getBookLink(element.attr("href")));
    }
    
    private String getBookLink(String bookLink) {
    	if(bookLink.contains(REMOVE_3)) {
    		bookLink = bookLink.substring(REMOVE_3.length());
    	} else {
    		bookLink = bookLink.substring(REMOVE_2.length());
    	}
		return HTTP_URL_BOOK+bookLink;
	}
    
    /**
     * Scrape data of Book web Page
     */
    private void scrapeBookWebPages(List<SubjectModel> subjectData) {
    	subjectData.forEach(s->scrapeBookWebPage(s));
    }
    
    private void scrapeBookWebPage(SubjectModel subjectData) {
    	subjectData.getBooks().forEach(b->scrapeBookWebPage(b));
    }
    
    private void scrapeBookWebPage(BookModel book) {
    	String link = book.getLink();
    	try {
			Document doc = Jsoup.connect(link).get();
			scrapeBookDetails(doc, book);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void scrapeBookDetails(Document doc, BookModel book) {

    	Elements elements = doc.getElementsByClass("col-sm-6 product_main");
    	Elements tag = elements.tagName("h1");
    	String text = tag.attr("h1");
    	book.setTitle(tag.text());
    	elements = doc.getElementsByClass("price_color");
    	book.setPrice(elements.text());
    	System.out.println();
    }
    
    private void printBookDetails(List<SubjectModel> subjectData) {
    	System.out.println();
    }
}
