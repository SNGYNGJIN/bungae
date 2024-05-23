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
                sessionStorage.setItem('loggedInUserId', userId);

                localStorage.setItem('userId', userId); // 'userId' 값을 로컬 스토리지에 저장
                window.location.href = '/map';
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

function sendFindIdRequest() {
    event.preventDefault();  // 폼 기본 제출 방지

    const name = document.getElementById('userName').value;
    const birth = document.getElementById('userBirth').value;
    const email = document.getElementById('userEmail').value;

    var Data = { name, birth, email };

    fetch('/user/api/find_id', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(Data)
    })
        .then(response => {
            if (!response.ok) throw new Error('find failed');
            return response.json();
        })
        .then(data => {
            if (data.code === 200) {
                alert("회원님의 아이디는 < " + data.result.userId + " >입니다.");
                var modal = document.getElementById('findUserIdModal');
                var modalInstance = new bootstrap.Modal(modal);
                modalInstance.hide();
            } else {
                console.log('Login failed:', data.message);
                alert('로그인 실패: ' + data.message);
            }
        })
        .catch(error => {
            console.error('Error during login:', error);
            alert('일치하는 정보가 없습니다.');
        });
}
