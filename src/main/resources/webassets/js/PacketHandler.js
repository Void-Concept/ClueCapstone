function PacketHandler() {
	this.handlePacket = function(packet) {
		try {
			var jpacket = JSON.parse(packet);
			console.log(jpacket);
			switch (jpacket.Command) {
				case "onOpen":
					console.log("TODO: onOpen");
					break;
				case "draw":
					console.log("TODO: draw");
					break;
				case "reject":
					console.log("TODO: reject");
					break;
			}
		} catch(err) {
			console.log(err);
		}
	}
}