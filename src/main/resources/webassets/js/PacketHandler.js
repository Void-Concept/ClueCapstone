function PacketHandler() {
	this.handlePacket = function(packet) {
		try {
			console.log(packet);
			var jpacket = JSON.parse(packet);
			console.log(jpacket);
			switch (jpacket.Command) {
				case "onOpen":
					console.log("TODO: onOpen");
					break;
				case "draw":
					gui.clear();
					comp = this.convertComponent(jpacket.Parameters[0]);
					console.log(comp.backgroundColor);
					var gameview = document.getElementById("gameview");
					gameview.style.background = comp.backgroundColor
					gui.addComponent(comp);
					break;
				case "reject":
					console.log("TODO: reject");
					break;
			}
		} catch(err) {
			console.log(err);
		}
	}
	
	this.drawablePart = function(p, comp) {
		comp.x = p.x;
		comp.y = p.y;
		comp.width = p.width;
		comp.height = p.height;
		comp.fontFamily = p.fontFamily;
		comp.fontSize = p.fontSize;
		comp.label = p.label;
		comp.color = p.color;
		comp.backgroundColor = p.backgroundColor;
		return comp;
	}
	
	this.convertComponent = function(p) {
		//console.log(p);
		comp = new BGDrawable();
		switch (p.type) {
			case "label":
				//console.log("Got label: " + p.label);
				comp = new Label(p.x, p.y, p.text);
				comp = this.drawablePart(p, comp);
				break;
			case "container":
				//console.log("Got container: " + p.label + " length " + p.components.length);
				var comp = new BGContainer();
				comp = this.drawablePart(p, comp);
				for (var i = 0; i < p.components.length; i++) {
					subComp = this.convertComponent(p.components[i]);
					comp.addComponent(subComp);
					//console.log("Returned to container: " + p.label);
				}
				break;
			case "radiobutton":
				//console.log("Got radiobutton: " + p.label);
				comp = new RadioButton();
				comp = this.drawablePart(p, comp);
				comp.checked = p.checked;
				comp.lineWidth = p.lineWidth;
				comp.enabled = p.enabled;
				break;
			case "button":
				//console.log("Got button: " + p.label);
				comp = new Button();
				comp = this.drawablePart(p, comp);
				comp.text = p.text;
				break;
		}
		return comp
	}
}