$(document).on('click', '#findpw', () => {

    let email = document.getElementById("find_email").value;


    axios({
        method: "post",
        url: "/findpw_ok", // "/alert" URL 로 요청
        data: {
            "email": email, // 전송할 데이터의 제목 필드
        },
        dataType: "JSON", // 응답 데이터 타입
        headers: {'Content-Type': 'application/json'} // 요청 헤더에 JSON 형식으로 데이터 전송
    }).then(res => {

        $("body").append(res.data);

    });
});


