document.addEventListener('DOMContentLoaded', function() {
    const userId = localStorage.getItem('userId'); // 현재 사용자 ID 가져오기
    checkImageSource();

    fetch(`/user/api/info/${userId}`, { // 닉네임 정보 요청
        method: 'GET',
        headers: { 'Accept': 'application/json' }
    }).then(response => response.json()).then(data => {
        if (data.code === 200) {
            document.getElementById('nickname').value = data.result.nickname;
            document.getElementById('userInfo').value = data.result.profile.userInfo;
            document.getElementById('profile-image').src = data.result.profile.userImage;
            localStorage.setItem('id', data.result.id);
            const id = parseInt(localStorage.getItem('id'), 10);

            //userProfileRequest(id); // 사용자 프로필 정보 요청
        } else {
            console.error('Failed to fetch user info:', data.message);
        }
    }).catch(error => {
        console.error('Error fetching user info:', error);
    });
});

// 사용자가 가져온 사진 미리 보여주기
function previewImage(event) {
    var reader = new FileReader();
    reader.onload = function() {
        var output = document.getElementById('profile-image');
        output.src = reader.result;
        document.getElementById('delete-image-btn').disabled = false; // 이미지 업로드 후 삭제 버튼 활성화
    };
    reader.readAsDataURL(event.target.files[0]);
}

// 등록된 프로필 사진 삭제
function deleteImage() {
    var defaultImageSrc = 'http://localhost:8080/images/user.png';
    document.getElementById('profile-image').src = defaultImageSrc;
    document.getElementById('delete-image-btn').disabled = true; // 이미지 삭제 후 버튼 비활성화

    // 사용자 ID를 포함하여 요청을 보낸다.
    const userId = localStorage.getItem('userId'); // 사용자 ID를 로컬 스토리지에서 가져옵니다.

    fetch(`/user/api/deleteProfileImage/${userId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ userImage: defaultImageSrc })
    })
        .then(response => response.json())
        .then(data => {
            console.log("서버에서 이미지 삭제 처리: ", data.message);
        })
        .catch(error => {
            console.error("서버 이미지 삭제 실패: ", error);
        });
}


// 이미지 소스 확인해서 <이미지 삭제 버튼> 비활성화/활성화
function checkImageSource() {
    var currentImageSrc = document.getElementById('profile-image').src;
    var defaultImageSrc = 'http://localhost:8080/images/user.png';
    var isDefault = currentImageSrc === defaultImageSrc;
    document.getElementById('delete-image-btn').disabled = isDefault;
    return isDefault;
}


// 수정된 사항 적용
function updateForm() {
    event.preventDefault();

    const userId = localStorage.getItem('userId');
    let image = document.getElementById("image-upload").files[0];
    const nickname = document.getElementById("nickname").value;
    const userInfo = document.getElementById("userInfo").value;

    let formData = new FormData();
    if (image) {
        formData.append("imageUpload", image);  // 'imageUpload'는 컨트롤러에서 기대하는 파라미터 이름
    }
    formData.append("nickname", nickname);
    formData.append("userInfo", userInfo);

    fetch(`/user/api/updateProfile/${userId}`, {  // 수정된 API 경로
        method: 'POST',
        body: formData,
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.json();
        })
        .then(data => {
            // alert(JSON.stringify(data));
            if (data.userImage) {
                alert('프로필이 성공적으로 업데이트되었습니다.');
                document.getElementById('profile-image').src = data.userImage; // 이미지 경로
                window.location.href = 'profile' // 성공시 사용자를 프로필 페이지로 리디렉션
            } else {
                alert(`프로필 정보 업데이트 실패: ${data.message}`);
            }
        })
        .catch(error => {
            alert(`프로필 정보 업데이트 중 오류 발생: ${error.message} ${error.status}`);
            console.error(error)
        });

    console.log("Image: ", image);
    console.log("Nickname: ", nickname);
    console.log("User Info: ", userInfo);

    return false;  // 기본 HTML 폼 제출을 방지
}
