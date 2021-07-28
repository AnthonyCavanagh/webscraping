/**
 * 
 */
const puppeteer = require('puppeteer');
const fs = require('fs');
const BASE_URL = 'https://www.amazon.co.uk/s?k=zombie+apocalypse&i=digital-text&crid=2PX1EMI67VC8O&sprefix=Zombie%2Caps%2C174&ref=nb_sb_ss_ts-doa-p_1_6';

(async () => {
	
	//Load the website
	const browser = await puppeteer.launch({headless: false, executablePath: "../node_modules/puppeteer/.local-chromium/win32-884014/chrome-win/chrome.exe"});
	const page = await browser.newPage();
	page.on('console', consoleObj => console.log(consoleObj.text()))
	await page.goto(BASE_URL);
	const title = await page.title();
	console.log(title);
	
	//
	
})();