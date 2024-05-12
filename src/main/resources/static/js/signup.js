function checkID() {
    const userId = document.getElementById('userId').value;
    const idCheckMessage = document.getElementById('idCheckMessage');
    const idValid = document.getElementById('idValid');

    return fetch('/user/api/check_id', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ userId: userId })
    })
        .then(response => response.json())
        .then(data => {
            /*alert(JSON.stringify(data))*/
            if (data.result.available) {
                alert("사용 가능한 아이디입니다.");
                idCheckMessage.textContent = "* 중복 확인 완료";
                idCheckMessage.style.color = "green";
                idValid.value = "true";
                return true;
            } else {
                alert("이미 사용 중인 아이디입니다.");
                idCheckMessage.textContent = "* 아이디 사용 불가";
                idCheckMessage.style.color = "red";
                idValid.value = "false";
                return false;
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert("아이디 확인 중 오류가 발생했습니다.");
            return true;
        });
}

// 비밀번호 일치 검사
function checkPWD() {
    let pass1 = document.forms["signup"]["passwd"].value;
    let pass2 = document.forms["signup"]["passwdCheck"].value;
    let pwdMessage = document.getElementById("passwordMessage");

    if (pass1 !== pass2) {
        pwdMessage.textContent = "비밀번호가 일치하지 않습니다";
        pwdMessage.style.color = "red";
        return false;
    } else if (pass1.length > 0 && pass2.length > 0) {
        pwdMessage.textContent = "비밀번호가 일치합니다";
        pwdMessage.style.color = "green";
        return true; // 비밀번호 일치
    } else {
        pwdMessage.textContent = ""; // 입력이 없을 때 메시지를 비웁니다.
        return false; // 입력이 없는 경우에도 false를 반환
    }
}

// 폼 제출 함수
async function submitForm() {
    event.preventDefault();
    let isValidID = document.getElementById("idValid").value;
    let isValidPWD = checkPWD();

    if (isValidID=="true" && isValidPWD) {
        let data = {
            userId: $("#userId").val(),
            passwd: $("#passwd").val(),
            email: $("#email").val(),
            name: $("#name").val(),
            nickname: $("#nickname").val(),
            birth: $("#birth").val(),
            tel: $("#tel").val(),
            gender: $("#gender").val()
        };

        $.ajax({
            type: "POST",
            url: "/user/api/signup",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).done(function(response) {
            if (response.code === 200) {
                localStorage.setItem('nickname', response.result.nickname);
                window.location.href = '/user/signupSuccess'; // 성공 페이지로 리디렉션
            } else {
                alert('회원가입 실패: ' + response.message);
            }
        }).fail(function(error) {
            alert('회원가입 처리 중 오류 발생');
        });
    } else {
        alert("회원가입 실패했습니다.");
    }

    return false; // 기본 제출 이벤트 방지
}