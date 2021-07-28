/**
 *  needed to create a a default package.json file
 *  npm init --yes
 *  npm install request --save
 *  npm install request-promise --save
	npm install puppeteer --save
	npm install nodemailer --save
	npm install cron --save
	uses puppeteer for dynamic websites
	
**/
const puppeteer = require('puppeteer');

const BASE_URL = 'https://www.britannica.com/topic/list-of-prime-ministers-of-Great-Britain-and-the-United-Kingdom-1800350';
(async () => {
	
	const browser = await puppeteer.launch({headless: false, executablePath: "../node_modules/puppeteer/.local-chromium/win32-884014/chrome-win/chrome.exe"});
	const page = await browser.newPage();
	page.on('console', consoleObj => console.log(consoleObj.text()))
	await page.goto(BASE_URL);
	const title = await page.title();
	
	const grabPrimeMinisters = await page.evaluate(() => {
		const primeministers = [];
		const containers = document.querySelector(".topic-list");
		const pms = containers.querySelectorAll("div");
	 		pms.forEach((element) => {
			primeministers.push(element.innerHTML);
			
		});
		return  primeministers;
	});
	
	const grabPrimeMinistersLinks = await page.evaluate(() => {
		const links = [];
		const containers = document.querySelector(".topic-list");
		const pms = containers.querySelectorAll('a[href]');
		console.log(pms.length);
	 		pms.forEach((element) => {
			links.push(element.getAttribute('href'));
			console.log(element.lastChild);
			
		});
		return  links;
	});
	
	
	console.log(title);
	console.log(grabPrimeMinistersLinks);
	console.log(grabPrimeMinisters);
	/*
	grabPrimeMinisters.forEach((element) => {
		console.log("Element "+element);
		});
	*/
	await browser.close();
})();