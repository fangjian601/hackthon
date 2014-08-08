var paper;
var paperWidth;
var paperHeight;
var screenWidth;
var screenHeight;
var lowerPaper;

var users = [];
var usersLoaded = 0;
var usersToLoad = 0;
var currentUser = 0;

var primaryStatGraph;

var cardXPositions = [];
var lowGraphs = [];

var canMove = true;
var host = "/query";

$( function() {
	screenWidth = $(window).width();
	screenHeight = $(window).height();
	createHomeScreen();
	$(document).keydown(function(e){
	    if (e.keyCode == 37) { 
	       // moveUser(1);
	       if(canMove)	$(".left").click();
	       return false;
	    }
	    if (e.keyCode == 39) { 
	       //moveUser(-1);
	       if(canMove)	$(".right").click();
	       return false;
	    }
	});
	$("#searchButton").click(function() {
		submitQuery();
		$("#home").fadeOut(300);
	})
});

function submitQuery() {
	var text = $("#textAreaInput").val();
	$.ajax({
			url : host,
			type: 'POST',
			contentType:"application/x-www-form-urlencoded",
			data : {text : text},
			success: function(data) {
			    //alert("Test")
			    console.dir(data);
                usersToLoad = sampleUserData.length;
                data.forEach(function(user, index) {
                    users.push(new User(user, index));
                });
			}});
}

function createTitle() {
	$("body").append("<label id='titleInApp'>GitMatch</label>");
}

function createArrows() {
	var arrowsString = '<img src="arrow_left.png" class="arrow left"/> <img src="arrow_right.png" class="arrow right"/>';
	$("body").append(arrowsString);
	$(".left").click(function() {
		moveUser(1);
	});
	$(".right").click(function() {
		moveUser(-1);
	});

	$(".arrow").click(function() {
		$(this).animate({width : "50px", height: "50px"}, 200, function() {
			$(this).animate({width: "70px", height: "70px"}, 200);
		});
	})
}

function userLoaded() {
	usersLoaded ++;
	if(usersLoaded == usersToLoad) {
		createStatScreen();
	}
}

function createStatScreen() {
	createTitle();
	paperWidth = screenWidth - 400;
	paperHeight = 500;
	lowerPaper = new Raphael(0, screenHeight - 250, screenWidth, 250);
	paper = new Raphael(200,0, paperWidth, paperHeight);
	//paper.circle(0,0,100000).attr({"fill" : "#CCC"})
	primaryStatGraph = new StatGraph(paperWidth/2,paperHeight/2, paperWidth * .18);
	primaryStatGraph.populateData(users[currentUser].stats);

	cardXPositions[0] = screenWidth * .166666;
	cardXPositions[1] = screenWidth * .333333;
	cardXPositions[2] = screenWidth * .5;
	cardXPositions[3] = screenWidth * .6666666;
	cardXPositions[4] = screenWidth * .8333333;
	cardXPositions.forEach(function(xPos, i) {
		if (i != 2) {
			lowGraphs.push(new StatGraph(xPos, 175, 70, true));
			lowGraphs[lowGraphs.length - 1].populateData(grabUser(currentUser + i).stats);
		}
	});
	users[users.length - 2].setActive(1);
	users[users.length - 1].setActive(1);
	users[0].setActive(2);
	users[1].setActive(1);
	users[2].setActive(1);

	users[users.length - 2].setPosition(0);
	users[users.length - 1].setPosition(1);
	users[0].setPosition(2);
	users[1].setPosition(3);
	users[2].setPosition(4);
	createArrows();
}

function grabUser(index) {
	if(index >= users.length) {
		return users[index - users.length];
	} else if(index < 0) {
		return users[index + users.length];
	}
	return users[index];
}

function moveUser(dir) {
	if(canMove) {
		canMove = false;
		setTimeout(function() {
			canMove = true;
		}, 650);
		currentUser += dir;
		if(currentUser < 0) {
			currentUser = users.length - 1;
		} else if(currentUser >= users.length) {
			currentUser -= users.length;
		}
		var counter = 0;
		for(var i = currentUser - 2; i <= currentUser + 2; i++) {
			var user = grabUser(i);
			if (i != currentUser) {
				user.setActive(1, dir);
				var subCounter = counter;
				if(subCounter > 2) subCounter --;
				lowGraphs[subCounter].populateData(user.stats);
			} else {
				user.setActive(2);
				primaryStatGraph.populateData(user.stats);
			}
			user.setPosition(counter);
			counter ++;
			user.turnEnded = true;
		}
		users.forEach(function(user) {
			if(!user.turnEnded) {
				user.setActive(0);
			}
			user.turnEnded = false;
		})

	}
}

function createHomeScreen() {
	$("#home").fadeIn(750);
}

function generateSampleData() {
	usersToLoad = sampleUserData.length;
	sampleUserData.forEach(function(user, index) {
		users.push(new User(user, index));
	});
}

function createArrow(direction) {

}







// var sampleUserData = 
// { "user_id": "fangjian",
//   "attributes": [
// 	    {"name": "java", "score": 60},
// 		{"name": "c++", "score": 32},
// 		{"name": "iOS", "score": 80},
// 		{"name": "ruby on rails", "score": 93},
// 		{"name": "Android", "score": 50},
// 		{"name": "node.js", "score": 55},
// 		{"name": "express.js", "score": 34},
// 		{"name": "python", "score": 72},
// 		{"name": "java", "score": 60},
// 		{"name": "c++", "score": 32},
// 		{"name": "iOS", "score": 80},
// 		{"name": "ruby on rails", "score": 93},
// 		{"name": "Android", "score": 50},
// 		{"name": "node.js", "score": 55},
// 		{"name": "express.js", "score": 34},
// 		{"name": "python", "score": 72},
// 		{"name": "java", "score": 60},
// 		{"name": "c++", "score": 32}
// 	]
// }

function getRandomInt(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

var sampleUserData =
[
  {"user_id": "fangjian",
   "attributes": 
   [
    				{"name": "java", "score": 57},
			{"name": "c++", "score": 69},
			{"name": "iOS", "score": 88},
			{"name": "ruby on rails", "score": 47},
			    	{"name": "HTML", "score": 57},
			{"name": "c++", "score": 68},
			{"name": "Android", "score": 80},
			{"name": "Hadoop", "score": 97},
			    	{"name": "C#", "score": 64},
			{"name": "SQL", "score": 47},
			{"name": "python", "score": 44},
			{"name": "node.js", "score": 32},
			    	{"name": "express.js", "score": 58},
			{"name": "assembly", "score": 74},
			{"name": "angular.js", "score": 58},
			{"name": "Jquery", "score": 97}
		]
	},

	  {"user_id": "xdaniellin",
   "attributes": 
   [
    				{"name": "java", "score": 45},
			{"name": "c++", "score": 35},
			{"name": "iOS", "score": 58},
			{"name": "ruby on rails", "score": 47},
			    	{"name": "HTML", "score": 98},
			{"name": "c++", "score": 96},
			{"name": "Android", "score": 78},
			{"name": "Hadoop", "score": 75},
			    	{"name": "C#", "score": 59},
			{"name": "SQL", "score": 76},
			{"name": "python", "score": 74},
			{"name": "node.js", "score": 85},
			    	{"name": "express.js", "score": 46},
			{"name": "assembly", "score": 57},
			{"name": "angular.js", "score": 48},
			{"name": "Jquery", "score": 88}
		]
	},

	  {"user_id": "ryhan",
   "attributes": 
   [
    				{"name": "java", "score": 33},
			{"name": "c++", "score": 68},
			{"name": "iOS", "score": 87},
			{"name": "ruby on rails", "score": 55},
			    	{"name": "HTML", "score": 57},
			{"name": "c++", "score": 78},
			{"name": "Android", "score": 57},
			{"name": "Hadoop", "score": 48},
			    	{"name": "C#", "score": 86},
			{"name": "SQL", "score": 78},
			{"name": "python", "score": 76},
			{"name": "node.js", "score": 47},
			    	{"name": "express.js", "score": 67},
			{"name": "assembly", "score": 68},
			{"name": "angular.js", "score": 64},
			{"name": "Jquery", "score": 38}
		]
	},

	  {"user_id": "christianreyes",
   "attributes": 
   [
    				{"name": "java", "score": 57},
			{"name": "c++", "score": 87},
			{"name": "iOS", "score": 46},
			{"name": "ruby on rails", "score": 75},
			    	{"name": "HTML", "score": 36},
			{"name": "c++", "score": 58},
			{"name": "Android", "score": 75},
			{"name": "Hadoop", "score": 48},
			    	{"name": "C#", "score": 48},
			{"name": "SQL", "score": 68},
			{"name": "python", "score": 75},
			{"name": "node.js", "score": 94},
			    	{"name": "express.js", "score": 44},
			{"name": "assembly", "score": 37},
			{"name": "angular.js", "score": 27},
			{"name": "Jquery", "score": 85}
		]
	},

	  {"user_id": "leah",
   "attributes": 
   [
    				{"name": "java", "score": 36},
			{"name": "c++", "score": 77},
			{"name": "iOS", "score": 66},
			{"name": "ruby on rails", "score": 68},
			    	{"name": "HTML", "score": 62},
			{"name": "c++", "score": 71},
			{"name": "Android", "score": 64},
			{"name": "Hadoop", "score": 68},
			    	{"name": "C#", "score": 87},
			{"name": "SQL", "score": 36},
			{"name": "python", "score": 66},
			{"name": "node.js", "score": 46},
			    	{"name": "express.js", "score": 64},
			{"name": "assembly", "score": 53},
			{"name": "angular.js", "score": 77},
			{"name": "Jquery", "score": 72}
		]
	},

	  {"user_id": "adidahiya",
   "attributes": 
   [
    				{"name": "java", "score": 66},
			{"name": "c++", "score": 76},
			{"name": "iOS", "score": 47},
			{"name": "ruby on rails", "score": 76},
			    	{"name": "HTML", "score": 67},
			{"name": "c++", "score": 68},
			{"name": "Android", "score": 84},
			{"name": "Hadoop", "score": 94},
			    	{"name": "C#", "score": 92},
			{"name": "SQL", "score": 67},
			{"name": "python", "score": 82},
			{"name": "node.js", "score": 67},
			    	{"name": "express.js", "score": 77},
			{"name": "assembly", "score": 56},
			{"name": "angular.js", "score": 65},
			{"name": "Jquery", "score": 55}
		]
	},

	  {"user_id": "alicelee",
   "attributes": 
   [
    				{"name": "java", "score": 47},
			{"name": "c++", "score": 84},
			{"name": "iOS", "score": 78},
			{"name": "ruby on rails", "score": 88},
			    	{"name": "HTML", "score": 78},
			{"name": "c++", "score": 67},
			{"name": "Android", "score": 46},
			{"name": "Hadoop", "score": 78},
			    	{"name": "C#", "score": 23},
			{"name": "SQL", "score": 88},
			{"name": "python", "score": 68},
			{"name": "node.js", "score": 97},
			    	{"name": "express.js", "score": 90},
			{"name": "assembly", "score": 92},
			{"name": "angular.js", "score": 79},
			{"name": "Jquery", "score": 89 }
		]
	},

	  {"user_id": "faheempatel",
   "attributes": 
   [
    				{"name": "java", "score": 46},
			{"name": "c++", "score": 76},
			{"name": "iOS", "score": 56},
			{"name": "ruby on rails", "score": 45},
			    	{"name": "HTML", "score": 65},
			{"name": "c++", "score": 44},
			{"name": "Android", "score": 56},
			{"name": "Hadoop", "score": 87},
			    	{"name": "C#", "score": 87},
			{"name": "SQL", "score": 69},
			{"name": "python", "score": 93},
			{"name": "node.js", "score": 34},
			    	{"name": "express.js", "score": 94},
			{"name": "assembly", "score": 93},
			{"name": "angular.js", "score": 83},
			{"name": "Jquery", "score": 88}
		]
	},

	  {"user_id": "hopkinschris",
   "attributes": 
   [
    				{"name": "java", "score": 97},
			{"name": "c++", "score": 78},
			{"name": "iOS", "score": 69},
			{"name": "ruby on rails", "score": 57},
			    	{"name": "HTML", "score": 67},
			{"name": "c++", "score": 38},
			{"name": "Android", "score": 85},
			{"name": "Hadoop", "score": 87},
			    	{"name": "C#", "score": 83},
			{"name": "SQL", "score": 92},
			{"name": "python", "score": 68},
			{"name": "node.js", "score": 54},
			    	{"name": "express.js", "score": 68},
			{"name": "assembly", "score": 87},
			{"name": "angular.js", "score": 88},
			{"name": "Jquery", "score": 57}
		]
	},

	  {"user_id": "mathiasbynens",
   "attributes": 
   [
    				{"name": "java", "score": 84},
			{"name": "c++", "score": 97},
			{"name": "iOS", "score": 84},
			{"name": "ruby on rails", "score": 67},
			    	{"name": "HTML", "score": 83},
			{"name": "c++", "score": 98},
			{"name": "Android", "score": 73},
			{"name": "Hadoop", "score": 71},
			    	{"name": "C#", "score": 79},
			{"name": "SQL", "score": 83},
			{"name": "python", "score": 67},
			{"name": "node.js", "score": 60},
			    	{"name": "express.js", "score": 81},
			{"name": "assembly", "score": 88},
			{"name": "angular.js", "score": 93},
			{"name": "Jquery", "score": 85}
		]
	},

	  {"user_id": "harperreed",
   "attributes": 
   [
    				{"name": "java", "score": 72},
			{"name": "c++", "score": 89},
			{"name": "iOS", "score": 57},
			{"name": "ruby on rails", "score": 78},
			    	{"name": "HTML", "score": 75},
			{"name": "c++", "score": 87},
			{"name": "Android", "score": 45},
			{"name": "Hadoop", "score": 75},
			    	{"name": "C#", "score": 79},
			{"name": "SQL", "score": 87},
			{"name": "python", "score": 71},
			{"name": "node.js", "score": 77},
			    	{"name": "express.js", "score": 78},
			{"name": "assembly", "score": 86},
			{"name": "angular.js", "score": 57},
			{"name": "Jquery", "score": 78}
		]
	},

	  {"user_id": "henriquea",
   "attributes": 
   [
    				{"name": "java", "score": 57},
			{"name": "c++", "score": 68},
			{"name": "iOS", "score": 47},
			{"name": "ruby on rails", "score": 69},
			    	{"name": "HTML", "score": 87},
			{"name": "c++", "score": 87},
			{"name": "Android", "score": 69},
			{"name": "Hadoop", "score": 97},
			    	{"name": "C#", "score": 86},
			{"name": "SQL", "score": 72},
			{"name": "python", "score": 97},
			{"name": "node.js", "score": 86},
			    	{"name": "express.js", "score": 42},
			{"name": "assembly", "score": 59},
			{"name": "angular.js", "score": 57},
			{"name": "Jquery", "score": 73}
		]
	},

	  {"user_id": "imsky",
   "attributes": 
   [
    				{"name": "java", "score": 86},
			{"name": "c++", "score": 47},
			{"name": "iOS", "score": 43},
			{"name": "ruby on rails", "score": 55},
			    	{"name": "HTML", "score": 78},
			{"name": "c++", "score": 89},
			{"name": "Android", "score": 65},
			{"name": "Hadoop", "score": 44},
			    	{"name": "C#", "score": 44},
			{"name": "SQL", "score": 52},
			{"name": "python", "score": 56},
			{"name": "node.js", "score": 89},
			    	{"name": "express.js", "score": 98},
			{"name": "assembly", "score": 91},
			{"name": "angular.js", "score": 69},
			{"name": "Jquery", "score": 82}
		]
	},

	  {"user_id": "soulwire",
   "attributes": 
   [
    				{"name": "java", "score": 58},
			{"name": "c++", "score": 68},
			{"name": "iOS", "score": 76},
			{"name": "ruby on rails", "score": 84},
			    	{"name": "HTML", "score": 67},
			{"name": "c++", "score": 74},
			{"name": "Android", "score": 84},
			{"name": "Hadoop", "score": 48},
			    	{"name": "C#", "score": 78},
			{"name": "SQL", "score": 70},
			{"name": "python", "score": 94},
			{"name": "node.js", "score": 85},
			    	{"name": "express.js", "score": 73},
			{"name": "assembly", "score": 52},
			{"name": "angular.js", "score": 29},
			{"name": "Jquery", "score": 85}
		]
	},

	  {"user_id": "joecritch",
   "attributes": 
   [
    				{"name": "java", "score": 87},
			{"name": "c++", "score": 56},
			{"name": "iOS", "score": 86},
			{"name": "ruby on rails", "score": 79},
			    	{"name": "HTML", "score": 86},
			{"name": "c++", "score": 65},
			{"name": "Android", "score": 86},
			{"name": "Hadoop", "score": 87},
			    	{"name": "C#", "score": 82},
			{"name": "SQL", "score": 91},
			{"name": "python", "score": 71},
			{"name": "node.js", "score": 79},
			    	{"name": "express.js", "score": 84},
			{"name": "assembly", "score": 76},
			{"name": "angular.js", "score": 89},
			{"name": "Jquery", "score": 74}
		]
	},

	  {"user_id": "Z1MM32M4N",
   "attributes": 
   [
    				{"name": "java", "score": 72},
			{"name": "c++", "score": 67},
			{"name": "iOS", "score": 88},
			{"name": "ruby on rails", "score": 53},
			    	{"name": "HTML", "score": 57},
			{"name": "c++", "score": 79},
			{"name": "Android", "score": 65},
			{"name": "Hadoop", "score": 79},
			    	{"name": "C#", "score": 88},
			{"name": "SQL", "score": 58},
			{"name": "python", "score": 65},
			{"name": "node.js", "score": 90},
			    	{"name": "express.js", "score": 51},
			{"name": "assembly", "score": 61},
			{"name": "angular.js", "score": 78},
			{"name": "Jquery", "score": 65}
		]
	},

	  {"user_id": "theycallmeswift",
   "attributes": 
   [
    				{"name": "java", "score": 66},
			{"name": "c++", "score": 78},
			{"name": "iOS", "score": 88},
			{"name": "ruby on rails", "score": 66},
			    	{"name": "HTML", "score": 77},
			{"name": "c++", "score": 57},
			{"name": "Android", "score": 37},
			{"name": "Hadoop", "score": 76},
			    	{"name": "C#", "score": 98},
			{"name": "SQL", "score": 74},
			{"name": "python", "score": 67},
			{"name": "node.js", "score": 68},
			    	{"name": "express.js", "score": 77},
			{"name": "assembly", "score": 81},
			{"name": "angular.js", "score": 84},
			{"name": "Jquery", "score": 58}
		]
	},

	  {"user_id": "mmirman",
   "attributes": 
   [
    				{"name": "java", "score": 96},
			{"name": "c++", "score": 49},
			{"name": "iOS", "score": 99},
			{"name": "ruby on rails", "score": 87},
			    	{"name": "HTML", "score": 94},
			{"name": "c++", "score": 84},
			{"name": "Android", "score": 75},
			{"name": "Hadoop", "score": 79},
			    	{"name": "C#", "score": 94},
			{"name": "SQL", "score": 75},
			{"name": "python", "score": 64},
			{"name": "node.js", "score": 54},
			    	{"name": "express.js", "score": 64},
			{"name": "assembly", "score": 53},
			{"name": "angular.js", "score": 82},
			{"name": "Jquery", "score":62 }
		]
	},

	  {"user_id": "envoked",
   "attributes": 
   [
    				{"name": "java", "score": 86},
			{"name": "c++", "score": 46},
			{"name": "iOS", "score": 78},
			{"name": "ruby on rails", "score": 55},
			    	{"name": "HTML", "score": 66},
			{"name": "c++", "score": 36},
			{"name": "Android", "score": 72},
			{"name": "Hadoop", "score": 62},
			    	{"name": "C#", "score": 37},
			{"name": "SQL", "score": 68},
			{"name": "python", "score": 46},
			{"name": "node.js", "score": 88},
			    	{"name": "express.js", "score": 77},
			{"name": "assembly", "score": 47},
			{"name": "angular.js", "score": 88},
			{"name": "Jquery", "score": 88}
		]
	},

	  {"user_id": "tijoinc",
   "attributes": 
   [
    				{"name": "java", "score": 71},
			{"name": "c++", "score": 61},
			{"name": "iOS", "score": 73},
			{"name": "ruby on rails", "score": 79},
			    	{"name": "HTML", "score": 63},
			{"name": "c++", "score": 58},
			{"name": "Android", "score": 89},
			{"name": "Hadoop", "score": 62},
			    	{"name": "C#", "score": 62},
			{"name": "SQL", "score": 69},
			{"name": "python", "score": 69},
			{"name": "node.js", "score": 80},
			    	{"name": "express.js", "score": 79},
			{"name": "assembly", "score": 71},
			{"name": "angular.js", "score": 69},
			{"name": "Jquery", "score": 91}
		]
	}
];


