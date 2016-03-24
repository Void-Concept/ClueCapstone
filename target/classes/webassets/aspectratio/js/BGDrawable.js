BGDrawable.currLabel = 1;
function BGDrawable() {
	BGDrawable.currLabel += 1;
	this.label = "Drawable"+BGDrawable.currLabel;
	
	this.x = 0;
	this.y = 0;
	this.width = 0;
	this.height = 0;
	
	this.color = BGDrawable.defaultColor;
	this.backgroundColor = BGDrawable.defaultBackgroundColor;
	
	this.fontFamily = BGDrawable.defaultFontFamily;
	this.fontSize = BGDrawable.defaultFontSize;
	
	this.visible = true;
	
	this.setFontSize = function(size) {
		this.fontSize = size;
	}
	
	this.setBackgroundColor = function(color) {
		this.background = new Rectangle(this.x, this.y, this.width, this.height, color);
	}
	
	this.render = function(ctx) {
		//console.log("Called component render");
		if (this.visible) {
			//draw background
			ctx.fillStyle = this.backgroundColor;
			ctx.fillRect(this.x, this.y, this.width, this.height);
			ctx.stroke();
			ctx.fillStyle = 'rgba(0,0,0,1)';
			
			//draw component
			ctx.strokeStyle=this.color;
			ctx.fillStyle = this.color;
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
BGDrawable.defaultBackgroundColor = 'transparent';