function GUI() {
	this.elements = [];
	this.addComponent = function(drawable) {
		this.elements.push(drawable);
	}
	this.renderComponent = function(ctx) {
		for (var i = 0; i < this.elements.length; i++) {
			this.elements[i].render(ctx);
		}
	}
	this.clickElement = function(event) {
		for (var i = 0; i < this.elements.length; i++) {
			var x = this.elements[i].x,
				y = this.elements[i].y
				width = this.elements[i].width
				height = this.elements[i].height;
			if ((event.absX > x && event.absX < x + width) && 
				(event.absY > y && event.absY < y + height))
				return this.elements[i];
			
		}
	}
	this.clickEvent = function(event) {
		var element = this.clickElement(event);
		if (typeof element != 'undefined')
			element.clickEvent(event);
	}
	this.clear = function() {
		this.elements = [];
	}
}