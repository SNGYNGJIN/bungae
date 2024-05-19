document.addEventListener('DOMContentLoaded', function() {
    var roomId = getRoomIdFromUrl(); // roomId를 추출
    var goChatButton = document.getElementById('go_chat'); // "참가" 버튼 가져오기
    var user = sessionStorage.getItem("loggedInUserId");

    // "참가" 버튼 클릭 이벤트 리스너 추가
    goChatButton.addEventListener('click', function() {
        join(roomId, user);
        window.location.href = '/chat/' + roomId; // 리다이렉트 이동
    });
});

function getRoomIdFromUrl() {
    const path = window.location.pathname;
    const match = path.match(/\/bungae\/bungae_detail\/(\d+)/);

    return match ? match[1] : null;
}

function join(chatRoomId, userId) {
    const url = `/chat/api/join/${chatRoomId}?userId=${userId}`; // URL 조립

    try {
        const response =fetch(url); // fetch 요청
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`); // 응답 오류 처리
        }
        const data = response.text(); // 응답 텍스트를 가져옴

        console.log(`API response: ${data}`); // 콘솔에 출력
    } catch (error) {
        console.error('Failed to fetch data:', error); // 에러 콘솔에 출력
    }
}