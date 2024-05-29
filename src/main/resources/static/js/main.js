$(document).ready(function () {
    $.ajax({
        url: '/bungae/getListOfCreateTime',
        type: 'GET',
        dataType: 'json',
        success: function (data) {
            let createListItem = function (bungae, isActive) {
                let activeClass = isActive ? 'active' : '';
                let textClass = isActive ? '' : 'text-muted';
                let bungaeTypeIcon = '';
                switch (bungae.bungaeType) {
                    case 'DRINK':
                        bungaeTypeIcon = 'fa fa-beer';
                        break;
                    case 'SOCCER':
                        bungaeTypeIcon = 'fa fa-futbol';
                        break;
                    case 'BASKETBALL':
                        bungaeTypeIcon = 'fa fa-basketball-ball';
                        break;
                    case 'CYCLE':
                        bungaeTypeIcon = 'fa fa-bicycle';
                        break;
                    case 'RUNNING':
                        bungaeTypeIcon = 'fa fa-running';
                        break;
                    case 'STUDY':
                        bungaeTypeIcon = 'fa fa-book';
                        break;
                    default:
                        bungaeTypeIcon = '';
                        break;
                }
                return `
                    <a href="bungae/bungae_detail/${bungae.bungaeId}" class="list-group-item list-group-item-action flex-column align-items-start ${activeClass}">
                        <div class="d-flex w-100 justify-content-between">
                            <h5 class="mb-1">${bungae.bungaeName} <i class="${bungaeTypeIcon}"></i></h5>
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
                let bungaeTypeIcon = '';
                switch (bungae.bungaeType) {
                    case 'DRINK':
                        bungaeTypeIcon = 'fa fa-beer';
                        break;
                    case 'SOCCER':
                        bungaeTypeIcon = 'fa fa-futbol';
                        break;
                    case 'BASKETBALL':
                        bungaeTypeIcon = 'fa fa-basketball-ball';
                        break;
                    case 'CYCLE':
                        bungaeTypeIcon = 'fa fa-bicycle';
                        break;
                    case 'RUNNING':
                        bungaeTypeIcon = 'fa fa-running';
                        break;
                    case 'STUDY':
                        bungaeTypeIcon = 'fa fa-book';
                        break;
                    default:
                        bungaeTypeIcon = '';
                        break;
                }
                return `
            <a href="bungae/bungae_detail/${bungae.bungaeId}" class="list-group-item list-group-item-action flex-column align-items-start ${activeClass}">
                <div class="d-flex w-100 justify-content-between">
                    <h5 class="mb-1">${bungae.bungaeName} <i class="${bungaeTypeIcon}"></i></h5>
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

    $('#search-button').click(function () {
        let keyword = $('#inlineFormInputGroup').val();
        if (keyword) {
            window.location.href = `/bungae_list?keyword=${encodeURIComponent(keyword)}`;
        } else {
            alert('검색어를 입력해주세요');
        }
    });


    $('#search-form').submit(function (event) {
        event.preventDefault();
        $('#search-button').click();
    });
});