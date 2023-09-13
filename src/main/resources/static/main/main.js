let selectedCategory = '';
let searchKeyword = ''; // 전역 변수로 선언
let selectedTab = 9;
let currentPage = 1;







$(document).on('click', '.category-btn', () => {
    selectedCategory = $(event.target).val();
    fetchData();
});

$(document).on('click', '.material-icons', () => {
    searchKeyword = $('#searchKeyword').val(); // 전역 변수에 값 저장
    fetchData(); // fetchData 함수 호출 (매개변수 없음)
});


// 이전 페이지 버튼
$(document).on('click', '.prevBtn', () => {
    currentPage -= 1;
    fetchData();
});

// 페이지 번호 버튼
$(document).on('click', '.numberBtn', (e) => {
    currentPage = parseInt($(e.currentTarget).attr("value"));
    fetchData();
});

// 다음 페이지 버튼
$(document).on('click', '.nextBtn', () => {
    currentPage += 1;
    fetchData();
});

function fetchData() {
    axios({
        method: 'post',
        url: '/main', // 여러분이 실제로 사용하는 API 엔드포인트로 대체해주세요
        data: {

            selectedCategory: selectedCategory,
            searchKeyword: searchKeyword,
            selectedTab: selectedTab,
            currentPage: currentPage
        },
        dataType: 'JSON', // 응답 데이터 타입
        headers: { 'Content-Type': 'application/json' } // 요청 헤더에 JSON 형식으로 데이터 전송
    }).then(response => {
        // 응답 데이터를 처리하는 부분입니다



        $("#axiosBody").empty();




        for (let i = 0; i < response.data.data.length; i++) {

            const html = `
                
                <li>
                <input class="productInitPrice" type="hidden" value="${response.data.data[i].price}">
                <input class="productDiscountPrice" type="hidden" value="${response.data.data[i].price * response.data.data[i].rate} / 100">
                <input class="productDiscountedPrice" type="hidden" value="${response.data.data[i].price - (response.data.data[i].price * response.data.data[i].rate / 100)}">
                <input class="productDiscount" type="hidden" value="${response.data.data[i].rate}">

                <!-- 클릭 시 상세페이지 이동 -->
                <a href="/user/productDetail/${response.data.data[i].id}">

                    <!-- 상품 사진 -->
                    <img class="productImage" src="${response.data.data[i].file_name}" alt="상품 이미지">

                </a>
                <!-- 상품 -->
                <div class="txt">
                    <!-- 상품명 -->
                    <div class="title">
                        <span class="productName" >${response.data.data[i].name}</span>

                    </div>

                    <!-- 상품 가격 -->
                    <div>
                        ${Math.ceil(response.data.data[i].price - (response.data.data[i].price * response.data.data[i].rate / 100)).toString()}원
                    </div>
                </div>

            </li>
            `;


            $("#axiosBody").append(html);


        }

        // .pagination 클래스 태그 내부 내용 교체
        const paginationDiv = $('.pagination');
        paginationDiv.empty();

        const pageInfo = response.data.pageInfo;

        const totalData = pageInfo.pages; // 총 페이지 수
        const pageNumberList = pageInfo.navigatepageNums; // 페이지 번호들의 순서를 담은 배열
        const currentPage = pageInfo.pageNum;

// 이전 페이지 버튼
        if (pageInfo.hasPreviousPage) {
            const prevBtn = $('<a>')
                .attr('href', '#')
                .addClass('prevBtn')
                .attr('value', pageInfo.prePage)
                .html('<span class="material-symbols-outlined">chevron_left</span>');
            paginationDiv.append(prevBtn);
        }

        const numbersDiv = $('<p>').addClass('numbers');

        pageNumberList.forEach((pageNumber) => {
            if (pageNumber <= totalData) {
                const numberBtn = $('<a>')
                    .text(pageNumber)
                    .attr('href', '#')
                    .addClass('numberBtn')
                    .attr('value', pageNumber);

                if (pageNumber === currentPage) {
                    numberBtn.addClass('bold').addClass("large-text").css("font-size", "16px").css("font-weight", "bold");
                }
                numbersDiv.append(numberBtn);
            }
        });

        paginationDiv.append(numbersDiv);

// 다음 페이지 버튼
        if (pageInfo.hasNextPage) {
            const nextBtn = $('<a>')
                .attr('href', '#')
                .addClass('nextBtn')
                .attr('value', pageInfo.nextPage)
                .html('<span class="material-symbols-outlined">chevron_right</span>');
            paginationDiv.append(nextBtn);
        }


    });
}
