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
                            <small class="text-muted">현재 인원: ${bungae.currentMemberCount} / ${bungae.bungaeMaxMember}</small>
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


if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(function (position) {
        var lat = position.coords.latitude, // 위도
            lon = position.coords.longitude; // 경도

        var mapContainer = document.getElementById('map'), // 지도를 표시할 div
            mapOption = {
                center: new kakao.maps.LatLng(lat, lon), // 지도의 중심좌표
                level: 3 // 지도의 확대 레벨
            };

        // 지도를 생성합니다
        map = new kakao.maps.Map(mapContainer, mapOption);

        // 지도에 확대 축소 컨트롤을 생성한다
        var zoomControl = new kakao.maps.ZoomControl();

        // 지도의 우측에 확대 축소 컨트롤을 추가한다
        map.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT);

        var locPosition = new kakao.maps.LatLng(lat, lon)
        var marker = new kakao.maps.Marker({
            position: locPosition
        });
        marker.setMap(map);


        loadData();
    });
} else {
    // Geolocation 사용 불가능할 때 처리
    alert("현 위치를 찾을 수 없습니다.");
}


// 생성된 번개모임 데이터를 불러오는 함수
function loadData() {
    $.ajax({
        url: '/bungae/getList', // 데이터를 가져올 URL
        type: 'GET', // 데이터 전송 방식
        dataType: 'json', // 받을 데이터 형식
        success: function (response) {
            var geocoder = new kakao.maps.services.Geocoder();
            // response는 모임 목록 데이터입니다.
            response.forEach(function (bungae, index) {
                // 주소로 좌표를 검색합니다
                geocoder.addressSearch(bungae.bungaeLocation.address, function (result, status) {
                    // 정상적으로 검색이 완료됐으면
                    if (status === kakao.maps.services.Status.OK) {
                        var coords = new kakao.maps.LatLng(result[0].y, result[0].x);

                        // 결과값으로 받은 위치를 마커로 표시합니다
                        var marker = new kakao.maps.Marker({
                            map: map,
                            position: coords
                        });

                        // 오버레이 컨텐츠에 고유한 ID를 부여합니다
                        var overlayId = 'overlay_' + index;

                        var content = '<div class="wrap">' +
                            '    <div class="info">' +
                            '        <div class="title">' + bungae.bungaeName +
                            '            <div class="close" onclick="closeOverlay(\'' + overlayId + '\')" title="닫기"></div>' +
                            '        </div>' +
                            '        <div class="body">' +
                            '            <div class="img">' +
                            '                <img src="" width="73" height="70">' +
                            '            </div>' +
                            '            <div class="desc">' +
                            '                <div class="ellipsis">' + bungae.bungaeLocation.keyword + '</div>' +
                            '                <div class="jibun ellipsis">' + bungae.bungaeLocation.address + '</div>' +
                            '                <div><a href="bungae/bungae_detail/' + bungae.bungaeId + '" class="link">상세보기</a></div>' +
                            '            </div>' +
                            '        </div>' +
                            '    </div>' +
                            '</div>';

                        var overlay = new kakao.maps.CustomOverlay({
                            content: content,
                            map: map,
                            position: marker.getPosition(),
                            zIndex: 1
                        });

                        // 오버레이를 숨김 상태로 시작합니다
                        overlay.setMap(null);

                        // 마커를 클릭했을 때 커스텀 오버레이를 표시합니다
                        kakao.maps.event.addListener(marker, 'click', function () {
                            overlay.setMap(map);
                        });

                        // 각 오버레이를 전역 객체로 저장합니다
                        window[overlayId] = overlay;
                    }
                });
            });
        },
        error: function (error) {
            console.log('Error:', error);
        }
    });
}

// 커스텀 오버레이를 닫기 위해 호출되는 함수입니다
function closeOverlay(overlayId) {
    var overlay = window[overlayId];
    if (overlay) {
        overlay.setMap(null);
    }
}


