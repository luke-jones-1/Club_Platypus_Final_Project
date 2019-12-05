//console.log(location.hostname);
//console.log(location.port);
// creates a web socket on page load
 var webSocket = new WebSocket("wss://" + location.hostname + ":" + location.port + "/chat");
// to update the chat when new message created
webSocket.onmessage = function (msg) { updateChat(msg); };


var room = true;

webSocket.onopen = function(msg){
    switchChat();
}
// creates a pop-up when websocket times-out/closes
webSocket.onclose = function (msg) {
    alert("WebSocket connection closed");
    };

// Jquery
// waits for user to click send then calls sendMessage function with arg message value
id("send").addEventListener("click", function () {
    sendMessage(id("message").value);
});

id("checkBoxState").addEventListener("click", function (){
  room = !room;
  switchChat();
})
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
    if(room){
    insertBottom("chatLogRoom1", data.userMessage); // calls insert function on parsed data passed though websocket from paddlechat
    } else {
    insertBottom("chatLogRoom2", data.userMessage);
    }
    id("userlist").innerHTML = "";
    insertTop("userlist", data.userlist)
}

// inserts into top of element
function insertTop(targetId, message) {
    id(targetId).insertAdjacentHTML("afterbegin", message); // inserts given html into element with given tag
}

// inserts into bottom of element
function insertBottom(targetId, message) {
    id(targetId).insertAdjacentHTML("beforeend", message); // inserts given html into element with given tag
}

// document.get... is Jquery
// finds html element with id of argument given
function id(id){ return document.getElementById(id);}

function switchChat(){
var element = document.getElementById('navIdentifier');
    var content;

    if  (room) {
        content = '<div class="jumbotron" id="chatLogRoom1"><p><Chat1/p> </div>';
    }
    if  (!room) {
        content = '<div class="jumbotron" id="chatLogRoom2"><p><Chat2/p> </div>';
    }

    element.innerHTML = content;
}