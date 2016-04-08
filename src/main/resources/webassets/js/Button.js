function Button(x, y, width, height, text) {
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;
	this.text = text;
	this.flashColor = "#FF0000";
	var flash = false;
	var flashTimer = null;
	this.clickListener = null;
	this.borderless = false;
	
	this.renderComponent = function(ctx) {
		if (!this.borderless) {
			ctx.rect(this.x, this.y, this.width, this.height);
			ctx.stroke();
		}
		
		if (flash) {
			ctx.fillStyle = this.flashColor;
			ctx.fillRect(this.x, this.y, this.width, this.height);
			ctx.fillStyle = this.color;
		}
		
		//console.log("Color: "+ this.color + " Background: " + this.backgroundColor + " Font: " + ctx.font + " strokeStyle: " + ctx.strokeStyle + " fillStyle: ");
		
		/*
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
		*/
		
		var metrics = ctx.measureText(this.text);
		var offsetX = (this.width - metrics.width)/2;
		var offsetY = (this.fontSize*.75 + this.height)/2;
		ctx.fillText(this.text, this.x + offsetX, this.y + offsetY);
	}
	var flashOff = function() {
		flash = false;
		render();
		window.clearTimeout(flashTimer);
	}
	this.clickEvent = function(event) {
		console.log("Button clicked");
		
		flash = true;
		render();
		flashTimer = setTimeout(flashOff, 100);
		
		if (this.clickListener != null) {
			this.clickListener(event);
		}
		
		var packet = new Packet();
		packet.command = "onClick";
		packet.addParameter("view", this.label);
		send(packet.getJson());
	}
}
Button.prototype = new BGDrawable;