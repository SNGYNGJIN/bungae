document.addEventListener('DOMContentLoaded', function () {
    // Offline.js 옵션 설정
    Offline.options = {checks: {xhr: {url: 'http://localhost:8080'}}};

    // 온라인/오프라인 상태 체크
    Offline.check();

    console.log("Offline state:", Offline.state);

    // 상태 변화 이벤트 리스너
    Offline.on('up', function () {
        console.log("We are online");
        sendOfflineState("up");
    });

    Offline.on('down', function () {
        console.log("We are offline");
        sendOfflineState("down");
    });

    // 서버로 상태 전송
    function sendOfflineState(state) {
        const userId = sessionStorage.getItem("loggedInUserId");
        $.ajax({
            type: 'POST',
            url: `/user/api/offline/${userId}`,
            contentType: 'application/json',
            data: JSON.stringify({ state: state }),
            success: function (response) {
                console.log("State sent successfully:", response);
            },
            error: function (error) {
                console.error("Error sending state:", error);
            }
        });
    }

    sendOfflineState(Offline.state);

    // 4분마다 요청해서 사용자가 창을 껐는지 확인
    setInterval(function() {
        sendOfflineState(Offline.state);
    }, 3 * 60 * 1000); // 3분마다 요청
});