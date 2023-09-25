let selectedCategory = '';
let searchKeyword = ''; // 전역 변수로 선언
let selectedTab = 9;
let currentPage = 1;







$(document).on('click', '.category-btn', () => {
    selectedCategory = $(event.target).val();
    fetchData();
});

// $(document).on('click', '.material-icons', () => {
//     searchKeyword = $('#searchKeyword').val(); // 전역 변수에 값 저장
//     fetchData(); // fetchData 함수 호출 (매개변수 없음)
// });


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


       // <div class="cart__count" ></div>

        $(".cart__count").empty();
        $(".cart__count").append(response.data.cart_total);

        $(".favorite__count").empty();
        $(".favorite__count").append(response.data.like_total);


        $("#axiosBody").empty();

        for (let i = 0; i < response.data.data.length; i++) {
            const product = response.data.data[i];

            // <li> 엘리먼트 생성
            const liElement = document.createElement("li");
            liElement.className = "cart__click";

            // 입력 필드 생성
            const inputElements = [
                { name: "productInitPrice", value: product.price },
                { name: "productDiscountPrice", value: product.price * product.rate / 100 },
                { name: "productDiscountedPrice", value: product.price - (product.price * product.rate / 100) },
                { name: "productDiscount", value: product.rate }
            ];

            inputElements.forEach(input => {
                const inputElement = document.createElement("input");
                inputElement.className = input.name;
                inputElement.type = "hidden";
                inputElement.value = input.value;
                liElement.appendChild(inputElement);
            });

            // 클릭 시 상세페이지 이동
            const linkElement = document.createElement("a");
            linkElement.href = `/user/productDetail/${product.id}`;
            liElement.appendChild(linkElement);

            // 상품 사진 생성
            const imgElement = document.createElement("img");
            imgElement.className = "productImage";
            imgElement.src = product.file_name;
            imgElement.alt = "상품 이미지";
            imgElement.setAttribute("th:value", product.id);
            imgElement.onclick = function() {
                saveImageInfo(this);
            };
            linkElement.appendChild(imgElement);

            // 아이콘 영역 생성
            const iconsElement = document.createElement("div");
            iconsElement.className = "icons";
            liElement.appendChild(iconsElement);

            // 좋아요 아이콘 생성
            const likeElement = document.createElement("a");
            likeElement.href = "#";
            likeElement.id = product.id;
            const likeIcon = document.createElement("span");
            likeIcon.id = product.id;
            likeIcon.className = product.like_status === 'liked' ? "material-icons red__heart" : "material-symbols-outlined";
            likeIcon.innerText = "favorite";
            likeIcon.onclick = function() {
                handleFavoriteClick(this);
            };
            likeElement.appendChild(likeIcon);
            iconsElement.appendChild(likeElement);


            // 장바구니 카트 아이콘 생성
            const cartElement = document.createElement("a");
            const cartIcon = document.createElement("span");
            cartElement.href = "#";
            cartElement.id = product.id;
            const cartStatus = product.cart_status === 'carted' ? 'material-icons' : 'material-symbols-outlined';
            cartIcon.className = cartStatus;
            cartIcon.innerText = "shopping_cart";
            cartIcon.onclick = function() {
                handleCartClick(this);
            };
            cartElement.appendChild(cartIcon);
            iconsElement.appendChild(cartElement);


            // // 장바구니 카트 아이콘 생성
            // const cartElement = document.createElement("a");
            // const cartIcon = document.createElement("span");
            // cartIcon.className = "material-symbols-outlined";
            // cartIcon.innerText = "shopping_cart";
            // cartElement.appendChild(cartIcon);
            // iconsElement.appendChild(cartElement);

            // 장바구니 클릭 시 수량 증감 버튼 생성 (생략)

            // 상품 정보 생성
            const txtElement = document.createElement("div");
            txtElement.className = "txt";
            liElement.appendChild(txtElement);

            // 상품명 생성
            const titleElement = document.createElement("div");
            titleElement.className = "title";
            const productName = document.createElement("span");
            productName.className = "productName";
            productName.innerText = product.name;
            titleElement.appendChild(productName);
            txtElement.appendChild(titleElement);

            // 상품 가격 생성
            const priceElement = document.createElement("div");
            priceElement.innerText = `${Math.ceil(product.price - (product.price * product.rate / 100))}원`;
            txtElement.appendChild(priceElement);

            // 생성한 엘리먼트를 #axiosBody에 추가
            $("#axiosBody").append(liElement);


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
