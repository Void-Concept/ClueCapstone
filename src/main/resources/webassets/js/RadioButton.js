function RadioButton(x, y, width, height, lineWidth = 5) {
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;
	this.checked = false;
	this.lineWidth = lineWidth;
	
	this.setChecked = function(state) {
		this.checked = state;
	}
	
	this.renderComponent = function(ctx) {
		ctx.rect(this.x, this.y, this.width, this.height);
		ctx.stroke();
		if (this.checked) {
			var savedWidth = ctx.lineWidth;
			ctx.lineWidth = this.lineWidth;
			ctx.beginPath();
			ctx.moveTo(this.x + this.width*.1, this.y + this.height/2);
			ctx.lineTo(this.x + this.width/2, this.y + this.height*.9);
			ctx.lineTo(this.x + this.width*.9, this.y+this.height*.05);
			ctx.stroke();
			ctx.lineWidth = savedWidth;
		}
	}
	this.clickEvent = function(event) {
		this.checked = !this.checked;
		console.log("Button clicked");
		console.log(this.checked);
		render();
	}
}
RadioButton.prototype = new BGDrawable;