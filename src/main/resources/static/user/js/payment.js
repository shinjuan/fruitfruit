$(document).on('click', '#paymentBtn', () => {


    let selectedIds = [];
    $('.cartProductId:checked').each(function () {
        selectedIds.push($(this).val());
    });

    if (selectedIds.length === 0) {
        alert('결제할 상품이 없습니다');
        return false;
    } else {
        // 선택된 체크박스 값을 hidden input으로 추가




        $('#selectedIdsContainer').empty(); // 이전에 추가된 값을 모두 제거

        $('#selectedIdsContainer').append('<input type="hidden" name="selectedCount" value="' + selectedIds.length + '">');

        selectedIds.forEach(function (productId) {
            $('#selectedIdsContainer').append('<input type="hidden" name="checkbox" value="' + productId + '">');
        });

        //form을 submit
        $('form').submit();
    }

});