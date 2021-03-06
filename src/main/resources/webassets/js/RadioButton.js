function RadioButton(x, y, width, height, lineWidth) {
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;
	this.checked = false;
	this.enabled = true;
	if (lineWidth = undefined)
		this.lineWidth = 5;
	else
		this.lineWidth = lineWidth
	
	this.setChecked = function(state) {
		if (this.enabled) {
			this.checked = state;
		}
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
		if (this.enabled) {
			this.checked = !this.checked;
			console.log("Button clicked");
			console.log(this.checked);
			render();
			
			var packet = new Packet();
			packet.command = "radioToggle";
			var param = new Object();
			param.view = this.label;
			param.state = this.checked;
			//packet.addParameter("view", this.label);
			//packet.addParameter("state", this.checked);
			packet.addParameters(param);
			send(packet.getJson());
		}
	}
}
RadioButton.prototype = new BGDrawable;