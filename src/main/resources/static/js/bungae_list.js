$(document).ready(function () {
    $.ajax({
        url: '/bungae/getList',
        type: 'GET',
        dataType: 'json',
        success: function (data) {
            let createListItem = function (bungae) {
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

                let imageSrc = bungae.bungaeImagePath ? `/uploads/${bungae.bungaeImagePath}` : 'https://static.pingendo.com/img-placeholder-1.svg';

                return `
              <a href="bungae/bungae_detail/${bungae.bungaeId}" class="list-group-item list-group-item-action flex-column align-items-start">
                <div class="row">
                  <div class="col-md-4">
                    <img class="d-block img-fluid" src="${imageSrc}">
                  </div>
                  <div class="col-md-8">
                    <div class="d-flex w-100 justify-content-between">
                      <h5 class="my-2">${bungae.bungaeName} <i class="${bungaeTypeIcon}"></i></h5>
                    </div>
                    <p class="text-left">일시: ${bungae.bungaeStartTime}</p>
                    <p class="text-left">장소: ${bungae.bungaeLocation.keyword}</p>
                    <p class="text-left">상세 주소: ${bungae.bungaeLocation.address}</p>
                    <p class="text-left">현재 인원: ${bungae.currentMemberCount} / ${bungae.bungaeMaxMember}</p>
                  </div>
                </div>
              </a>`;
            };

            $.each(data, function (i, bungae) {
                if (i % 2 === 0) {
                    $('#bungae-list-left').append(createListItem(bungae));
                } else {
                    $('#bungae-list-right').append(createListItem(bungae));
                }
            });
        },
        error: function (err) {
            alert('Error: ' + err);
        }
    });
});