// ID 중복 검사 함수를 Promise를 반환하도록 변경
function checkID() {
    const userId = document.getElementById('userId').value;
    const idCheckMessage = document.getElementById('idCheckMessage');

    return fetch('/user/check_id', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ id: userId })
    })
        .then(response => response.json())
        .then(data => {
            if (data.data.available) {
                alert("사용 가능한 아이디입니다.");
                idCheckMessage.textContent = "* 중복 확인 완료";
                idCheckMessage.style.color = "green";
                return true;
            } else {
                alert("이미 사용 중인 아이디입니다.");
                idCheckMessage.textContent = "* 아이디 사용 불가";
                idCheckMessage.style.color = "red";
                return false;
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert("아이디 확인 중 오류가 발생했습니다.");
            return false;
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
    } else {
        pwdMessage.textContent = "비밀번호가 일치합니다";
        pwdMessage.style.color = "green";
        return true;
    }
}

// 폼 제출 함수
async function submitForm() {
    let isValidID = await checkID();
    let isValidPWD = checkPWD();

    if (isValidID && isValidPWD) {
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
            url: "/api/signup",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).done(function(response) {
            if (response.success) {
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
