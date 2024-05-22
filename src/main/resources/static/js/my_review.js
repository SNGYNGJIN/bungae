document.addEventListener('DOMContentLoaded', function() {
    const reviewModal = document.getElementById('myreviewModal');
    const myReviewBtn = document.getElementById('myReview');
    const usersContainer = document.getElementById('usersContainer');
    const paginationContainer = document.getElementById('pagination');
    const currentId = sessionStorage.getItem('loggedInUserId');

    function displayReviews(reviews, page = 1, reviewsPerPage = 5) {
        const start = (page - 1) * reviewsPerPage;
        const end = page * reviewsPerPage;
        usersContainer.innerHTML = ''; // Clear previous content

        reviews.slice(start, end).forEach(review => {
            const reviewElement = document.createElement('div');
            reviewElement.className = 'review-info';
            reviewElement.innerHTML = `
                <p>${generateStarRating(review.rating)}</p>
                <p>${review.content}</p>
                <hSDFASDFASDr>
            `;
            usersContainer.appendChild(reviewElement);
        });

        setupPagination(reviews, reviewsPerPage);
    }

    function setupPagination(reviews, reviewsPerPage) {
        paginationContainer.innerHTML = '';
        let pageCount = Math.ceil(reviews.length / reviewsPerPage);
        for (let i = 1; i <= pageCount; i++) {
            const pageBtn = document.createElement('button');
            pageBtn.innerText = i;
            pageBtn.addEventListener('click', () => displayReviews(reviews, i, reviewsPerPage));
            paginationContainer.appendChild(pageBtn);
        }
    }


    myReviewBtn.addEventListener('click', function() {
        fetch(`/review/myReview/${currentId}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                data.reverse(); // Reverse the array to display reviews in descending order
                displayReviews(data); // Show the first set of reviews
                $(reviewModal).modal('show');
            })
            .catch(error => {
                console.error('Error fetching review data:', error);
                alert('Failed to fetch review data.');
            });
    });
});

function generateStarRating(rating) {
    let fullStars = Math.floor(rating); // 전체 별의 수
    let halfStar = rating % 1 !== 0 ? 1 : 0; // 반 별의 유무
    let starsHTML = '';

    // 전체 별 이미지 추가
    for (let i = 0; i < fullStars; i++) {
        starsHTML += '<img src="/images/filledStar.svg" alt="Full star">';
    }

    // 반 별 이미지 추가
    if (halfStar) {
        starsHTML += '<img src="/images/halfStar.svg" alt="Half star">';
    }

    return starsHTML;
}
