function User(userData, index) {
	this.login = userData.user_id;
	this.stats = userData.attributes;
	this.getGitData();
	this.activeLevel = 0;
	this.position = -1;
	this.index = index;
	this.hasCard = false;
	this.turnEnded = false;
}

User.prototype.getGitData = function() {
	$.get('http://api.github.com/users/' + this.login, function(data) {
		this.gitdata = data;
		if(typeof this.gitdata.name === 'undefined') this.gitdata.name = "No name";
		userLoaded();
	}.bind(this));
}

User.prototype.setActive = function(level, dir) {
	var oldActive = this.activeLevel;
	this.activeLevel = level;
	if (oldActive == 0 && this.activeLevel == 1 && !this.hasCard) {
		this.createCard(dir);
	} else if (oldActive == 1 && this.activeLevel == 0) {
	} else if (oldActive == 2 && this.activeLevel != 2) {
		this.unfocusCard();
	} else if (this.activeLevel == 2){
		if(!this.hasCard) {
			this.createCard(dir);
		}
		this.focusCard();
	}

	if(this.activeLevel < 0) {
		this.activeLevel = 0;
	} else if(this.activeLevel > 2) {
		this.activeLevel = 2;
	}
	if(this.activeLevel == 0) {
		this.destroyCard();
	}
}

User.prototype.setPosition = function(position) {
	this.position = position;
	if(this.focusIt) {
		this.focusIt = false;
		$("#card" + this.index).animate({left : cardXPositions[this.position] - 230/2 + "px", "height" : "250px", 'width' : '230px', "bottom" : "60px"}, 600);
	} else if(this.unfocusIt) {
		this.unfocusIt = false;
		$("#card" + this.index).animate({left : cardXPositions[this.position] - 210/2 + "px", "height" : "90px", 'width' : '210px', "bottom" : "175px"}, 600);
	} else {
		$("#card" + this.index).animate({left : cardXPositions[this.position] - $("#card" + this.index).width()/2 + "px"}, 600);
	}
	
}

User.prototype.createCard = function(dir) {
	var cardString = '<div class="card" id="card' + this.index + '"> <img class="avatar" src="' + 
				this.gitdata.avatar_url	+ '"> '
				+ '<label class="cardName bold">' + this.gitdata.name + '</label>' 
				+ '<div class="cardName">' + this.gitdata.login + '</div>' 
				+ '<div class="cardName"><span class="bold">' + this.gitdata.followers + '</span> followers</div>' 
		+ '</div>';
	$('body').append(cardString);
	var leftDistance = "-200px";
	if(dir == 1) {
		leftDistance = screenWidth + 200 + "px";
	}
	$("#card" + this.index).css({"bottom" : "175px", "left" : leftDistance});
	this.hasCard = true;
}

User.prototype.destroyCard = function() {
	if(this.position < 1) {
		$("#card" + this.index).animate({"left" : "-200px"}, 300);
		setTimeout(function() {
			$("#card" + this.index).remove();
		}.bind(this), 300)
	} else if (this.position > 3) {
		$("#card" + this.index).animate({"left" : (screenWidth + 200) + "px"}, 300);
		setTimeout(function() {
			$("#card" + this.index).remove();
		}.bind(this), 300)
	} else {
		$("#card" + this.index).remove();
	}
	
	this.hasCard = false;
}

User.prototype.focusCard = function() {
	this.focusIt = true;
	if(this.gitdata.company) {
		//$("#card" + this.index).append('<div class="company bu" id="link4' + this.index +'"><center>Company: ' + this.gitdata.company + '</center></div>');
		//$("#link4" + this.index).delay(220).fadeIn(350);

	}
	$("#card" + this.index).append('<div class="email bu"><center><a href="mailto:' + this.gitdata.email + '"  class="hidden" id="link1' + this.index + '">' + "Send an email" + '<img src="mail.png" class="imgIcon"/></a></center></div>');
	$("#card" + this.index).append('<div class="linked bu"><center><a href="https://www.linkedin.com/vsearch/f?type=all&keywords=' + this.gitdata.name.replace(' ', '+') + '" target="_blank" class="hidden" id="link2' + this.index + '">Find on LinkedIn<img src="linkedin.png" class="imgIcon"/></a></center></div>');
	$("#card" + this.index).append('<div class="linked bu"><center><a href="' + this.gitdata.html_url + '" target="_blank" class="hidden" id="link3' + this.index + '">View Github account<img src="github.png" class="imgIcon"/></a></center></div>');
	
	$("#link1" + this.index).delay(150).fadeIn(350);
	$("#link2" + this.index).delay(170).fadeIn(350);
	$("#link3" + this.index).delay(190).fadeIn(350);

	$("#card" + this.index).find("img").animate({"border-bottom-left-radius" : "0px", "border-width": "1px"}, 200);

	
	//$("#card" + this.index).animate({"height" : "250px", "bottom" : "20px"}, 300);
}

User.prototype.unfocusCard = function() {
	this.unfocusIt = true;
	$("#link1" + this.index).remove();
	$("#link2" + this.index).remove();
	$("#link3" + this.index).remove();
	$("#link4" + this.index).remove();
	$("#card" + this.index).find("img").animate({"border-bottom-left-radius" : "15px", "border-width" : "0px"}, 200);
	//$("#card" + this.index).animate({"height" : "90px", "bottom" : "175px"}, 300);
}
