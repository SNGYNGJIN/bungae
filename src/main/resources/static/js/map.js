$(document).ready(function () {
    $.ajax({
        url: '/bungae/getList',
        type: 'GET',
        dataType: 'json',
        success: function (data) {
            let createListItem = function (bungae) {
                return `
                    <a href="bungae/bungae_detail/${bungae.bungaeId}" class="list-group-item list-group-item-action flex-column align-items-start">
                        <div class="d-flex w-100 justify-content-between">
                            <h5 class="mb-1">${bungae.bungaeName}</h5>
                            <small class="text-muted">시작: ${bungae.bungaeStartTime}</small>
                        </div>
                    </a>`;
            };

            let listContainer = $('#bungae-list');
            data.forEach(function (bungae) {
                listContainer.append(createListItem(bungae));
            });
        },
        error: function (err) {
            alert('Error: ' + err);
        }
    });
});