var webSocket;
var socketListener = function(text) {};

function openSocket() {
	// Ensures only one connection is open at a time
	if(webSocket !== undefined && webSocket.readyState !== WebSocket.CLOSED){
		writeResponse("WebSocket is already opened.");
		return;
	}
	if (window.location.hostname == "") {
		console.log("Running in local debug mode; no connection");
		return;
	}
	// Create a new instance of the websocket
	webSocket = new WebSocket("ws://" + window.location.hostname + ":8080/boardgame");

	/**
	* Binds functions to the listeners for the websocket.
	*/
	webSocket.onopen = function(event) {
		if (event.data === undefined)
			return;
		//writeResponse(event.data);
		console.log("Opened");
		connStatus.color = "#00FF00";
		render();
	};
	webSocket.onmessage = function(event) {
		//writeResponse(event.data);
		socketListener(event.data);
		connStatus.color = "#00FF00";
		render();
	};
	webSocket.onclose = function(event) {
		//writeResponse("Connection closed");
		connStatus.color = "#FF0000";
		init();
		render();
	};
}

/**
* Sends the value of the text input to the server
*/
function send(packet) {
	console.log("Sending: " + packet);
	webSocket.send(packet);
}

function closeSocket() {
	webSocket.close();
}

function writeResponse(text) {
	socketListener(text);
}
