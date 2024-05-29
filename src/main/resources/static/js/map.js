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

        var imageSrc = '/images/myLoc.png', // 마커이미지의 주소입니다
            imageSize = new kakao.maps.Size(30, 35), // 마커이미지의 크기입니다
            imageOption = {offset: new kakao.maps.Point(27, 69)}; // 마커이미지의 옵션입니다. 마커의 좌표와 일치시킬 이미지 안에서의 좌표를 설정합니다.

        // 마커의 이미지정보를 가지고 있는 마커이미지를 생성합니다
        var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imageOption);

        var locPosition = new kakao.maps.LatLng(lat, lon)
        var marker = new kakao.maps.Marker({
            position: locPosition,
            image: markerImage
        });
        marker.setMap(map);

        var content = '<div class="customoverlay">' +
            '  <a>' +
            '    <span class="title">현재 위치</span>' +
            '  </a>' +
            '</div>';

        // 커스텀 오버레이를 생성합니다
        var customOverlay = new kakao.maps.CustomOverlay({
            map: map,
            position: locPosition,
            content: content,
            yAnchor: 1
        });

        // 지도의 이동이 끝날 때마다 호출되는 이벤트
        kakao.maps.event.addListener(map, 'idle', function () {
            updateBungaeList();
        });

        // 체크박스 선택 상태 변경 이벤트 리스너를 등록합니다
        $('input[name="category"]').on('change', function () {
            updateBungaeList();
        });

        function updateBungaeList() {
            var selectedCategories = $('input[name="category"]:checked').map(function () {
                return this.value;
            }).get();
            $.ajax({
                url: '/bungae/getList', // 번개 모임 정보를 가져오는 서버의 URL
                type: 'GET',
                dataType: 'json',
                success: function (data) {
                    var listContainer = $('#bungae-list');
                    listContainer.empty(); // 리스트 초기화

                    var geocoder = new kakao.maps.services.Geocoder();

                    data.forEach(function (bungae) {
                        geocoder.addressSearch(bungae.bungaeLocation.address, function (result, status) {
                            if (status === kakao.maps.services.Status.OK) {
                                var lat = parseFloat(result[0].y); // 위도
                                var lng = parseFloat(result[0].x); // 경도

                                if (isInMapBounds(lat, lng) && (selectedCategories.length === 0 || selectedCategories.includes(bungae.bungaeType))) {
                                    listContainer.append(createListItem(bungae));
                                }
                            }
                        });
                    });
                },
                error: function (xhr, status, error) {
                    console.error("Error occurred: " + error);
                }
            });
        }

        function isInMapBounds(lat, lng) {
            var bounds = map.getBounds();
            var sw = bounds.getSouthWest(); // 남서쪽 경계
            var ne = bounds.getNorthEast(); // 북동쪽 경계
            return lat >= sw.getLat() && lat <= ne.getLat() && lng >= sw.getLng() && lng <= ne.getLng();
        }

        function createListItem(bungae) {
            // 번개 모임 데이터를 기반으로 리스트 아이템 HTML 생성
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
               <a href="bungae/bungae_detail/${bungae.bungaeId}" class="list-group-item list-group-item-action flex-column align-items-start">
                   <div class="d-flex w-100 justify-content-between">
                       <h5 class="mb-1">${bungae.bungaeName} <i class="${bungaeTypeIcon}"></i></h5>
                       <small class="text-muted">${formatDateTime(bungae.bungaeStartTime)}</small>
                   </div>
               </a>`;
               function formatDateTime(dateTimeString) {
                   var date = new Date(dateTimeString);

                   var month = (date.getMonth() + 1).toString().padStart(2, '0'); // 월은 0부터 시작하므로 1을 더함
                   var day = date.getDate().toString().padStart(2, '0');
                   var hours = date.getHours().toString().padStart(2, '0');
                   var minutes = date.getMinutes().toString().padStart(2, '0');

                   return `${month}월${day}일 ${hours}:${minutes}`;
               }
        }


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

                        let imageSrc = bungae.bungaeImagePath ? `/uploads/${bungae.bungaeImagePath}` : 'https://static.pingendo.com/img-placeholder-1.svg';

                        var content = `
                            <div class="wrap">
                                <div class="info">
                                    <div class="title">${bungae.bungaeName}
                                        <div class="close" onclick="closeOverlay('${overlayId}')" title="닫기"></div>
                                    </div>
                                    <div class="body">
                                        <div class="img">
                                            <img src="${imageSrc}" width="83" height="80">
                                        </div>
                                        <div class="desc">
                                            <div class="ellipsis">장소: ${bungae.bungaeLocation.keyword}</div>
                                            <div class="time ellipsis">일시: ${bungae.bungaeStartTime}</div>
                                            <div class="age ellipsis">연령대: ${bungae.bungaeMinAge}세~${bungae.bungaeMaxAge}세</div>
                                            <div class="member ellipsis">최대인원: ${bungae.bungaeMaxMember}명</div>
                                            <div><a href="bungae/bungae_detail/${bungae.bungaeId}" class="link">상세보기</a></div>
                                        </div>
                                    </div>
                                </div>
                            </div>`;

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


