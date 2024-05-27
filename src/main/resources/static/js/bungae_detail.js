$(document).ready(function () {
    $('#joinButton').click(function (event) {
        event.preventDefault();
        let bungaeId = $(this).data('bungae-id');
        let user = sessionStorage.getItem("loggedInUserId");

        $.ajax({
            type: 'POST',
            url: '/' + bungaeId + '/join',
            success: function (response) {
                if (response === "새로운 참여자") {
                    alert('참가 완료!');
                    joinChat(bungaeId, user);
                }
                const enterEvent = new Event('enterChat');
                window.dispatchEvent(enterEvent);
                console.log("enterChat event dispatched");

                // 이벤트가 완전히 처리된 후 페이지 이동
                setTimeout(function() {
                    window.location.href = '/chat/' + bungaeId;
                }, 500); // 500ms 후 페이지 이동
            },
            error: function (xhr) {
                let errorMessage;
                if (xhr.status === 400) {
                    errorMessage = '잘못된 요청: ' + xhr.responseText;
                } else if (xhr.status === 404) {
                    errorMessage = '번개 모임 또는 유저를 찾을 수 없습니다: ' + xhr.responseText;
                } else if (xhr.status === 409) {
                    if (xhr.responseText.includes("참가 중인 모임이 있어 다른 모임에 참여할 수 없습니다.")) {
                        errorMessage = '참가 중인 모임이 있어 다른 모임에 참여할 수 없습니다.';
                    } else if (xhr.responseText.includes("연령대에 맞지 않는 번개 모임입니다.")) {
                        errorMessage = '연령대에 맞지 않는 번개 모임입니다.';
                    } else if (xhr.responseText.includes("수용 인원이 초과된 번개 모임입니다.")) {
                        errorMessage = '수용 인원이 초과된 번개 모임입니다.';
                    } else {
                        errorMessage = '해당 번개 모임은 참여 불가능한 상태입니다.';
                    }
                } else {
                    errorMessage = '참가 실패. 다시 시도해주세요.';
                }
                alert(errorMessage);
            }
        });
    });

    $('#updateButton').click(function () {
        let bungaeId = $(this).data('bungae-id');
        window.location.href = '/bungae/bungae_update/' + bungaeId;
    });
});

// 번개 아이디에 해당하는 번개멤버 채팅 참가 시키기
async function joinChat(chatRoomId, userId) {
    const url = `/chat/api/join/${chatRoomId}?userId=${userId}`;

    try {
        const response = await fetch(url); // `await`를 추가해 응답을 기다리기
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.text(); // `await`를 추가해서 텍스트 변환을기다리기
        console.log(`API response: ${data}`);
    } catch (error) {
        console.error('Failed to fetch data:', error);
    }
}