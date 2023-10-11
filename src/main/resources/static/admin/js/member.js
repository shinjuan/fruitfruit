
// 페이지 번호 버튼
$(document).on('click', '.numberBtn2', (e) => {
    currentPage = parseInt($(e.currentTarget).attr("value"));
    updatePageInputAndSubmitForm();
});

// 다음 페이지 버튼
$(document).on('click', '.nextBtn2', () => {
    currentPage += 1;
    updatePageInputAndSubmitForm();
});

// 이전 페이지 버튼
$(document).on('click', '.prevBtn2', () => {
    currentPage -= 1;
    updatePageInputAndSubmitForm();
});

// 페이지 번호 업데이트 및 폼 제출
function updatePageInputAndSubmitForm() {
    $("#pageNum2").val(currentPage); // 페이지 번호를 hidden 입력 필드에 할당
    $("#search-form2").submit(); // 폼 제출
}