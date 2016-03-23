BGDrawable.currLabel = 1;
function BGDrawable() {
	BGDrawable.currLabel += 1;
	this.label = "Drawable"+BGDrawable.currLabel;
	
	this.x = 0;
	this.y = 0;
	this.width = 0;
	this.height = 0;
	
	this.color = BGDrawable.defaultColor;
	
	this.fontFamily = BGDrawable.defaultFontFamily;
	this.fontSize = BGDrawable.defaultFontSize;
	
	this.visible = true;
	
	this.setFontSize = function(size) {
		this.fontSize = size;
	}
	
	this.render = function(ctx) {
		//console.log("Called component render");
		if (this.visible) {
			ctx.fillStyle=this.color;
			ctx.font = this.fontSize + "px " + this.fontFamily;
			this.renderComponent(ctx);
		}
	}
	this.renderComponent = function(ctx) {}
	this.clickEvent = function(event) {}
}
BGDrawable.defaultFontFamily = "Arial";
BGDrawable.defaultFontSize = 30;
BGDrawable.defaultColor = "#000000";