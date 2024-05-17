$(document).ready(function () {
    $.ajax({
        url: '/bungae/getList',
        type: 'GET',
        dataType: 'json',
        success: function (data) {
            let createListItem = function (bungae) {
                return `
              <a href="/bungae/bungae_detail/${bungae.bungaeId}" class="list-group-item list-group-item-action flex-column align-items-start">
                <div class="row">
                  <div class="col-md-4">
                    <img class="d-block img-fluid" src="https://static.pingendo.com/img-placeholder-1.svg">
                  </div>
                  <div class="col-md-8">
                    <div class="d-flex w-100 justify-content-between">
                      <h5 class="my-2">${bungae.bungaeName}</h5>
                    </div>
                    <p class="text-left">일시: ${bungae.bungaeStartTime}</p>
                    <p class="text-left">장소: [위도: ${bungae.bungaeLocation.latitude}, 경도: ${bungae.bungaeLocation.longitude}]</p>
                    <p class="text-left">최대인원: ${bungae.bungaeMaxMember}</p>
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