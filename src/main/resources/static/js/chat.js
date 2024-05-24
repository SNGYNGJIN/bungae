var roomId = getRoomIdFromUrl(); // roomId를 추출
var eventSource;

function getRoomIdFromUrl() {
    const path = window.location.pathname;
    const match = path.match(/\/chat\/(\d+)/);
    return match ? match[1] : null;
}

$(function () {
    var socket = new SockJS('/ws');
    var stompClient = Stomp.over(socket);
    var currentUserId = sessionStorage.getItem('loggedInUserId'); // 현재 사용자 ID 가져오기

    memberList();

    stompClient.connect({}, function (frame) {
        stompClient.send(`/send/enter/${roomId}/${currentUserId}`, {}, JSON.stringify(currentUserId));

        // 채팅방 구독하기
        stompClient.subscribe('/room/' + roomId, function (messageOutput) {
            var message = JSON.parse(messageOutput.body);
            if (message.type === 'leave') {
                alert("User left: " + message.message);
            } else if (message && !(message.type === 'enter')) {
                showMessageOutput(message, currentUserId);
            }
        });

        window.addEventListener("beforeunload", function(event) {
            console.log("beforeunload event triggered");
            handleUnload();
            event.returnValue = ''; // This will show a confirmation dialog in some browsers
        });

        window.addEventListener("unload", function(event) {
            console.log("unload event triggered");
            handleUnload();
        });

        // 페이지 로드 시 채팅 기록 불러오기
        fetch(`/chat/api/messages/${roomId}`)
            .then(response => response.json())
            .then(data => {
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

    // 메시지 전송 버튼 클릭 시 기본 동작 방지
    document.getElementById("sendButton").addEventListener("click", function(event) {
        event.preventDefault();
        sendMessage();
    });

    function handleUnload() {
        stompClient.send(`/send/leave/${roomId}/${currentUserId}`, {}, JSON.stringify(currentUserId));
        $.ajax({
            url: "/alarm/unsubscribe/" + currentUserId,
            method: 'DELETE',
            async: false, // 비동기 방식을 사용하지 않도록 설정
            success: function () {
                console.log("사용자가 채팅방을 떠났습니다. SSE 구독이 끊어졌습니다.");
            },
            error: function (error) {
                console.error("Error : ", error);
            }
        });
        if (eventSource) {
            eventSource.close();
        }
    }

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
            memberList();
        } else {
            if (message.sender === currentUserId) {
                messageElement.classList.add('my-message');
                messageElement.appendChild(timeSpan);
                messageElement.appendChild(textNode);
            } else {
                var userDetailsModal = document.getElementById("userDetailsModal");
                var ModalImage = document.getElementById("mprofile-image");
                var ModalNickname = document.getElementById("muserNickname");
                var ModalAgeGender = document.getElementById("mAge-Gender");
                var ModalInfo = document.getElementById("muserInfo");
                var ModalReview = document.getElementById("muserReviewScore");

                var closeModal = document.getElementsByClassName("close")[0];

                closeModal.onclick = function() {
                    userDetailsModal.style.display = "none";
                }

                window.onclick = function(event) {
                    if (event.target == userDetailsModal) {
                        userDetailsModal.style.display = "none";
                    }
                }

                messageElement.classList.add('their-message');
                nicknameRequest(message.sender).then(userInfo => {
                    var imgElement = document.createElement('img');
                    imgElement.src = userInfo.userImage;
                    imgElement.className = 'user-image';
                    imgElement.addEventListener('click', function() {
                        ModalImage.src = userInfo.userImage;
                        ModalNickname.textContent = userInfo.nickname;
                        ModalAgeGender.textContent = `${userInfo.userAge}세 / ${userInfo.usergender === 'FEMALE' ? '여' : '남'}`;
                        ModalReview.textContent = `받은 후기들의 평점은 ${userInfo.userRating}점입니다.`;
                        ModalInfo.textContent = userInfo.Info;
                        userDetailsModal.style.display = "block";
                    });

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

                    messageList.scrollTop = messageList.scrollHeight;
                }).catch(error => {
                    console.error("Error loading user info:", error);
                });
            }
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
                    return {
                        nickname: data.result.nickname,
                        userRating: data.result.profile.userRating,
                        usergender: data.result.profile.gender,
                        userAge: data.result.profile.userAge,
                        userImage: data.result.profile.userImage,
                        Info: data.result.profile.userInfo
                    };
                } else {
                    displayError(`사용자 정보 가져오기 실패: ${data.message}`);
                    throw new Error(`사용자 정보 가져오기 실패: ${data.message}`);
                }
            })
            .catch(error => {
                displayError(`사용자 정보 가져오기 중 오류 발생: ${error}`);
                throw error;
            });
    }

    function displayError(message) {
        console.error(message);
        const errorContainer = document.getElementById('errorMessage');
        errorContainer.textContent = message;
    }

    function formatTime(isoString) {
        const date = new Date(isoString);
        return date.toLocaleTimeString('ko-KR', { hour: '2-digit', minute: '2-digit', hour12: false });
    }

    function memberList() {
        return fetch(`/chat/api/chatMember/${roomId}`, {
            method: 'GET',
            headers: {
                'Accept': 'application/json'
            }
        })
            .then(response => response.json())
            .then(data => {
                updateMemberList(data);
            })
            .catch(error => {
                console.error(`사용자 정보 가져오기 중 오류 발생: ${error}`);
            });
    }

    function updateMemberList(members) {
        const organizerElement = document.getElementById('organizer');
        const memberElement = document.getElementById('member');

        organizerElement.innerHTML = '';
        memberElement.innerHTML = '👥';

        members.forEach(member => {
            const userInfo = `
                <div class="user-info">
                    <img src="${member.userImage}" alt="User Image" style="width: 30px; height: 30px;">
                    <p>${member.nickname}</p>
                </div>
            `;

            if (member.organizer) {
                organizerElement.innerHTML += " 👑 " + userInfo;
            } else {
                memberElement.innerHTML += userInfo;
            }
        });
    }
});
