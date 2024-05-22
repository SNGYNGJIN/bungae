$(document).ready(function () {
    $('#joinButton').click(function () {
        let bungaeId = $(this).data('bungae-id');

        $.ajax({
            type: 'POST',
            url: '/' + bungaeId + '/join',
            success: function (response) {
                alert('참가 완료!');
            },
            error: function (xhr) {
                let errorMessage;
                if (xhr.status === 400) {
                    errorMessage = '잘못된 요청: ' + xhr.responseText;
                } else if (xhr.status === 409) {
                    errorMessage = '모임의 중복 참가는 불가능합니다. ' + xhr.responseText;
                } else {
                    errorMessage = '참가 실패. 다시 시도해주세요.';
                }
                alert(errorMessage);
            }
        });
    });
});