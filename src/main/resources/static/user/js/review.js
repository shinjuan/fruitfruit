$(document).on('click', '#write_review', () => {

    // 모달창 보이기

    showModal_write_review()



});

$(document).on('click', '[id^="review_edit_"]', function(event) {

    const reviewId = $(this).val();
    // review_id hidden input의 value 설정
    $('input[name="review_id"]').val(reviewId);
    // 모달창 보이기
    $('input[name="review_type"]').val("edit");

    showModal_write_review()



});


$(document).on('click', '[id^="write_reply_"]', function(event) {
    // 클릭한 버튼의 ID에서 reviewId 추출
    const reviewId = $(this).val();



    // review_id hidden input의 value 설정
    $('input[name="review_id"]').val(reviewId);

    // 모달창 보이기
    showModal_write_review();
});


function showModal_write_review() {
    $("#modal_write_review").css("display", "block");
}

// 모달창 감추기
function hideModal_write_review() {
    $("#modal_write_review").css("display", "none");
};


