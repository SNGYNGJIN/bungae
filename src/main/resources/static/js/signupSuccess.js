/*
async function submitForm(event) {
    event.preventDefault(); // 폼의 기본 제출 동작 방지

    let data = {
        id : document.getElementById("id").value,
        userId: document.getElementById("userId").value,
        nickname: document.getElementById("nickname").value,
    };

    try {
        const response = await fetch('/user/api/signup', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        const jsonResponse = await response.json();

        if (response.code === 200) {
            localStorage.setItem('nickname', jsonResponse.nickname); // 성공 시 닉네임 저장
            window.location.href = '/user/signupSuccess'; // 성공 페이지로 리디렉션
        } else {
            alert('닉네임: ' + jsonResponse.nickname);
        }
    } catch (error) {
        console.error('회원가입 처리 중 오류 발생:', error);
        alert('회원가입 처리 중 오류 발생');
    }
}
*/
