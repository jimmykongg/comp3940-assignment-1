async function getJWTToken() {
    try {
        const response = await axios.post('/api/check-auth')
        return response.data
    } catch (error) {
        console.error('getJWTTokenError: ', error);
    }
}

async function setUpWekSocket() {
    const token = JSON.stringify(await getJWTToken())
    let encodedToken = encodeURIComponent(token)

    let ws = new WebSocket("ws://localhost:8083//WebApp_war/quizRoomSocket?token=" + encodedToken);

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
}
setUpWekSocket()