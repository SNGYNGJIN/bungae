document.addEventListener('DOMContentLoaded', function() {
    const loggedInUserId = sessionStorage.getItem("loggedInUserId");
    const loginLogoutButton = document.getElementById('loginLogoutButton');

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
});
