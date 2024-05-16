var socket = new SockJS('/ws');
var stompClient = Stomp.over(socket);

stompClient.connect({}, function (frame) {
    console.log('Connected: ' + frame);

    // 서버로부터 메시지를 받기 위해 특정 주소를 구독합니다.
    stompClient.subscribe('/chat/newMessage', function (message) {
        var messageData = JSON.parse(message.body);
        var messageElement = document.createElement('div');
        messageElement.textContent = messageData.content;  // 예시로 메시지 내용만 표시
        document.getElementById('messageList').appendChild(messageElement);

        console.log(JSON.parse(message.body).content);
    });

    // 메시지 보내기 예시
    stompClient.send("/app/someFunction", {}, JSON.stringify({ 'message': 'Hello, world!' }));
});
