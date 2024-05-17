document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("bungaeType").addEventListener("change", function () {
        let selectedTopic = this.value;
        let minAgeInput = document.getElementById("bungaeMinAge");
        if (selectedTopic === 'DRINK') {
            minAgeInput.value = 20;
            minAgeInput.min = 20;
            minAgeInput.readOnly = true;
        } else {
            minAgeInput.value = '';
            minAgeInput.min = 0;
            minAgeInput.readOnly = false;
        }
    });
});
document.addEventListener("DOMContentLoaded", function () {
    let inputElement = document.getElementById("bungaeStartTime");

    // 현재 시간을 가져오기 위한 함수
    function getCurrentDateTime() {
        let now = new Date();
        let year = now.getFullYear();
        let month = (now.getMonth() + 1).toString().padStart(2, '0');
        let day = now.getDate().toString().padStart(2, '0');
        let hours = now.getHours().toString().padStart(2, '0');
        let minutes = now.getMinutes().toString().padStart(2, '0');
        let currentDateTime = year + '-' + month + '-' + day + ' ' + hours + ':' + minutes;
        return currentDateTime;
    }

    // 현재 이전의 일시를 숨기기 위한 함수
    function hidePastDateTime() {
        inputElement.min = getCurrentDateTime();
    }

    // 페이지 로드 시 현재 이전의 일시를 숨기기
    hidePastDateTime();

    inputElement.addEventListener("change", function () {
        let selectedDateTime = new Date(inputElement.value);
        let currentDateTime = new Date();
        let minStartTime = new Date(currentDateTime.getTime() + (1 * 60 * 60 * 1000));
        let koreanTime = new Date(minStartTime.getTime() + 1000 * 60 * 60 * 9);

        if (selectedDateTime < minStartTime) {
            let time = new Date(koreanTime.getTime());
            inputElement.value = time.toISOString().slice(0, 16);
        }
    });
});

document.addEventListener("DOMContentLoaded", () => {
    document.getElementById("createBungaeForm").addEventListener("submit", sendCreateBungaeRequest);
});

function sendCreateBungaeRequest(event) {
    event.preventDefault();

    const form = document.getElementById("createBungaeForm");
    const formData = new FormData(form);
    const userId = localStorage.getItem("userId");

    if (!userId) {
        alert("로그인이 필요합니다.");
        window.location.href = "/user/login";
        return;
    }

    formData.append("userId", userId);

    fetch("/bungae/create_bungae", {
        method: "POST",
        body: formData
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(errorData => {
                    throw new Error(`서버 오류 발생: ${response.status} ${response.statusText}, ${JSON.stringify(errorData)}`);
                });
            }
            return response.json();
        })
        .then(data => {
            console.log(data);
            if (data.status === "success") {
                window.location.href = data.url;
            } else {
                alert("번개모임 생성 중 오류가 발생했습니다.");
            }
        })
        .catch(error => {
            console.error("Error:", error);
            alert(`서버 오류가 발생했습니다: ${error.message}`);
        });
}
