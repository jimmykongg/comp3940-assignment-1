async function getJWTToken() {
    try {
        const response = await axios.post('/api/check-auth')
        return response.data
    } catch (error) {
        console.error('getJWTTokenError: ', error);
    }
}
let ws;
async function setUpWekSocket() {
    const token = JSON.stringify(await getJWTToken())
    const encodedToken = encodeURIComponent(token)
    const params = new URLSearchParams(window.location.search)
    const categoryID = params.get('categoryID');
    const roomID = params.get('roomID');
    ws = new WebSocket(`ws://localhost:8083/quizRoomSocket?token=${encodedToken}
    &categoryID=${categoryID}&roomID=${roomID}`);

    ws.onopen = function(event) {
        console.log("Connected to WebSocket Server");
    };

    ws.onerror = function(event){
        console.log(event)
    }

    ws.onmessage = function(event) {
        const data = JSON.parse(event.data);

        loadContents(data)
    };

    ws.onclose = function(event) {
        console.log("Connection closed");
    };
}
setUpWekSocket()

function loadContents(data) {
    const {type} = data;

    switch (type) {
        case "joinRoom":
            loadChatRoom(data.joinMessage)
            break;
        case "question":
            loadMedia(data.mediaType, data.filePath)
            loadQuestion(data.description)
            break;
        case "answers":
            loadAnswers(data.answers, data.username, data.role)
            break;
        case "checkAnswer":
            loadCheckMessage(data.checkMessage, data.displayColor)
            break;
        case "redirect":
            window.location.href = data.redirectURL;
            break;
        default:
            console.error("Unknown message type", type)
    }
}

function loadMedia(mediaType, filePath) {
    let mediaHTML = '';

    if (mediaType === 'image') {
        mediaHTML = `<img src="/images/${filePath}" alt="Quiz Image" style="max-width: 600px;">`;
    } else if (mediaType === 'video') {
        mediaHTML = `<iframe width="600" height="400" src="https://www.youtube.com/embed/${filePath}?autoplay=1&mute=1" allow="autoplay; encrypted-media"></iframe>`;
    } else if (mediaType === 'audio') {
        mediaHTML = `<iframe width="600" height="400" src="https://www.youtube.com/embed/${filePath}?autoplay=1&mute=1" allow="autoplay; encrypted-media"></iframe>`;
    }

    document.querySelector('.mediaWrapper').innerHTML = mediaHTML;
}

function loadQuestion(question) {
    document.querySelector('#question').innerHTML = `${question}`;

    document.querySelector('#nextQuestion').style.display = "inline"
    document.querySelector(".chatRoomWrapper").style.visibility = "visible";
}

function loadChatRoom(joinMessage) {
    const chatRoom = document.querySelector("#chatRoom")

    chatRoom.insertAdjacentHTML("beforeend", `<p>${joinMessage}</p>`)
}

function getNextQuestion() {
    const message = {type: "getNextQuestion"}

    ws.send(JSON.stringify(message));
}

function handleGetNextQuestion() {
    const button = document.querySelector("#nextQuestion")

    button.addEventListener('click', getNextQuestion)
}
handleGetNextQuestion()

function loadAnswers(answers, username, role) {
    const answerWrapper = document.querySelector(".answersWrapper")
    answerWrapper.innerHTML = ""

    answers.forEach((answer) => {
        answerWrapper.insertAdjacentHTML("beforeend", `
            <div>
                <button class="answerBtn" value=${answer.id}>${answer.description}</button>
            </div>
    `)
    })

    handleSubmitAnswer(username, role)
}

function handleSubmitAnswer(username, role) {
    document.querySelectorAll(".answerBtn").forEach((btn) => {
        btn.addEventListener("click", () => {
            const answer = {type: "submitAnswer", answerID: btn.value, username: username, role: role}

            ws.send(JSON.stringify(answer))
        })
    })
}

function loadCheckMessage(message, color) {
    document.querySelector("#chatRoom").insertAdjacentHTML("beforeend", `
        <p style="color: ${color};">${message}</p>
    `)
}