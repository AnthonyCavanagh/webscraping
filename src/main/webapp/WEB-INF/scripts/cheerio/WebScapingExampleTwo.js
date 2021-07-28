/**
 *  needed to create a a default package.json file
 *  npm init --yes
 *  npm install request --save
 *  npm install request-promise --save
	npm install cheerio --save
	npm install cli-table --save
	https://forum.freecodecamp.org/users
 */
const rp = require('request-promise');
const cheerio = require('cheerio');
const Table = require('cli-table');

let table = new Table({
	head: ['username', '❤️', 'challenges'],
	colWidths: [15, 5, 10]
})

const options = {
	// url address and javascript in this case directory_items
	url: `https://forum.freecodecamp.org/directory_items?period=weekly&order=likes_received&_=1518604435748`,
	json: true
}

rp(options)
	.then((data) => {
		let userData = [];
		for(let user of data.directory_items){
			userData.push({name: user.user.username, likes_received: user.likes_received})
		}
		// all data loaded into userData;
		//console.log(userData);
		process.stdout.write('loading');
		processData(userData);
	}).catch((err) => {
		console.log(err);
	});
	
	function processData(userData){
	  var index =1;
      function next() {
			if(index < 2){
				var options = {
					//For each user get there webpage
					url: `https://www.freecodecamp.org/` + userData[index].name,
					transform: body => cheerio.load(body)
				}
					rp(options)
					  .then(function($){
						console.log("Found "+userData[index].name);
						// Will output . while processing data
						process.stdout.write(`>>>`);
						// Check account exists
						const fccAccount = $('h1.landing-heading').length == 0;
						
						// How many challenges they passed
						const challengesPassed = fccAccount ? $('tbody tr').length : 'unknown';
						// add to table
						table.push([userData[index].name, userData[index].likes_received, challengesPassed]);
						++index;
						//Recursive call
						return next();
					}).catch( (error) => {
						//console.log(error);
					   console.log("Not Found https://www.freecodecamp.org/"+userData[index].name);
					   ++index;
					   return next();
					});
			}  else {
					printTable();
				}
		}
		return next();
	};
	
	


function printTable(){
		//console.log("✅");
	 // console.log(table.toString());
    }