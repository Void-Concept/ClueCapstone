var webSocket;
var socketListener = function(text) {};

function openSocket() {
	// Ensures only one connection is open at a time
	if(webSocket !== undefined && webSocket.readyState !== WebSocket.CLOSED){
		writeResponse("WebSocket is already opened.");
		return;
	}
	// Create a new instance of the websocket
	webSocket = new WebSocket("ws://" + window.location.hostname + ":8080/echo");

	/**
	* Binds functions to the listeners for the websocket.
	*/
	webSocket.onopen = function(event){
		if(event.data === undefined)
			return;
		writeResponse(event.data);
	};
	webSocket.onmessage = function(event){
		writeResponse(event.data);
	};
	webSocket.onclose = function(event){
		writeResponse("Connection closed");
	};
}

/**
* Sends the value of the text input to the server
*/
function send(text){
	console.log("Sending: " + text);
	webSocket.send(text);
}

function closeSocket(){
	webSocket.close();
}

function writeResponse(text){
	socketListener(text);
}
