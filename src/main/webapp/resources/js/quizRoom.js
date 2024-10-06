let ws = new WebSocket("ws://localhost:8083//WebApp_war/quizRoomSocket");

ws.onopen = function(event) {
    console.log("Connected to WebSocket Server");
};


document.querySelector("#test_form").onsubmit = function(event) {
    event.preventDefault();  // Prevent form from submitting the traditional way
    let message = document.querySelector("#text").value;


    if (message) {
        ws.send(message);
        document.querySelector("#text").value = "";
    }
};

ws.onerror = function(event){
    console.log(event)
}


ws.onmessage = function(event) {
    let messageElement = document.createElement("h3");
    messageElement.textContent = event.data;
    document.querySelector("#test_window").appendChild(messageElement);
};

ws.onclose = function(event) {
    console.log("Connection closed");
};
