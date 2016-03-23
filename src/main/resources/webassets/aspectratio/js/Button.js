
function Button(x, y, width, height, text) {
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;
	this.text = text;
	
	this.renderComponent = function(ctx) {
		ctx.rect(this.x, this.y, this.width, this.height);
		ctx.stroke();
		var metrics = ctx.measureText(text);
		var offsetX = (this.width - metrics.width)/2;
		var offsetY = (this.fontSize*.75 + height)/2;
		ctx.fillText(this.text, this.x + offsetX, this.y + offsetY);
	}
	this.clickEvent = function(event) {
		
	}
}
Button.prototype = new BGDrawable;