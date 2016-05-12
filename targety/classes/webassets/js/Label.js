function Label(x, y, text) {
	this.x = x;
	this.y = y;
	this.text = text;
	this.renderComponent = function(ctx) {
		ctx.fillStyle = this.color;
		ctx.font = this.fontSize + "px " + this.fontFamily;
		var offset = this.fontSize*.75;
		ctx.fillText(this.text, this.x, this.y + offset);
	}
}
Label.prototype = new BGDrawable;