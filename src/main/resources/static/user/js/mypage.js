// 현재 페이지 변수
let currentPage = 1;



// 페이지 번호 버튼
$(document).on('click', '.numberBtn', (e) => {
    currentPage = parseInt($(e.currentTarget).attr("value"));
    updatePageInputAndSubmitForm();
});

// 다음 페이지 버튼
$(document).on('click', '.nextBtn', () => {
    currentPage += 1;
    updatePageInputAndSubmitForm();
});

// 이전 페이지 버튼
$(document).on('click', '.prevBtn', () => {
    currentPage -= 1;
    updatePageInputAndSubmitForm();
});

// 페이지 번호 업데이트 및 폼 제출
function updatePageInputAndSubmitForm() {
    $("#pageNum").val(currentPage); // 페이지 번호를 hidden 입력 필드에 할당
    $("#search-form").submit(); // 폼 제출
}