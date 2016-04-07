function Packet() {
	this.command = "";
	this.parameters = [];
	
	this.addParameter = function(param, value) {
		this.parameters.push([param, value]);
	}
	
	this.getJson = function() {
		var json = "";
		json = json.concat("{\"Command\":\""+ this.command +"\",");
		json = json.concat("\"Parameters\":[");
		for (i = 0; i < this.parameters.length; i++) {
			json = json.concat("{\"" + this.parameters[i][0] + "\":\"" + this.parameters[i][1] + "\"}");
			if (i != this.parameters.length - 1) {
				json = json.concat(",");
			}
		}
		json = json.concat("]}");
		return json;
	}
}


/*

out packets
{
	command:"onClick"
	parameters:[
		{
			view:"ID name"
		}
	]
}
{
	command:"refresh",
	parameters:[
	]
}

in packets
{
	command:"changeUI",
	parameters:[
		{
			view:"ID name",
			structure:{
				//structure
			}
		}
	]
}
{
	command:"draw",
	parameters:[
		{
			//UI view structure
		}
	]
}


*/