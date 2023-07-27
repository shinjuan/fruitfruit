function checkLength() {
    const inputElement = document.getElementById('nickname');
    const inputValue = inputElement.value.trim();
    const maxLength = 10; // 최대 글자 수

    // 한글과 같이 2자 이상의 바이트를 차지하는 문자 처리를 위해 정규식 사용
    const byteLength = (s) => s.split('').reduce((a, v) => a + (v.charCodeAt(0) > 127 ? 2 : 1), 0);

    if (byteLength(inputValue) < 2 || byteLength(inputValue) > maxLength) {
        // 글자 수가 제한 범위를 벗어난 경우

        // 텍스트를 제한 범위 내로 잘라서 다시 입력
        inputElement.value = inputValue.substring(0, maxLength);
    }
}