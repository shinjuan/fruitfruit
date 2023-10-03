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



$(document).on('click', '#edit_delivery', () => {


    showModal2();



});

// 모달창 보이기
function showModal2() {

    $(".delivery__edit").css("display", "block");
}

// 모달창 감추기
function hideModal2() {
    $(".delivery__edit").css("display", "none");
}