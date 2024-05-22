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
    var organizer = document.getElementById('organizer');
    var member = document.getElementById('member');


    memberList();

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
                // 사용자 상세 모달 가져오기
                var userDetailsModal = document.getElementById("userDetailsModal");
                var ModalImage = document.getElementById("mprofile-image")
                var ModalNickname = document.getElementById("muserNickname");
                var ModalAgeGender = document.getElementById("mAge-Gender");
                var ModalInfo = document.getElementById("muserInfo")
                var ModalReview = document.getElementById("muserReviewScore");

                var closeModal = document.getElementsByClassName("close")[0];

                // 모달 닫기 이벤트
                closeModal.onclick = function() {
                    userDetailsModal.style.display = "none";
                }

                window.onclick = function(event) {
                    if (event.target == userDetailsModal) {
                        userDetailsModal.style.display = "none";
                    }
                }

                // 다른 사람의 메시지일 때
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
                        return {
                            nickname: data.result.nickname,
                            userRating : data.result.profile.userRating,
                            usergender : data.result.profile.gender,
                            userAge : data.result.profile.userAge,
                            userImage: data.result.profile.userImage,
                            Info : data.result.profile.userInfo
                        };
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


    function displayError(message) {
        console.error(message); // 콘솔에 오류 로그 출력
        const errorContainer = document.getElementById('errorMessage');
        errorContainer.textContent = message; // 사용자 인터페이스에 오류 메시지 표시
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
                // 여기에서 데이터 구조를 확인하고 함수를 호출합니다.
                updateMemberList(data);  // 직접 받은 데이터로 목록을 업데이트 (예: data.results, data 등 실제 구조에 맞춰야 함)
            })
            .catch(error => {
                console.error(`사용자 정보 가져오기 중 오류 발생: ${error}`);
            });
    }

    function updateMemberList(members) {
        const organizerElement = document.getElementById('organizer');
        const memberElement = document.getElementById('member');

        // 초기화
        organizerElement.innerHTML = '';
        memberElement.innerHTML = '';

        members.forEach(member => {
            const userInfo = `
            <div>
                <img src="${member.userImage}" alt="User Image" style="width: 30px; height: 30px;">
                <p>${member.nickname}</p>
            </div>
        `;

            if (member.organizer) {
                organizerElement.innerHTML += "주최자 : " + userInfo;
            } else {
                memberElement.innerHTML += "참여자 : " + userInfo;
            }
        });
    }
});