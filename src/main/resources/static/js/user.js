let index = {
    init: function() {
        $("#btn-save").on("click", () => { //function를 사용하지않은 이유는 this를 바인딩하기 위해서
            this.save();
        });
    },

    save: function() {
        // alert("user의 svae할수있음 ");
        let data = {
            userId: $("#userId").val(),
            username: $("#username").val(),
            password: $("#password").val(),
            nickname: $("#nickname").val(),
            email: $("#email").val(),
            tel: $("#tel").val(),
            gender : $("#gender").val(),
            birth: $("#birth").val()
        };

        //console.log(data);


        //ajax호출시 default가 비동기호출
        //ajax 통신을 이용해서 3개의 데이터를 json으로 변경해서 insert 요청
        $.ajax({
            type:"POST",
            url:"/api/user", //맵핑되는 주소랑 동일, insert되는 주소는 api폴더안에 넣을거라서
            data:JSON.stringify(data), // json으로 변경해야함
            /*body 데이터가 어떤 타입인지, MINE타입연관 */
            contentType:"application/json; charset=utf-8",

            /*
            요청을 서버로해서 응답이 왔을 때, 숫자가올수도잇고, 문자가 올수도있고,
            json이 올수도 있는걸 json타입으로 변경해줌
            * 응답은 다 String(문자열로 옴)
            */

            dataType:"json"
            //회원가입 수행 요청
        }).done(function(resp){
            alert("회원가입이 완료되었습니다. ");
            //console.log(resp);
            //location.href="/";
        }).fail(function(error){
            alert(JSON.stringify(error));
        });

    }
}

index.init();