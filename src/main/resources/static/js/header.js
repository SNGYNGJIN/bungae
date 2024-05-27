document.addEventListener('DOMContentLoaded', function() {
    /*
        로그인/로그아웃 처리 스크립트
    */
    const loggedInUserId = sessionStorage.getItem("loggedInUserId");
    const loginLogoutButton = document.getElementById('loginLogoutButton');
    const notificationIcon = document.getElementById('notificationIcon');
    const notificationModal = document.getElementById('notificationModal');
    const notificationList = document.getElementById('notificationList');
    const pagination = document.getElementById('alarm_pagination');

    if (!loginLogoutButton) {
        console.error("loginLogoutButton element not found.");
        return;
    }
    console.log("Logged in user ID:", loggedInUserId);
    if (loggedInUserId) {
        loginLogoutButton.textContent = '로그아웃';
        loginLogoutButton.href = '#';
    } else {
        loginLogoutButton.textContent = '로그인';
        loginLogoutButton.href = 'login';
    }

    loginLogoutButton.addEventListener('click', function(event) {
        if (loggedInUserId) {
            event.preventDefault();
            sessionStorage.removeItem("loggedInUserId");
            window.location.href = '/login';
        }
    });

    if (!loggedInUserId) {
        console.error("No logged in user ID found in sessionStorage");
        return;
    }

    /*
        알림 메세지 스크립트
    */
    let notifications = [];
    let currentPage = 1;
    const itemsPerPage = 5;

    // 알림 아이콘 클릭 이벤트
    notificationIcon.addEventListener('click', function() {
        loadNotifications();
        notificationModal.style.display = 'block';
    });

    // 모달 창 닫기 이벤트
    document.querySelector('.alarm_close').addEventListener('click', function() {
        notificationModal.style.display = 'none';
        markNotificationsAsRead(); // 알림을 읽음 상태로 업데이트
    });

    // 알림을 읽음 상태로 표시
    function markNotificationsAsRead() {
        $.ajax({
            url: '/chat/api/checkLogoutMessage/' + loggedInUserId,
            method: 'POST',
            success: function(response) {
                console.log("Notifications marked as read:", response);
                // 로컬 데이터도 업데이트
                notifications.forEach(notification => {
                    notification.read = true;
                });
                displayNotifications();
            },
            error: function(error) {
                console.error("Error marking notifications as read:", error);
            }
        });
    }

    // 알림 목록 로드
    function loadNotifications() {
        $.ajax({
            url: '/chat/api/logoutChat/' + loggedInUserId,
            method: 'GET',
            success: function(data) {
                notifications = data.sort((a, b) => new Date(b.sendedTime) - new Date(a.sendedTime)).slice(0, 25);
                console.log("Data received:", data);
                displayNotifications();
                updateNotificationIcon(); // 알림 아이콘 업데이트
            },
            error: function(error) {
                console.error("Error loading notifications:", error);
            }
        });
    }

    // 알림 목록 표시
    function displayNotifications() {
        const start = (currentPage - 1) * itemsPerPage;
        const end = start + itemsPerPage;
        const paginatedNotifications = notifications.slice(start, end);

        notificationList.innerHTML = '';
        if (paginatedNotifications.length === 0 && notifications.length === 0) {
            notificationList.innerHTML = '<p>받은 알림이 없어요</p>';
        } else {
            paginatedNotifications.forEach(notification => {
                const color = notification.read ? 'lightgray' : 'black';
                notificationList.innerHTML += `<div style="color: ${color}">
                    <p>${notification.senderNickname} : ${notification.message}</p>
                    <p>[보낸 시각 : ${new Date(notification.sendedTime).toLocaleString()}]</p>
                    <!--<hr style="border-color: gray;">-->
                    <hr>
                </div>`;
            });
        }

        setupPagination();
    }

    // 페이징 처리 설정
    function setupPagination() {
        const pageCount = Math.ceil(notifications.length / itemsPerPage);
        pagination.innerHTML = '';

        for (let i = 1; i <= pageCount; i++) {
            const button = document.createElement('button');
            button.textContent = i;
            if (i === currentPage) {
                button.classList.add('active');
            }
            button.addEventListener('click', function() {
                currentPage = i;
                displayNotifications();
            });

            pagination.appendChild(button);
        }
    }

    // 알림 아이콘 업데이트
    function updateNotificationIcon() {
        const hasUnread = notifications.some(notification => !notification.read);
        notificationIcon.textContent = hasUnread ? '📬' : '📭';
    }

    // 초기 알림 상태 설정
    loadNotifications();
});
