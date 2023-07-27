function checkPasswordMatch() {
    const password = document.getElementById('password').value;
    const password2 = document.getElementById('password2').value;
    const pwdchkMsg = document.getElementById('pwdchk');

    if (password !== password2) {
        pwdchkMsg.style.display = 'block'; // 메시지를 보여줌
    } else {
        pwdchkMsg.style.display = 'none'; // 메시지를 숨김
    }
}

// 비밀번호 입력 또는 비밀번호 확인 입력에서 이벤트 발생 시 실행되도록 설정
document.getElementById('password').addEventListener('input', checkPasswordMatch);
document.getElementById('password2').addEventListener('input', checkPasswordMatch);