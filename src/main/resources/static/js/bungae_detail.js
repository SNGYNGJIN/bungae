document.addEventListener('DOMContentLoaded', function() {
    var roomId = getRoomIdFromUrl(); // roomId를 추출
    var goChatButton = document.getElementById('go_chat');
    var user = sessionStorage.getItem("loggedInUserId");

    // "참가" 버튼 클릭 이벤트 리스너 추가
    goChatButton.addEventListener('click', function() {
        join(roomId, user);
        window.location.href = '/chat/' + roomId;
    });
});

// url에서 bungae_id(chatRoomId) 찾기
function getRoomIdFromUrl() {
    const path = window.location.pathname;
    const match = path.match(/\/bungae\/bungae_detail\/(\d+)/);

    return match ? match[1] : null;
}

// 번개 아이디에 해당하는 번개멤버에 유저 데이터 삽입
async function join(chatRoomId, userId) {
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