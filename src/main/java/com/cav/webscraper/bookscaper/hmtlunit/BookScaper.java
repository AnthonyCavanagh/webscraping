package com.cav.webscraper.bookscaper.hmtlunit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.cav.webscraper.models.BookModel;
import com.cav.webscraper.models.SubjectModel;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class BookScaper {
	
	private static final String HTTP_URL_SUBJECT = "http://books.toscrape.com/";
	private static final String HTTP_URL_BOOK = "http://books.toscrape.com/catalogue/";
	
	private static final String REMOVE = "../../..";
	
	/**
	 * 
	 * @param searchUrl
	 */
	public void scapeData(String searchUrl) {
		WebClient client = new WebClient();
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(true);
		try {
			HtmlPage page = client.getPage(searchUrl);
			List <String>subjectLinks = scapeSubjectLinks(page);
			List<SubjectModel> subjects = scrapeBookDetailsPerSubject(subjectLinks, client);
			scrapeBookDataPerBookLinkStream(subjects, client);
			writeBookData(subjects);

		} catch (FailingHttpStatusCodeException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 *  Scrape all the links from main page
	 * @param page
	 * @return
	 */
	private List<String> scapeSubjectLinks(HtmlPage page){
		List<Object> items = page.getByXPath("//ul[@class='nav nav-list']");
		return items.stream().map(o -> getSubjectLinks(o)).collect(ArrayList::new, List::addAll, List::addAll);
	}

	/**
	 * scrape all the booklinks per subject
	 * @param subjects
	 * @param client
	 * @return
	 */
	private List <SubjectModel> scrapeBookDetailsPerSubject(List <String> subjects, WebClient client) {
		return subjects.stream().map(s->scrapeSubjectPage(s,client)).collect(Collectors.toList());
		
	}
	
	/**
	 * scrape all the book data per book page
	 * @param subjects
	 * @param client
	 */
	private void scrapeBookDataPerBookLinkStream(List <SubjectModel> subjects, WebClient client) {
		subjects.stream().forEach(s-> setBookDetailsfromSubject(s, client));
	}
	
	private List <BookModel>getBookLinks(HtmlPage page) {
		List <BookModel> bookDetails = new ArrayList<BookModel>();
		List<Object> items = page.getByXPath("//article[@class='product_pod']");
		for(Object obj : items) {
			HtmlElement item = (HtmlElement)obj;
			DomNodeList<HtmlElement> elements = item.getElementsByTagName("a");
			for(HtmlElement element : elements) {
				String link = getBookLink(element.getAttribute("href"));
				bookDetails.add(new BookModel(link));
			}
		}
		return bookDetails;
		
	}

	private void setBookDetails(BookModel bookDetail, WebClient client) {
		try {
			HtmlPage page = client.getPage(bookDetail.getLink());
			bookDetail.setTitle(scapeBookElement(page, "//div[@class='col-sm-6 product_main']","h1"));
			bookDetail.setPrice(scapeBookElement(page, "//p[@class='price_color']"));
		} catch (FailingHttpStatusCodeException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
   private String scapeBookElement(HtmlPage page, String anchor) {
	   List<Object> headers = page.getByXPath(anchor);
		for(Object obj : headers) {
			HtmlElement item = (HtmlElement)obj;
			return item.getVisibleText();
		}
		return "";
	}
   
   private String scapeBookElement(HtmlPage page, String anchor1, String anchor2) {
	   List<Object> headers = page.getByXPath(anchor1);
		for(Object obj : headers) {
			HtmlElement item = (HtmlElement)obj;
			DomNodeList<HtmlElement> elements = item.getElementsByTagName(anchor2);
			for(HtmlElement element : elements) {
				return element.getVisibleText();
			}
		}
		return "";
	}

   
   private void writeBookData(List<SubjectModel> subjects) {
	   System.out.println();
   }
   
   private String getBookLink(String bookLink) {
		bookLink = bookLink.substring(REMOVE.length());
		return HTTP_URL_BOOK+bookLink;
	}
   
   
   private List <String> getSubjectLinks(Object obj) {
	   List <String> subjects = new ArrayList <String>();
	   HtmlElement item = (HtmlElement)obj;
		//System.out.println(item.asXml());
		DomNodeList<HtmlElement> elements = item.getElementsByTagName("a");
		for(HtmlElement element : elements) {
			subjects.add(HTTP_URL_SUBJECT+element.getAttribute("href"));
		}
		return subjects;
   }
   
   private SubjectModel scrapeSubjectPage(String link, WebClient client)  {
	   String subjectHeader = null;
	   SubjectModel subject = null;
		List <BookModel> bookDetails = new ArrayList<BookModel>();
		try {
			HtmlPage page = client.getPage(link);
			List<Object> headers = page.getByXPath("//div[@class='page-header action']");
			for(Object obj : headers) {
				HtmlElement item = (HtmlElement)obj;
				subjectHeader = item.getVisibleText();
				bookDetails = getBookLinks(page);
			}
			subject = new SubjectModel(subjectHeader, bookDetails);
			
		} catch (FailingHttpStatusCodeException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return subject;
   }
   
   private void setBookDetailsfromSubject(SubjectModel subject, WebClient client) {
	   subject.getBooks().stream().forEach(b->setBookDetails(b, client));
   }
   
  
}
