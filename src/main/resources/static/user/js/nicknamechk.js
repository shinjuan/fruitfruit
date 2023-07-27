$(document).on('blur', '#nickname', () => {
    // Axios 사용해 Post 요청

    let nickname = document.getElementById("nickname").value;

    axios({
        method: "post",
        url: "/nicknamechk", // "/alert" URL 로 요청
        data: {
            "nickname": nickname, // 전송할 데이터의 제목 필드
        },
        dataType: "JSON", // 응답 데이터 타입
        headers: {'Content-Type': 'application/json'} // 요청 헤더에 JSON 형식으로 데이터 전송
    }).then(res => {


        $("#nicknamechk").text(res.data);


        //$("body").append(res.data);
        // console.log(res.data.test);





        for (let i = 0; i < res.data.length; i++) {
            const html = `<b>이름 : </b> ${res.data[i].name} <br>
<b>이메일 : </b> ${res.data[i].email} <br>`;
            $("body").append(html);
        }
    });
});