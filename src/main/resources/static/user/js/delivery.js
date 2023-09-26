$(document).on('click', '#new_delivery', () => {


    showModal();



});

// 모달창 보이기
function showModal() {

    $(".delivery__add").css("display", "block");
}

// 모달창 감추기
function hideModal() {
    $(".delivery__add").css("display", "none");
}