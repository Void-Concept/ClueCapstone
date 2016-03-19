BGDrawable.currLabel = 1;
function BGDrawable() {
	BGDrawable.currLabel += 1;
	BGDrawable.label = "Drawable"+BGDrawable.currLabel;
	
	this.x = 0;
	this.y = 0;
	this.width = 0;
	this.height = 0;
	
	this.color = BGDrawable.defaultColor;
	
	this.fontFamily = BGDrawable.defaultFontFamily;
	this.fontSize = BGDrawable.defaultFontSize;
	
	this.visible = true;
	
	this.render = function(ctx) {
		if (this.visible) {
			this.renderComponent(ctx);
		}
	}
	this.renderComponent = function(ctx) {}
}
BGDrawable.defaultFontFamily = "Arial";
BGDrawable.defaultFontSize = 30;
BGDrawable.defaultColor = "#000000";