function StatGraph(x, y, radius, isPreview) {
	this.x = x;
	this.y = y;
	this.totalScore = 0;
	this.easing = '>';
	this.radius = radius;
	this.isPreview = isPreview;
	this.isPreview ? this.paper = lowerPaper : this.paper = paper;
	
	this.labelPaths = [];
	this.points = [];
	this.labels = [];
	this.hexSections = [];
	this.xTriangleDistance = 86;
	this.drawBorders();
}

StatGraph.prototype.drawBorders = function() {
	var opacity = .4;
	var animTime = 1000;
	var easing = 'bounce';
	if(this.isPreview) {
		var pathString = "M" + this.x + ' ' + this.y + "L" + (this.x - this.xTriangleDistance) + ' ' + (this.y - 100)
						+ "L" + (this.x + this.xTriangleDistance) + ' ' + (this.y - 100)
						+ 'L' + this.x + ' ' + this.y; 
		this.triangle = this.paper.path(pathString).attr({"fill" : "90-#AAA:94-#777", "stroke-width" : 0 })
	}
	this.border = this.paper.circle(this.x, this.y, 0).attr({'fill' : '#FFF'}).animate({r : this.radius}, animTime, easing, function() {
		this.draw();
	}.bind(this));



	//if (!this.isPreview) {
		this.q1Boarder = this.paper.circle(this.x, this.y, 0).attr({'stroke-opacity' : 1}).animate({'stroke-opacity' : opacity, r : this.radius * .25}, animTime * .5, easing);
		this.q2Boarder = this.paper.circle(this.x, this.y, 0).attr({'stroke-opacity' : 1}).animate({'stroke-opacity' : opacity, r : this.radius * .5}, animTime * .7, easing);
		this.q3Boarder = this.paper.circle(this.x, this.y, 0).attr({'stroke-opacity' : 1}).animate({'stroke-opacity' : opacity, r : this.radius * .75}, animTime * .9, easing);
	//}
}

StatGraph.prototype.populateData = function(data) {
	this.data = data;
	if(this.hasData) {
		var animTime = 600;
		if(!this.isPreview) {
			this.hexSections.forEach(function(section) {
				section.animate({path : "M" + this.x + ' ' + this.y}, animTime, function() {
					this.remove();
				})
			}.bind(this))
		} else {
			var pathString = "M" + this.x + ' ' + this.y + "L" + (this.x - this.xTriangleDistance) + ' ' + (this.y - 96)
						+ "L" + (this.x + this.xTriangleDistance) + ' ' + (this.y - 96)
						+ 'L' + this.x + ' ' + this.y;
			this.triangle.attr({fill : "#AAA"}).animate({path : pathString}, 100, function() {
				setTimeout(function() {
					pathString = "M" + this.x + ' ' + this.y + "L" + (this.x - this.xTriangleDistance) + ' ' + (this.y - 100)
						+ "L" + (this.x + this.xTriangleDistance) + ' ' + (this.y - 100)
						+ 'L' + this.x + ' ' + this.y;
					this.triangle.attr({"fill" : "90-#AAA:94-#777"}).animate({path : pathString}, 150);
				}.bind(this), 300);
				
			}.bind(this));
			this.boldPath.animate({"path" : "M" + this.x + ' ' + this.y}, 400, function() {
				$(this).remove();
			})
			this.boldPath = null;
			// this.hexSections.forEach(function(section) {
			// 	section.remove()
			// });
		}
		
		this.hexSections = [];
		this.points = [];
		this.drawPoints();
		if(this.isPreview) {
			this.linkPoints();
		} else {
			this.linkPointSegments();
		}
		
	}
	this.hasData = true;
}

StatGraph.prototype.draw = function() {
	if (!this.isPreview) {
		this.drawLabelLines();
		this.drawLabelText();
	}
	this.drawPoints();
	if(this.isPreview) {
		this.linkPoints();
	} else {
		this.linkPointSegments();
	}	
	//this.linkPoints();
}

StatGraph.prototype.drawLabelLines = function() {
	var deltaRadian = (Math.PI * 2) / this.data.length;
	this.data.forEach( function(attribute, index) {
		this.totalScore += attribute.score;
		var x = this.x + this.radius * Math.sin(deltaRadian * index);
		var y = this.y + this.radius * -1 * Math.cos(deltaRadian * index);
		var line = this.paper.path('M' + this.x + ' ' + this.y + 'L' + x + ' ' + y).attr({'stroke-opacity' : 0}).animate({'stroke-opacity' : .25}, 750);
		this.labelPaths.push(line);
	}.bind(this));
}

StatGraph.prototype.drawLabelText = function() {
	var deltaRadian = (Math.PI * 2) / this.data.length;
	this.data.forEach( function(attribute, index) {
		var x = this.x + this.radius * Math.sin(deltaRadian * index) * 1.15;
		var y = this.y + this.radius * -1 * Math.cos(deltaRadian * index) * 1.15;
		var text = this.paper.text(x, y, attribute.name).attr({'font-size': 13, 'opacity' : 0, 'font-family' : 'Roboto', 'font-weight' : 'bold'})
			.animate({'opacity' : .8}, 350 + index * 80, 'back-in');
		this.labels.push(text);
	}.bind(this));
}

StatGraph.prototype.drawPoints = function() {
	var deltaRadian = (Math.PI * 2) / this.data.length;
	this.data.forEach( function(attribute, index) {
		var strength = attribute.score/100;
		var x = this.x + (this.radius * strength) * Math.sin(deltaRadian * index);
		var y = this.y + (this.radius * strength) * -1 * Math.cos(deltaRadian * index);
		//var cir = paper.circle(x, y, 3).attr({fill : '#0F0', 'stroke-width' : 0}).animate({cx : x, cy : y}, 1000 * strength);
		var pointObj = {x : x, y : y, strength : strength};
		this.points.push(pointObj);
		
	}.bind(this));
}

StatGraph.prototype.linkPoints = function() {
	var pathString = 'M' + this.points[0].x + ' ' + this.points[0].y;
	this.points.forEach( function(point) {
		pathString += 'L' + point.x + ' ' + point.y;
	}.bind(this));
	pathString += 'L' + this.points[0].x + ' ' + this.points[0].y;
	this.boldPath = this.paper.path("M" + this.x + ' ' + this.y).attr({'fill': '#070','fill-opacity': .8, 'stroke' : '#070', 'stroke-width' : 2, 'stroke-opacity' : 1})
		.animate({'path' : pathString}, 500, this.easing);
}

StatGraph.prototype.linkPointSegments = function() {
	var totalPoints = this.points.length;
	this.points.forEach( function(point, index) {
		var p1x;
		var p1y;

		var p3x;
		var p3y;

		if (index === 0) {
			p1x = this.points[totalPoints - 1].x;
			p1y = this.points[totalPoints - 1].y;
		} else {
			p1x = this.points[index - 1].x;
			p1y = this.points[index - 1].y;
		}

		if (index === totalPoints-1) {
			p3x = this.points[0].x;
			p3y = this.points[0].y;
		} else {
			p3x = this.points[index + 1].x;
			p3y = this.points[index + 1].y;
		}

		p1x = (p1x + point.x)/2;
		p1y = (p1y + point.y)/2;

		p3x = (point.x + p3x)/2;
		p3y = (point.y + p3y)/2;

		var pathString = 'M' + this.x + ' ' + this.y;
		pathString += 'L' + p1x + ' ' + p1y;
		pathString += 'L' + point.x + ' ' + point.y;
		//pathString += 'L' + point.x * 1.1 + ' ' + point.y * 1.1;
		//pathString += 'M' + point.x + ' ' + point.y;
		pathString += 'L' + p3x + ' ' + p3y;
		//paper.circle(p1.x,p1.y, 2);
		//paper.circle(point.x, point.y, 4);
		//paper.circle(p3.x,p3.y, 2);
		pathString += 'L' + this.x + ' ' + this.y;
		var dasPath = this.paper.path("M" + this.x + ' ' + this.y).attr({'fill' : '#070', 'fill-opacity' : 0.9, 'stroke-width' : 0, stroke : '#FFA'})
		.animate({'path' : pathString, 'fill-opacity' : .9}, 700 * point.strength, '<')
		.mouseover(function() {
			this.toFront().animate({'stroke-width' : 1}, 75);
			this.statGraph.labels[this.index].animate({'font-size' : 17}, 75);
		}).mouseout(function() {
			this.animate({'stroke-width' : 0}, 75);
			this.statGraph.labels[this.index].animate({'font-size' : 13}, 75);
		});
		dasPath.index = index;
		dasPath.statGraph = this;
		this.hexSections.push(dasPath);
	}.bind(this));
}












