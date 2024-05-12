function sendLoginRequest() {
    event.preventDefault();
    const data = {
        userId: document.getElementById('userId').value,
        passwd: document.getElementById('passwd').value
    };

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
                window.location.href = '/map'; // 로그인 후 이동할 페이지
            } else {
                console.log('Login failed:', data.message);
                alert('로그인 실패: ' + data.message);
            }
        })
        .catch(error => {
            console.error('Error during login:', error);
            alert('로그인 처리 중 오류 발생');
        });
}
