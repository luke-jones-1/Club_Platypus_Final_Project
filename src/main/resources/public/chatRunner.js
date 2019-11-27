//console.log(location.hostname);
//console.log(location.port);

// creates a web socket on page load
var webSocket = new WebSocket("wss://" + location.hostname + ":" + location.port + "/chat");

// to update the chat when new message created
webSocket.onmessage = function (msg) { updateChat(msg); };

// creates a pop-up when websocket times-out/closes
webSocket.onclose = function () { alert("WebSocket connection closed")};


// Jquery
// waits for user to click send then calls sendMessage function with arg message value
id("send").addEventListener("click", function () {
    sendMessage(id("message").value);
});

//Jquery
// waits for user to press enter then calls sendMessage function with arg message value
id("message").addEventListener("keypress", function(e) {
    if (e.keyCode === 13) { sendMessage(e.target.value); }
})


//calls websocket.send which is what starts the process of sending messages to other users
function sendMessage(message) {
    if(message !== ""){
        webSocket.send(message);
        id("message").value = "";
    }
}

function updateChat(msg) {
    var data = JSON.parse(msg.data); //Accepts incoming message info
    insert("chat", data.userMessage);
    id("userlist").innerHTML = "";
    data.userlist.forEach(function (user) {
    insert("userlist", "<li>" + user + "</li>")
    });
}

function insert(targetId, message) {
    id(targetId).insertAdjacentHTML("afterbegin", message);
}

// document.get... is Jquery
// finds html element with id of argument given
function id(id){ return document.getElementById(id);}
