document.addEventListener('DOMContentLoaded', function() {
    const reviewModal = document.getElementById('reviewModal');
    const reviewModal2 = document.getElementById('reviewModal2');
    const mnickname2 = document.getElementById('mnickname2');
    const usersContainer = document.getElementById('usersContainer');
    const currentId = sessionStorage.getItem('loggedInUserId');
    const submitReviewBtn = document.getElementById('submitReview');

    document.querySelectorAll('.go_review').forEach(function(goReviewBtn) {
        goReviewBtn.addEventListener('click', function () {
            const bungaeId = this.getAttribute('data-bungae-id');
            fetch(`/review/${bungaeId}`)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.json();
                })
                .then(async data => { // 비동기 함수로 처리
                    usersContainer.innerHTML = '';
                    for (let user of data) {
                        if (user.userId !== currentId) {
                            const reviewed = await getReviewed(bungaeId, currentId, user.userId); // 리뷰 상태를 가져옴
                            const userElement = document.createElement('div');
                            userElement.className = 'user-info';
                            userElement.innerHTML = `
                                    <img src="${user.userImage}" alt="Profile Image" style="width:50px; height:50px;">
                                    <p>${user.nickname}</p>
                                    <button class="btn btn-primary review-user-btn" data-user-id="${user.userId}" data-nickname="${user.nickname}" ${reviewed ? 'disabled' : ''}>
                                        ${reviewed ? '작성 완료' : '리뷰 등록'}
                                    </button>
                                    <hr>
                                `;
                            usersContainer.appendChild(userElement);

                            if (!reviewed) {
                                userElement.querySelector('.review-user-btn').addEventListener('click', function () {
                                    const userId = this.getAttribute('data-user-id');
                                    const nickname = this.getAttribute('data-nickname');
                                    mnickname2.textContent = nickname;
                                    mnickname2.dataset.userId = userId;
                                    mnickname2.dataset.bungaeId = bungaeId;
                                    $('#reviewModal2').modal('show');
                                });
                            }
                        }
                    }
                    $(reviewModal).modal('show');
                })
                .catch(error => {
                    console.error('Error fetching user details:', error);
                    alert('데이터를 불러오는데 실패했습니다.');
                });
        });
    });

    // 리뷰 최종 등록 버튼에 이벤트 리스너 추가
    submitReviewBtn.addEventListener('click', async function () {
        const reviewText = document.getElementById('reviewText').value;
        const selectedRating = document.querySelector('input[name="rating"]:checked').value;
        const userId = mnickname2.dataset.userId;
        const bungaeId = mnickname2.dataset.bungaeId;

        save(userId, currentId, reviewText, selectedRating, bungaeId ,async function (success) {
            if (success) {
                const modal1Btn = document.querySelector(`.review-user-btn[data-user-id="${userId}"]`);
                if (await getReviewed(bungaeId, currentId, userId)) {
                    modal1Btn.textContent = "작성 완료";
                    modal1Btn.disabled = true;
                }
            } else {
                alert('리뷰 등록에 실패했습니다. 다시 시도해주세요.');
            }
        });
    });
});

function save(userId, currentId, reviewText, selectedRating, bungaeId, callback) {
    fetch('/review/save/' + userId, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({bungaeId:bungaeId, reviewerId: currentId ,star: selectedRating, review: reviewText})
    })
        .then(response => response.json())
        .then(data => {
            console.log('Success:', data);
            alert('리뷰가 성공적으로 등록되었습니다!');
            callback(true);
            $('#reviewModal2').modal('hide'); // 성공 후 모달 닫기
        })
        .catch(error => {
            console.error('Error:', error);
            callback(false);
            alert('리뷰 등록에 실패했습니다.');
        });
}

async function getReviewed(bungaeId, currentId, userId) {
    try {
        const response = await fetch('/review/reviewed', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ bungaeId: bungaeId, reviewerId: currentId, userId: userId })
        });
        const data = await response.json();
        console.log('Success:', data);
        return data;
    } catch (error) {
        console.error('Error:', error);
        return false; // 에러 발생 시 false 반환
    }
}