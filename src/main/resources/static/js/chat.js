var roomId = getRoomIdFromUrl(); // roomId를 추출

function getRoomIdFromUrl() {
    const path = window.location.pathname;
    const match = path.match(/\/chat\/(\d+)/);
    return match ? match[1] : null;
}

$(function () {
    var socket = new SockJS('/ws');
    var stompClient = Stomp.over(socket);
    var currentUserId = sessionStorage.getItem('loggedInUserId'); // 현재 사용자 ID 가져오기

    stompClient.connect({}, function (frame) {
        // 구독하기
        stompClient.subscribe('/room/' + roomId, function (messageOutput) {
            var message = JSON.parse(messageOutput.body);
            if (message) {showMessageOutput(message, currentUserId);}
        });

        // 페이지 로드 시 채팅 기록 불러오기
        fetch(`/chat/api/messages/${roomId}`)
            .then(response => response.json())
            .then(data => {
                // 서버로부터 받은 데이터가 배열인지 확인
                if (Array.isArray(data)) {
                    data.forEach(message => {
                        showMessageOutput(message, currentUserId);
                    });
                } else {
                    console.error('Received data is not an array:', data);
                }
            })
            .catch(error => console.error('Error loading chat messages:', error));


    });

    window.sendMessage = function () {
        var messageContent = document.getElementById("message").value;
        if (messageContent && stompClient) {
            var chatMessage = {
                chatRoomId: roomId,
                sender: currentUserId,
                message: messageContent,
                type: "TALK"
            };
            try {
                var jsonString = JSON.stringify(chatMessage);
                stompClient.send("/send/" + roomId, {}, jsonString);
            } catch (e) {
                console.error("JSON stringify error:", e);
            }
            //stompClient.send("/send/" + roomId, {}, JSON.stringify(chatMessage));
            document.getElementById("message").value = '';
        }
        return false;
    }

    function showMessageOutput(message, currentUserId) {
        var messageList = document.getElementById('messageList');
        var messageElement = document.createElement('div');
        messageElement.className = 'message-row';

        var textNode = document.createElement('div');
        textNode.className = 'message-content';
        textNode.textContent = message.message;

        var timeSpan = document.createElement('span');
        timeSpan.className = 'message-time';
        var formattedTime = formatTime(message.sendTime);
        timeSpan.textContent = formattedTime;

        if (message.type !== "TALK") {
            messageElement.classList.add('announcement-message');
            messageElement.appendChild(textNode);
        } else {
            // 메시지 송신자가 현재 사용자인 경우
            if (message.sender === currentUserId) {
                messageElement.classList.add('my-message');
                messageElement.appendChild(timeSpan);
                messageElement.appendChild(textNode); // 텍스트만 추가
            } else {
                // 다른 사람의 메시지일 때
                messageElement.classList.add('their-message');
                nicknameRequest(message.sender).then(userInfo => {
                    var imgElement = document.createElement('img');
                    imgElement.src = userInfo.userImage;
                    imgElement.className = 'user-image';

                    var messageInfo = document.createElement('div');
                    messageInfo.className = 'message-info';

                    var nicknameSpan = document.createElement('span');
                    nicknameSpan.textContent = userInfo.nickname;
                    nicknameSpan.className = 'nickname';

                    var textNode = document.createElement('div');
                    textNode.textContent = message.message;
                    textNode.className = 'message-content';

                    messageElement.classList.add('their-message');
                    messageInfo.appendChild(nicknameSpan);
                    messageInfo.appendChild(textNode);
                    messageInfo.appendChild(timeSpan);

                    messageElement.appendChild(imgElement);
                    messageElement.appendChild(messageInfo);

                }).catch(error => {
                    console.error("Error loading user info:", error);
                });
            }
        }
        messageList.appendChild(messageElement); // 메시지 목록에 메시지 요소 추가
        messageList.scrollTop = messageList.scrollHeight; // 스크롤을 최하단으로
    }


    function nicknameRequest(userId) {
        return fetch(`/user/api/info/${userId}`, {
            method: 'GET',
            headers: {
                'Accept': 'application/json'
            }
        })
            .then(response => response.json())
            .then(data => {
                if (data.code === 200) {
                    return userImageRequest(data.result.id).then(userImage => {
                        return {
                            nickname: data.result.nickname,
                            userImage: userImage
                        };
                    });
                } else {
                    displayError(`사용자 정보 가져오기 실패: ${data.message}`);
                    throw new Error(`사용자 정보 가져오기 실패: ${data.message}`);
                }
            })
            .catch(error => {
                displayError(`사용자 정보 가져오기 중 오류 발생: ${error}`);
                throw error; // 에러를 다시 throw 하여 체인을 중단
            });
    }




// 프로필 사진 가져오기
    function userImageRequest(id) {
        return fetch(`/user/api/info/profile/${id}`, {  // 여기에 return 추가
            method: 'GET',
            headers: {
                'Accept': 'application/json'
            }
        })
            .then(response => response.json())
            .then(data => {
                if (data.code === 200) {
                    return data.result.userImage;
                } else {
                    displayError(`프로필 정보 가져오기 실패: ${data.message}`);
                    throw new Error(`프로필 정보 가져오기 실패: ${data.message}`);  // 에러를 throw 하여 Promise 체인이 거부됨
                }
            })
            .catch(error => {
                displayError(`프로필 정보 가져오기 중 오류 발생: ${error.message}`);
                throw error;  // 에러를 다시 throw 하여 체인을 중단
            });
    }


    function displayError(message) {
        console.error(message); // 콘솔에 오류 로그 출력
        const errorContainer = document.getElementById('errorMessage');
        errorContainer.textContent = message; // 사용자 인터페이스에 오류 메시지 표시
    }

    function formatTime(isoString) {
        const date = new Date(isoString);
        return date.toLocaleTimeString('ko-KR', { hour: '2-digit', minute: '2-digit', hour12: false });
    }
});