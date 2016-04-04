function BGContainer {
	this.views = [];
	
	this.renderComponent = function(ctx) {
		for (i = 0; i < view.length; i++) {
			this.views[i].renderComponent(ctx);
		}
	}
}
BGContainer.prototype = new BGDrawable;