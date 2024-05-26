document.addEventListener('DOMContentLoaded', function () {
    const loggedInUserId = sessionStorage.getItem("loggedInUserId");
    let eventSource;

    if (!loggedInUserId) {
        console.error("No logged in user ID found in sessionStorage");
        return;
    }

    // 초기화 시 구독 확인
    checkAndSubscribe();

    function checkAndSubscribe() {
        console.log("checkAndSubscribe called");
        setTimeout(function() {
            $.ajax({
                url: `/chat/api/findDisconnect/${loggedInUserId}`,
                method: 'GET',
                success: function (response) {
                    console.log("checkAndSubscribe response:", response);
                    if (response === true) {
                        subscribeToSSE();
                    } else if (response === false) {
                        if (eventSource && eventSource.readyState !== EventSource.CLOSED) {
                            eventSource.close();
                            sessionStorage.removeItem('sseSubscribed');
                            console.log("SSE 구독이 끊어졌습니다.");
                        }
                    }
                },
                error: function (error) {
                    console.error("Error : ", error);
                }
            });
        }, 2000); // 2초 지연
    }

    function subscribeToSSE() {
        console.log("subscribeToSSE called");
        if (!eventSource || eventSource.readyState === EventSource.CLOSED) {
            console.log("Subscribing to SSE for user:", loggedInUserId);
            eventSource = new EventSource("/alarm/subscribe/" + loggedInUserId);

            eventSource.addEventListener("alarm", function (event) {
                console.log("Alarm event received:", event);
                const data = JSON.parse(event.data);

                // 브라우저 알람 요청 허용시 알람 뜨게 하게
                (async () => {
                    const showNotification = () => {
                        const notification = new Notification('⚡️ 새로운 메세지 도착', {
                            body : data.senderId + " : " + data.message
                        });
                        setTimeout(() => {
                            notification.close();
                        }, 10 * 1000);
                        notification.addEventListener('click', () => {
                            unsubscribe(); // SSE 연결 끊기 !!!!
                            window.open("http://localhost:8080" + data.url, '_blank');
                        });
                    }

                    // 브라우저 알림 허용 권한
                    let granted = false;

                    if (Notification.permission === 'granted') {
                        granted = true;
                    } else if (Notification.permission !== 'denied') {
                        let permission = await Notification.requestPermission();
                        granted = permission === 'granted';
                    }
                    console.log(granted);
                    // 알림 보여주기
                    if (granted) {
                        showNotification();
                    }
                })();
            });

            eventSource.onerror = function (error) {
                console.error("Error occurred: ", error);
                console.log("EventSource error details:", error);

                setTimeout(function () {
                    subscribeToSSE();
                }, 3000); // 3초 후 재구독 시도
            };

            sessionStorage.setItem('sseSubscribed', 'true');
        }
    }

    function unsubscribe() {
        console.log("unsubscribe called");
        if (eventSource) {
            $.ajax({
                url: "/alarm/unsubscribe/" + loggedInUserId,
                method: 'DELETE',
                async: false,
                success: function () {
                    console.log("SSE 구독이 끊어졌습니다.");
                },
                error: function (error) {
                    console.error("Error : ", error);
                }
            });

            eventSource.close();
            sessionStorage.removeItem('sseSubscribed');
        }
    }

    // 페이지 나가기 전 구독 해제
    window.addEventListener('beforeunload', unsubscribe);

    // 채팅 페이지에서 구독 해제
    window.addEventListener('enterChat', function (event) {
        console.log("enterChat event received");
        unsubscribe();
        // 2초 후 checkAndSubscribe 호출
        setTimeout(checkAndSubscribe, 2000);
    });

    // 채팅 페이지 벗어날 때 구독 설정
    window.addEventListener('leaveChat', function (event) {
        console.log("leaveChat event received");
        setTimeout(checkAndSubscribe, 2000);
    });
});
