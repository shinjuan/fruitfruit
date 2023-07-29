$(document).on('click', '#changepw', () => {

    let newpwd = document.getElementById("password").value;
    let newpwd2 = document.getElementById("password2").value;

    axios({
        method: "post",
        url: "/changepw_ok", // "/alert" URL 로 요청
        data: {
            "newpwd": newpwd, // 전송할 데이터의 제목 필드
            "newpwd2": newpwd2
        },
        dataType: "JSON", // 응답 데이터 타입
        headers: {'Content-Type': 'application/json'} // 요청 헤더에 JSON 형식으로 데이터 전송
    }).then(res => {

        $("body").append(res.data);

    });
});
