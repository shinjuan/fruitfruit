$(document).on('click', '#write_review', () => {

    // 모달창 보이기

    showModal_write_review()



});

function showModal_write_review() {
    $("#modal_write_review").css("display", "block");
}

// 모달창 감추기
function hideModal_write_review() {
    $("#modal_write_review").css("display", "none");
};

$(document).on('click', '#edit_review', () => {

    // 모달창 보이기



});


function showModal_edit_review() {
    $("#modalMessage").text();
    $("#modal_join").css("display", "block");
}

// 모달창 감추기
function hideModal_edit_review() {
    $("#modal_join").css("display", "none");
};



