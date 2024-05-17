var roomId = getRoomIdFromUrl();


// localhost8080:chat/{chatRoomId}의 url에서 chatRoomId를 추출해내기
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
        var roomId = getRoomIdFromUrl();
        stompClient.subscribe('/room/' + roomId, function (messageOutput) {
            var message = JSON.parse(messageOutput.body);
            showMessageOutput(message, currentUserId);
        });
    });

    window.sendMessage = function () {
        var messageContent = document.getElementById("message").value;
        if (messageContent && stompClient) {
            var chatMessage = {
                sender: currentUserId,
                message: messageContent
            };
            stompClient.send("/send/" + roomId, {}, JSON.stringify(chatMessage));
            document.getElementById("message").value = '';
        }
        return false;
    }

    function showMessageOutput(message, currentUserId) {
        var messageList = document.getElementById('messageList');
        var messageElement = document.createElement('div');
        messageElement.className = 'message-row';
        messageElement.classList.add(message.sender === currentUserId ? 'my-message' : 'their-message');

        if (message.sender === currentUserId) {
            // 내 메시지일 때
            var textNode = document.createElement('div');
            textNode.textContent = message.message;
            textNode.className = 'message-content';
            messageElement.appendChild(textNode); // 텍스트만 추가

        } else {
            // 다른 사람의 메시지일 때
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

                messageInfo.appendChild(nicknameSpan);
                messageInfo.appendChild(textNode);

                messageElement.appendChild(imgElement); // 이미지 추가
                messageElement.appendChild(messageInfo);

            }).catch(error => {
                console.error("Error loading user info:", error);
            });
        }

        messageList.appendChild(messageElement);
        messageList.scrollTop = messageList.scrollHeight;
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
                    // userImageRequest 함수 호출 및 결과와 함께 객체 반환
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

});

