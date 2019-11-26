console.log(location.hostname);
console.log(location.port);
var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/chat");
webSocket.onmessage = function (msg) { updateChat(msg); };
webSocket.onclose = function () { alert("WebSocket connection closed")};

id("send").addEventListener("click", function () {
    sendMessage(id("message").value);
});

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

function id(id){ return document.getElementById(id);}
