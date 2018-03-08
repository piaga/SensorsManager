var stompClient = null;
var update=null;
var subscription=null;
var sensorsStatus=null;
var localUrl="http://localhost:8080/greetings";
var remoteUrl="http://piagatech.ddns.net:8090/greetings";

var lastMessage=null;
var lastTime=Date.now();
var lastSecTime=Date.now();
var fotograms=0;
var fps=0;

var ws=null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
        $("#update").html("<h1>UPDATES</h1>");
        $("#status").html("<h1>STATUS</h1>");
        $("#greeting").html("<h1>DETTAGLI</h1>");
    }
    else {
        $("#conversation").hide();
    }
    
    
    
}

function connectStomp() {
	var connectionUrl=$("#localCB").is(":checked")?localUrl:remoteUrl;
	console.log("Connecting to "+connectionUrl);
	
    var socket = new SockJS(connectionUrl);
    stompClient = Stomp.over(socket);
    stompClient.debug=null;
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        
        subscription=stompClient.subscribe('/sensors/details',function(greeting){
        	
        	showSetup(JSON.parse(JSON.stringify(greeting.body)));
        	if (sensorsStatus==null)
	        	sensorsStatus=stompClient.subscribe('/sensors/status',function(greeting){
	            	//console.error(JSON.parse(JSON.stringify(greeting)));
	            	
	            	showStatus(JSON.parse(JSON.stringify(greeting.body)));
	            });
            if (update==null)
            	update=stompClient.subscribe('/sensors/update', function (greeting) {
	            	
	            	showUpdate(JSON.parse(greeting.body));
	                //showGreeting(JSON.parse(greeting).content);
	            });
        	
        });
        
        
        
        
    });
}

function connectWS()
{
	ws=new WebSocket("ws://piagatech.ddns.net:8090/name");
	ws.onmessage=function(data)
	{
	
		parseMessage(data.data);
		
	}
	setConnected(true);
}

function parseMessage(data)
{
	
	if (!(data instanceof Blob))
	{
		var obj=JSON.parse(data);
		
	
	
		if (obj.generic=="Double"||obj.generic=="Vector3")
		{
			showUpdate(data)
		}
	}
	else
	{
		
		
		$("#camera").attr("src",window.URL.createObjectURL(data));
		
	}
}

function disconnectWS()
{
	if (ws!=null)
		{
			ws.close();
		}
	setConnected(false);
	console.log("Disconnected");
}

function disconnectStomp() {
    if (stompClient !== null) {
    	update.unsubscribe();
    	update=null;
    	
    	sensorsStatus.unsubscribe();
    	sensorsStatus=null;
    	subscription.unsubscribe();
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function lastMessageTime()
{
	if (lastTime!=null)
	{
		var delay=Date.now()-lastTime;
		$("#lastMessage").html("<p>ping:"+delay+" </p><p>fps: "+fps+"</p>");
		lastTime=Date.now();
		fotograms++;
		if (lastTime-lastSecTime>1000)
		{
			lastSecTime=lastTime;
			
			fps=fotograms;
			fotograms=0;
			
			
		}
		
		
	}
	else
	{
		lastTime=Date.now();
	}
}

function sendName() {
    stompClient.send("/app/message", {}, JSON.stringify({'name': $("#name").val()}));
}

function showSetup(message) {
	//console.log("showGreeting: "+message);
	
    $("#greeting").append("<p>" + message + "</p>");
}

function showUpdate(message) {

	
	if (message.sensor=="CAMERA")
		{
			lastMessageTime();
			var value=message.value;
			$("#camera").attr("src","data:image/jpeg;base64,"+value);
			
		}
	else
	{
		if (message.sensor=="LIGHT"||message.sensor=="PRESSURE"||message.sensor=="MAGNETIC")
			$("#update").append("<p>"+ message.sensor+" : "+ message.value +"</p>");
		else
			if (message.sensor=="ACCELEROMETER")
				$("#update").append("<p>"+ message.sensor+" : "+ message.value.x+"|"+message.value.y+"|"+message.value.z+"</p>");
		
	}
}

function showStatus(message) {
	//console.log("showGreeting: "+message);
    $("#status").append("<p>" + message + "</p>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { 
    	connectStomp();
    	//connectWS();
});
    $( "#disconnect" ).click(function() {
    	disconnectStomp();
    	//disconnectWS();
});
    $( "#send" ).click(function() { sendName(); });
});
