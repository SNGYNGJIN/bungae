document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("Cg_code").addEventListener("change", function () {
        let selectedTopic = this.value;
        let minAgeInput = document.getElementById("bungaeMinAge");
        if (selectedTopic === '술자리') {
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
        let currentDateTime = year + '-' + month + '-' + day + 'T' + hours + ':' + minutes;
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
            inputElement.value = koreanTime.toISOString().slice(0, 16);
        }
    });
});