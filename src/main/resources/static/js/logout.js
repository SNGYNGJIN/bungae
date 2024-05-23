document.addEventListener('DOMContentLoaded', function() {
    const loggedInUserId = sessionStorage.getItem("loggedInUserId");
    const loginLogoutButton = document.getElementById('loginLogoutButton');

    if (loggedInUserId) {
        loginLogoutButton.textContent = '로그아웃';
        loginLogoutButton.href = '#';
    } else {
        // 로그인 상태가 아니면 텍스트를 '로그인'으로 설정
        loginLogoutButton.textContent = '로그인';
        loginLogoutButton.href = 'login';
    }

    loginLogoutButton.addEventListener('click', function(event) {
        if (loggedInUserId) {
            // '로그아웃' 버튼 클릭 시
            event.preventDefault();
            sessionStorage.removeItem("loggedInUserId");
            window.location.href = '/login';
        }
    });
});