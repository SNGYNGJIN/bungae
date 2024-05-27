$(document).ready(function () {
    $.ajax({
        url: '/bungae/getListOfCreateTime',
        type: 'GET',
        dataType: 'json',
        success: function (data) {
            let createListItem = function (bungae, isActive) {
                let activeClass = isActive ? 'active' : '';
                let textClass = isActive ? '' : 'text-muted';
                return `
                    <a href="bungae/bungae_detail/${bungae.bungaeId}" class="list-group-item list-group-item-action flex-column align-items-start ${activeClass}">
                        <div class="d-flex w-100 justify-content-between">
                            <h5 class="mb-1">${bungae.bungaeName}</h5>
                        </div>
                        <p class="mb-1">${bungae.bungaeDescription}</p>
                        <small class="${textClass}">${bungae.bungaeStartTime}</small>
                        <small class="${textClass}">현재 인원: ${bungae.currentMemberCount} / ${bungae.bungaeMaxMember}</small>
                    </a>`;
            };

            let listContainer = $('#new-list');
            data.forEach(function (bungae, index) {
                let isActive = index % 2 === 0;
                listContainer.append(createListItem(bungae, isActive));
            });
        },
        error: function (err) {
            alert('Error: ' + err);
        }
    });

    $.ajax({
        url: '/bungae/getListOfStartTime',
        type: 'GET',
        dataType: 'json',
        success: function (data) {
            let createListItem = function (bungae, isActive) {
                let activeClass = isActive ? 'active' : '';
                let textClass = isActive ? '' : 'text-muted';
                return `
            <a href="bungae/bungae_detail/${bungae.bungaeId}" class="list-group-item list-group-item-action flex-column align-items-start ${activeClass}">
                <div class="d-flex w-100 justify-content-between">
                    <h5 class="mb-1">${bungae.bungaeName}</h5>
                </div>
                <p class="mb-1">${bungae.bungaeDescription}</p>
                <small class="${textClass}">${bungae.bungaeStartTime}</small>
                <small class="${textClass}">현재 인원: ${bungae.currentMemberCount} / ${bungae.bungaeMaxMember}</small>
            </a>`;
            };

            let listContainer = $('#deadline-list');
            data.forEach(function (bungae, index) {
                let isActive = index % 2 === 0;
                listContainer.append(createListItem(bungae, isActive));
            });
        },
        error: function (err) {
            alert('Error: ' + err);
        }
    });
});
