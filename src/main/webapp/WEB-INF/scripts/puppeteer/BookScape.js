/**
 * 
 */
const puppeteer = require('puppeteer');
const fs = require('fs');
const BASE_URL = 'http://books.toscrape.com/?fbclid=IwAR0q3CYrd3gt92PU9ldLgXfcsagh2VtakvHQ2zzHABshjfIToXCxVHpWYGo';

(async () => {
	
	
	const browser = await puppeteer.launch({headless: false, executablePath: "../node_modules/puppeteer/.local-chromium/win32-884014/chrome-win/chrome.exe"});
	const page = await browser.newPage();
	page.on('console', consoleObj => console.log(consoleObj.text()))
	await page.goto(BASE_URL);
	const title = await page.title();
	console.log(title);
	

	/**
		Method grabBookSubjectsLinks
		
		Get all the url links for the different book subjects
	 **/
	const subjectLinks = grabBookSubjectsLinks = await page.evaluate(() => {
		const links = [];
		// const containers = document.querySelector(".nav nav-list");
		const containers = document.querySelector('ul[class="nav nav-list"]');
		const subjects = containers.querySelectorAll('a[href]');
		subjects.forEach((element)=> {
			link = element.getAttribute('href');
			if(link.includes('http')){
				links.push(link);
			} else {
				links.push('http://books.toscrape.com/'+link);
			}
		});
		return links;
	})
	
	
	// For each subject get list of book URLs.
	const webData = [];
	for(subjectLink of subjectLinks) {
		console.log("Go to "+subjectLink);
		await page.goto(subjectLink);
		
		
		/**
		   Method grabBooklinks 
		
		   Get all the url links for the different book subjects
	    **/
		let bookdatas = grabBooklinks  = await page.evaluate(() => {
			let bookDatalinks = [];
			
		    let header = document.querySelector('div[class="page-header action"] > h1');
            //const books = containers.querySelectorAll('a[href]');
           // const containers = document.querySelector('o1[class="row"]');
			const books = document.querySelectorAll('article[class="product_pod"]');
			console.log(header.innerHTML+" containes this number of books "+books.length);
			
			
			const data = {
				subject:undefined,
	            bookLinks:[]
		   }
		   books.forEach((element)=> {
		   let bookdata = element.querySelector('a[href]');
				link = bookdata.getAttribute('href');
				if(link.includes('http')){
					links.push(link);
				} else {
					let subLink = link.substring("../../..".length, link.length);
					let booklink = 'http://books.toscrape.com/catalogue'+subLink
					//console.log("push on to bookDatalinks "+link);
					bookDatalinks.push(booklink);
				}
			})
			data.subject = header.innerHTML;
			data.bookLinks = bookDatalinks;
			return data;
		});
		webData.push(grabBooklinks);
	}
	
	
	for(data of webData){
		let bookLinks = data.bookLinks;
		bookDatas = [];
		for(bookLink of bookLinks) {
			const resp = await page.goto(bookLink);
			if(resp.status() === 200){
				console.log(bookLink);
				
				/**
					method grabBookData
				 */
				let bookData = grabBookData  = await page.evaluate(() => {
					 let title = document.querySelector('div[class="col-sm-6 product_main"] > h1');
					 let price = document.querySelector('p[class="price_color"]');
				     let starRating = document.querySelector('div[class="star-rating Two"]');

					 let bookData = {
						title:undefined,
						price:undefined,
						starRating:undefined
					}
					console.log("title "+title.innerHTML);
					bookData.title = title.innerHTML;
					bookData.price = price.innerHTML;
					bookData.starRating = starRating;
					
					return bookData;
				});
				bookDatas.push(bookData.title+","+bookData.price+","+bookData.starRating);
			}// End if
			
		}// End loop booklink
		
		// write to file
		const writeStream = fs.createWriteStream(data.subject+'.txt');
		const pathName = writeStream.path;
		bookDatas.forEach(value => writeStream.write(`${value}\n`));
		writeStream.on('finish', () => {
   			console.log(`wrote all the array data to file ${pathName}`);
		});
		writeStream.on('error', (err) => {
          console.error(`There is an error writing the file ${pathName} => ${err}`)
        });
       // close the stream
       writeStream.end();
	}
	await browser.close(webData);
})();