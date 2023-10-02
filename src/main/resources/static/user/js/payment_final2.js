$(document).on('click', '#paymentBtn_final2', () => {


    // 선택한 약관들을 배열에 저장합니다.
    let selectedTerms = [];
    if ($("#chk7").is(":checked")) selectedTerms.push(7);
    if ($("#chk8").is(":checked")) selectedTerms.push(8);
    if ($("#chk9").is(":checked")) selectedTerms.push(9);


    // 필수 약관 체크 여부 확인
    if (!(selectedTerms.includes(8) && selectedTerms.includes(9) )) {
        // 필수 약관 중 하나라도 체크되지 않은 경우 모달 알림창 표시
        showModal2("필수 동의 사항에 체크해야 합니다.");
        event.preventDefault();
    }
    if(document.getElementById('receiverName').value.trim() === '') {
        showModal2("받으실 분 성함을 입력해주세요");
        event.preventDefault();
    }
    if (document.getElementById('receiverPhone').value.trim() === '') {
        showModal2("받으실 분 연락처를 입력해주세요");
        event.preventDefault();
    }
    if(document.getElementById('add').value.trim() === '') {
        showModal2("우편번호를 입력해주세요");
        event.preventDefault();
    }
    if (document.getElementById('add2').value.trim() === '') {
        showModal2("주소를 입력해주세요");
        event.preventDefault();
    }
    if (document.getElementById('ask').value.trim() === '배송 시 요청사항 선택') {
        showModal2("배송 시 요청사항을 선택해주세요");
        event.preventDefault();
    }
    if (!$('input[name="cardType"]').is(':checked') && !$('input[name="cardType"]').is(':checked')) {
        showModal2("카드 구분을 선택해주세요.");
        event.preventDefault();
    }
});


// 모달창 보이기
function showModal2(message) {
    $("#modalMessage").text(message);
    $(".txt05").css("display", "block");
}

// 모달창 감추기
function hideModal2() {
    $(".txt05").css("display", "none");
}



