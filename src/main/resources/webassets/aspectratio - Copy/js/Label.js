function Label(x, y, text) {
	this.fontSize = 30;
	this.fontFamily = "Arial"
	this.x = x;
	this.y = y;
	this.text = text;
	this.renderComponent = function(ctx) {
		ctx.fillStyle = "#000000";
		ctx.font = this.fontSize + "px " + this.fontFamily;
		var offset = this.fontSize*.75;
		console.log(offset);
		ctx.fillText(text, x, y + offset);
	}
}