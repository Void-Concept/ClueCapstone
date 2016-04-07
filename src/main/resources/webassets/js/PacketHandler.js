function PacketHandler() {
	this.handlePacket = function(packet) {
		try {
			var jpacket = JSON.parse(packet);
			console.log(jpacket);
			switch (jpacket.Command) {
				case "onOpen":
					console.log("TODO: onOpen");
					break;
			}
		} catch(err) {
			console.log(err);
		}
	}
}