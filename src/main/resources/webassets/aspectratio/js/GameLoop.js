var dimX = 480;
var dimY = 854;
var gui = new GUI();
var connStatus = new Rectangle(460, 2, 10, 10, "#FF0000");

var device = {
	is_android: function() {
		return navigator.userAgent.match(/Android/i);
    },
    is_blackberry: function() {
		return navigator.userAgent.match(/BlackBerry/i);
    },
	is_iphone: function() {
		return navigator.userAgent.match(/iPhone/i);
	},
    is_ipad: function() {
        return navigator.userAgent.match(/iPad/i);
    },
    is_ipod: function() {
        return navigator.userAgent.match(/iPod/i);
    },
    is_ios: function() {
        return navigator.userAgent.match(/iPhone|iPad|iPod/i);
    },
    is_windows_phone: function() {
        return navigator.userAgent.match(/IEMobile/i);
    },
    is_mobile: function() {
		return (device.is_android() || device.is_blackberry() || device.is_ios() || device.is_windows_phone() ) != null;
    }
};
console.log(device.is_mobile());

function scaleEvent(event) {
	var elem = document.getElementById('gameview'),
    elemLeft = elem.offsetLeft,
    elemTop = elem.offsetTop;
	event.absX = (event.pageX - elemLeft) * dimX / elem.width;
	event.absY = (event.pageY - elemTop) * dimY / elem.height;
	return event;
}
function clickEvent(event) {
	scaledEvent = scaleEvent(event);
	gui.clickEvent(scaledEvent);
}

function loadGame() {
	gui.clear();
	
	var gameview = document.getElementById("gameview");
	gameview.style.background = "#FFA500"
	
	gui.addComponent(new Label(220, 10, "Clue"));
	
	var suspects = ["Mr. Green", "Colonel Mustard", "Ms. Peacock", "Prof. Plum", "Ms. Scarlet", "Ms. White"];
	var weapons = ["Candlestick", "Knife", "Lead Pipe", "Pistol", "Rope", "Wrench"];
	var places = ["Hall", "Lounge", "Dining Room", "Kitchen", "Ballroom", "Conservatory", "Billiard Room", "Library", "Study"];
	for (i = 0; i < suspects.length; i++) {
		gui.addComponent(new RadioButton(10, 80 + 30*i, 30, 30, 2));
		var aLabel = new Label(50, 85 + 30*i, suspects[i])
		aLabel.setFontSize(20);
		gui.addComponent(aLabel);
	}
	
	for (i = 0; i < weapons.length; i++) {
		gui.addComponent(new RadioButton(10, 310 + 30*i, 30, 30, 2));
		var aLabel = new Label(50, 315 + 30*i, weapons[i])
		aLabel.setFontSize(20);
		gui.addComponent(aLabel);
	}
	
	for (i = 0; i < places.length; i++) {
		gui.addComponent(new RadioButton(10, 540 + 30*i, 30, 30, 2));
		var aLabel = new Label(50, 545 + 30*i, places[i])
		aLabel.setFontSize(20);
		gui.addComponent(aLabel);
	}
	
	var btn = new Button(290, 400, 90, 90, "^");
	btn.label = "up arrow";
	gui.addComponent(btn);
	var btn = new Button(380, 490, 90, 90, ">");
	btn.label = "right arrow";
	gui.addComponent(btn);
	var btn = new Button(200, 490, 90, 90, "<");
	btn.label = "left arrow";
	gui.addComponent(btn);
	var btn = new Button(290, 580, 90, 90, "v");
	btn.label = "down arrow";
	gui.addComponent(btn);
	
	render();
}

function render() {
	var w = window.innerWidth;
	var h = window.innerHeight;
	var gameview = document.getElementById("gameview");
	var wAR = w / dimX;
	var hAR = h / dimY;

	if (Math.abs((wAR - hAR)/((wAR + hAR) / 2)) > 0.22) {
		if (wAR < hAR) {
			gameview.width = w;
			gameview.height = w * dimY/dimX;
			gameview.style.left = "0px";
			gameview.style.top = (h/2 - gameview.height / 2) + "px";
		} else {
			gameview.width = h * dimX/dimY;
			gameview.height = h;
			gameview.style.left = (w/2 - gameview.width / 2) + "px";
			gameview.style.top = "0px";
		}
	} else {
		gameview.width = w;
		gameview.height = h;
		gameview.style.left = "0px";
		gameview.style.top = "0px";
	}
	
	//console.log(gameview.width + " " + gameview.height + " " + gameview.style.left + " " + gameview.style.top + " " + gameview.width/gameview.height)
	var ctx = gameview.getContext("2d");
	ctx.scale(gameview.width/dimX, gameview.height/dimY);
	gui.renderComponent(ctx);
	
	connStatus.render(ctx);
}

window.setInterval(render, 1000);
document.getElementById("gameview").addEventListener("click", clickEvent);

var btn = new Button(5, 5, 150, 50, "Start Game");

btn.clickListener = function(event) {
	loadGame();
};
gui.addComponent(btn);


socketListener = function(text) {
	console.log(text);
}
openSocket();

render();
