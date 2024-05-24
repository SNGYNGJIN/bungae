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

            eventSource.onmessage = function (event) {
                alert("Message received: " + event.data);
                const div = document.createElement("div");
                div.textContent = `Event received: ${event.data}`;
                document.getElementById("messages").appendChild(div);
                console.log(event.data);
            };

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
                async: false, // 비동기 방식을 사용하지 않도록 설정
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
        setTimeout(checkAndSubscribe, 2000); // 2초 지연
    });

    // 채팅 페이지 벗어날 때 구독 설정
    window.addEventListener('leaveChat', function (event) {
        console.log("leaveChat event received");
        checkAndSubscribe();
    });
});
