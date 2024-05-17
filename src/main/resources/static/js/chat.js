document.addEventListener('DOMContentLoaded', function () {
    function getRoomIdFromUrl() {
        const path = window.location.pathname;
        // URL /chat/{roomId}에서 roomId를 추출
        const match = path.match(/\/chat\/(\d+)/);
        return match ? match[1] : null;
    }

    var roomId = getRoomIdFromUrl(); // roomId 추출
    var stompClient = null;

    if (!roomId) {
        console.error("No room ID found in URL");
        return; // roomId가 없으면 함수 종료
    }

    function connect() {
        if (!roomId) {
            console.error("Room ID is required");
            return;
        }
        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function(frame) {
            console.log('Connected: ' + frame);
            subscribeToChat();
        }, function(error) {
            console.error('STOMP error:', error);
        });
    }

    function subscribeToChat() {
        stompClient.subscribe('/room/' + roomId, function (messageOutput) {
            showMessage(JSON.parse(messageOutput.body));
        });
    }

    function disconnect() {
        if (stompClient !== null) {
            stompClient.disconnect();
            console.log("Disconnected");
        }
    }

    function sendMessage(event) {
        event.preventDefault();
        var messageContent = document.getElementById('message').value;
        if (messageContent && stompClient) {
            var chatMessage = {
                sender: 'userId',  // 예시용; 실제 구현에서는 동적으로 변경 필요
                content: messageContent,
                type: 'TALK'
            };
            stompClient.send("/send/" + roomId, {}, JSON.stringify(chatMessage));
            document.getElementById('message').value = '';
        }
    }

    function showMessage(message) {
        var messageList = document.getElementById('messageList');
        var messageElement = document.createElement('div');
        messageElement.textContent = message.sender + ": " + message.content;
        messageList.appendChild(messageElement);
        messageList.scrollTop = messageList.scrollHeight;  // Scroll to bottom
    }

    connect();  // 페이지 로드 시 WebSocket 연결을 시도

    document.querySelector('form').addEventListener('submit', sendMessage);
});
