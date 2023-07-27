$(document).on('click', '#alert', () => {
    // Axios 사용해 Post 요청
    axios({
        method: "post",
        url: "/test2", // "/alert" URL 로 요청
        data: {
            "title": "title 테스트22222222", // 전송할 데이터의 제목 필드
            "msg": "msg 테스트33333" // 전송할 데이터의 메시지 필드
        },
        dataType: "JSON", // 응답 데이터 타입
        headers: {'Content-Type': 'application/json'} // 요청 헤더에 JSON 형식으로 데이터 전송
    }).then(res => {
        // 서버로부터의 응답 데이터는 res 변수에 저장

        // 응답 데이터(res.data)에 포함된 "alert.html"을 페이지에 추가
        // $("body").append(res.data);
       // console.log(res.data.test);





        for (let i = 0; i < res.data.length; i++) {
            const html = `<b>이름 : </b> ${res.data[i].name} <br>
<b>이메일 : </b> ${res.data[i].email} <br>`;
            $("body").append(html);
        }
    });
});