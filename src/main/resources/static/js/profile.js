document.addEventListener('DOMContentLoaded', () => {
    const userId = localStorage.getItem('userId');
    if (!userId) {
        displayError("사용자 정보를 찾을 수 없습니다.");
        return;
    }

    userInfoRequest(userId);  // User 테이블에서 가져오는 정보 - 닉네임
});

// User 테이블에서 닉네임 가져오기
function userInfoRequest(userId) {
    fetch(`/user/api/info/${userId}`, {
        method: 'GET',
        headers: {
            'Accept': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => {
            if (data.code === 200) {
                const { nickname, id } = data.result;
                localStorage.setItem('nickname', nickname);
                document.getElementById('userNickname').textContent = nickname;
                userProfileRequest(id);  // ID를 사용하여 프로필 정보 요청
            } else {
                displayError(`사용자 정보 가져오기 실패: ${data.message}`);
            }
        })
        .catch(error => {
            displayError(`사용자 정보 가져오기 중 오류 발생: ${error.message}`);
        });
}

// userprofile 테이블에서 자기소개, 프로필 사진 가져오기
function userProfileRequest(id) {
    fetch(`/user/api/info/profile/${id}`, {
        method: 'GET',
        headers: {
            'Accept': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => {
            if (data.code === 200) {
                const { userRating, userAge, gender, userInfo, userImage } = data.result;

                document.getElementById('userReviewScore').textContent = `받은 후기들의 평점은 ${userRating}점입니다.`;
                document.getElementById('Age-Gender').textContent = `${userAge}세 / ${gender === 'FEMALE' ? '여' : '남'}`;
                document.getElementById('userInfo').textContent = userInfo;
                document.getElementById('profile-image').src = userImage; // 이미지 경로
            } else {
                displayError(`프로필 정보 가져오기 실패: ${data.message}`);
            }
        })
        .catch(error => {
            displayError(`프로필 정보 가져오기 중 오류 발생: ${error.message}`);
        });
}




function displayError(message) {
    const errorContainer = document.getElementById('errorContainer');
    if (errorContainer) {
        errorContainer.textContent = message;
        errorContainer.style.display = 'block';
    }
}
