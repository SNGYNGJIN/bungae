document.addEventListener('DOMContentLoaded', function() {
    const userId = localStorage.getItem('userId'); // 현재 사용자 ID 가져오기
    checkImageSource();

    fetch(`/user/api/info/${userId}`, { // 닉네임 정보 요청
        method: 'GET',
        headers: { 'Accept': 'application/json' }
    }).then(response => response.json()).then(data => {
        if (data.code === 200) {
            document.getElementById('nickname').value = data.result.nickname;
            localStorage.setItem('id', data.result.id);
            userProfileRequest(data.result.id); // 사용자 프로필 정보 요청
        } else {
            console.error('Failed to fetch user info:', data.message);
        }
    }).catch(error => {
        console.error('Error fetching user info:', error);
    });
});

function userProfileRequest(id) {
    fetch(`/user/api/info/profile/${id}`, { // 프로필 정보 요청
        method: 'GET',
        headers: { 'Accept': 'application/json' }
    }).then(response => response.json()).then(data => {
        if (data.code === 200) {
            document.getElementById('userInfo').value = data.result.userInfo;
            document.getElementById('profile-image').src = data.result.userImage;
        } else {
            console.error('Failed to fetch user profile:', data.message);
        }
    }).catch(error => {
        console.error('Error fetching user profile:', error);
    });
}

function previewImage(event) {
    var reader = new FileReader();
    reader.onload = function() {
        var output = document.getElementById('profile-image');
        output.src = reader.result;
        document.getElementById('delete-image-btn').disabled = false; // 이미지 업로드 후 삭제 버튼 활성화
    };
    reader.readAsDataURL(event.target.files[0]);
}

function deleteImage() {
    var defaultImageSrc = 'http://localhost:8080/images/user.png';
    document.getElementById('profile-image').src = defaultImageSrc;
    document.getElementById('delete-image-btn').disabled = true; // 이미지 삭제 후 버튼 비활성화

    fetch('/api/deleteProfileImage', { method: 'POST' }).then(response => response.json()).then(data => {
        console.log("서버에서 이미지 삭제 처리: ", data.message);
    }).catch(error => {
        console.error("서버 이미지 삭제 실패: ", error);
    });
}

function checkImageSource() { // 이미지 소스 확인
    var currentImageSrc = document.getElementById('profile-image').src;
    var defaultImageSrc = 'http://localhost:8080/images/user.png';
    document.getElementById('delete-image-btn').disabled = (currentImageSrc === defaultImageSrc);
}

function updateForm() {
    const id = localStorage.getItem('id');
    const image = document.getElementById("profile-image").src;
    const nickname = document.getElementById("nickname").value;
    const userInfo = document.getElementById("userInfo").value;

    let formData = new FormData(d);
    formData.append("image", image);
    formData.append("nickname", nickname);
    formData.append("userInfo", userInfo);

    fetch(`/user/api/profile/update/${id}`, {
        method: 'POST',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            if (data.code === 200) {
                alert('프로필이 성공적으로 업데이트되었습니다.');
                window.location.href = '/profile'; // 프로필 페이지로 리디렉션
            } else {
                alert(`프로필 정보 업데이트 실패: ${data.message}`);
            }
        })
        .catch(error => {
            alert(`프로필 정보 업데이트 중 오류 발생: ${error.message}`);
        });

    return false; // 기본 HTML 폼 제출을 방지
}

function updateForm() {
    const id = localStorage.getItem('id');
    const image = document.getElementById("profile-image").src;
    const nickname = document.getElementById("nickname").value;
    const userInfo = document.getElementById("userInfo").value;

    let data = {
        image: image,
        nickname: nickname,
        userInfo: userInfo
    };

    fetch(`/user/api/profile/update/${id}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => response.json())
        .then(data => {
            if (data.code === 200) {
                alert('프로필이 성공적으로 업데이트되었습니다.');
                window.location.href = '/user/profile';
            } else {
                alert(`프로필 정보 업데이트 실패: ${data.message}`);
            }
        })
        .catch(error => {
            alert(`프로필 정보 업데이트 중 오류 발생: ${error.message}`);
        });

    return false; // 기본 HTML 폼 제출을 방지
}