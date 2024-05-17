function sendLoginRequest() {
    event.preventDefault();  // 폼 기본 제출 방지

    // 입력 필드에서 아이디와 비밀번호 값 추출
    const userId = document.getElementById('userId').value;
    const passwd = document.getElementById('passwd').value;
    const data = { userId, passwd };

    fetch('/user/api/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (!response.ok) throw new Error('Login failed');
            return response.json();
        })
        .then(data => {
            if (data.code === 200) {
                localStorage.setItem('accessToken', data.result.access_token);
                localStorage.setItem('refreshToken', data.result.refresh_token);
                sessionStorage.setItem('loggedInUserId', userId);
                localStorage.setItem('userId', userId); // 'userId' 값을 로컬 스토리지에 저장
                window.location.href = '/map';
            } else {
                console.log('Login failed:', data.message);
                alert('로그인 실패: ' + data.message);
            }
        })
}
