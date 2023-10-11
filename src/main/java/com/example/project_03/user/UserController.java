package com.example.project_03.user;

import com.example.project_03.admin.AdminService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AdminService adminService;



    @GetMapping("favicon.ico")
    @ResponseBody
    void noFavicon() {
    }


    @GetMapping("/changePW")
    public String login(@RequestParam("email") String email, Model model) {

        model.addAttribute("email",email);

        return  "changePw";
    }


    @GetMapping("{pageName}")
    public String login(@PathVariable String pageName) {

        return  pageName;
    }





    @PostMapping("join_ok")
    public String join_ok(@RequestParam HashMap<String, Object> requestData,
                          @RequestParam List<String> status,
                          BCryptPasswordEncoder bCryptPasswordEncoder,
                          RedirectAttributes redirectAttributes)  {

        String encodedPassword = bCryptPasswordEncoder.encode((String)requestData.get("password"));

        requestData.put("status",status);
        requestData.put("encodedPassword",encodedPassword);

        userService.insertMemberTermAll(requestData);

        redirectAttributes.addAttribute("email", requestData.get("email"));

        return "redirect:/joinConfirm";
    }

    @PostMapping("login_ok")
    public String login_ok(@RequestParam HashMap<String, Object> requestData,HttpServletResponse response, HttpSession session, Model model)  {

        HashMap<String, Object> result = userService.loginChk(requestData);

        String loginResult = (String) result.get("result");

        if (loginResult.equals("success")) {
            model.addAttribute("email", requestData.get("email"));

            // 로그인 유지 체크 여부 확인
            if (requestData.containsKey("login_keep")) {
                // 쿠키에 이메일 저장 (유효 기간은 7일로 설정)
                Cookie emailCookie = new Cookie("email", (String) requestData.get("email"));
                emailCookie.setMaxAge(7 * 24 * 60 * 60); // 7일 (초 단위)
                response.addCookie(emailCookie);
            } else {
                // 로그인 유지를 체크하지 않았을 경우, 세션에 이메일 저장 쿠키 삭제
                session.setAttribute("email", (String) requestData.get("email"));
                Cookie emailCookie = new Cookie("email", null);
                emailCookie.setMaxAge(0);
                response.addCookie(emailCookie);
            }

            return "redirect:/"; // 로그인 성공 시 이동할 뷰 페이지
        } else if (loginResult.equals("fail")) {
            model.addAttribute("failMessage", "이메일과 비밀번호가 일치하지 않습니다.");
            return "login";
        } else if (loginResult.equals("fail2")) {
            model.addAttribute("fail2Message", "이메일이 존재하지 않습니다.");
            return "login";
        }

        return loginResult;
    }




    @PostMapping("findpw_ok")
    public String findpw_ok(@RequestBody HashMap<String, Object> requestData,Model model)  {

        HashMap<String,Object> findemail = userService.emailChk(requestData);

        if (findemail == null) {
            return "modal/findpw_notok";
        } else {
            model.addAttribute("email", requestData.get("email"));
            return "modal/findpw_ok";
        }


    }

    @PostMapping("changepw_ok")
    public String changepw_ok(@RequestBody HashMap<String, Object> requestData, BCryptPasswordEncoder bCryptPasswordEncoder
                              ) {

        HashMap<String, Object> pwdchk = userService.emailChk(requestData);

        String encodedPassword = (String) pwdchk.get("password");
        String newPassword = (String) requestData.get("newpwd");

        if (bCryptPasswordEncoder.matches(newPassword, encodedPassword)) {
            return "modal/changepwd_same";
        } else {
            String encodedPassword2 = bCryptPasswordEncoder.encode((String)requestData.get("newpwd"));
            requestData.put("encodedPassword2",encodedPassword2);
            userService.changePwd(requestData);
            return "modal/changepwd";
        }


    }


    @ResponseBody
    @PostMapping("/emailchk")
    public String emailChk(@RequestBody HashMap<String, Object> requestData) {

        HashMap<String,Object> emailChk = userService.emailChk(requestData);

        if (emailChk != null) {
            return "이미 가입된 계정입니다.";

        } else {
            return null;
        }
    }

    @ResponseBody
    @PostMapping("/nicknamechk")
    public String nicknameChk(@RequestBody HashMap<String, Object> requestData) {

        String nicknameChk = userService.nicknameChk(requestData);

        if (nicknameChk != null) {
            return "해당 닉네임은 이미 사용 중입니다.";

        } else {
            return null;
        }
    }

    @GetMapping("/detail/{product_no}")
    public String detail(Model model, @PathVariable String product_no, HttpSession session) {


        String loggedInEmail = (String) session.getAttribute("email");

        HashMap<String,Object> detail = new HashMap<>();

        detail.put("email",loggedInEmail);
        detail.put("product_no",product_no);

        HashMap<String,Object> productDetail = userService.selectProductDetail(detail);

        model.addAttribute("detail",productDetail);

        return "detail";
    }

    @GetMapping("detail/review/{product_no}")
    public String review(Model model, @PathVariable String product_no, HttpSession session,@RequestParam(defaultValue = "1") int pageNum,
                         @RequestParam(defaultValue = "1") int pageSize) {


        String loggedInEmail = (String) session.getAttribute("email");

        PageHelper.startPage(pageNum, pageSize);


        HashMap<String,Object> detail = new HashMap<>();

        detail.put("email",loggedInEmail);
        detail.put("product_no",product_no);



        List<HashMap<String,Object>> reviewList = userService.selectReviewList(product_no);

        PageInfo<HashMap<String, Object>> pageInfo = new PageInfo<>(reviewList);
        System.out.println("마이페이지검색테스트:"+pageInfo);
        model.addAttribute("pageInfo", pageInfo);

        HashMap<String,Object> productDetail = userService.selectProductDetail(detail);

        model.addAttribute("detail",productDetail);

        model.addAttribute("reviewList",reviewList);
        model.addAttribute("email",loggedInEmail);

        HashMap<String,Object> emailAndProductNo = new HashMap<>();

        emailAndProductNo.put("email",loggedInEmail);
        emailAndProductNo.put("product_no",product_no);

        HashMap<String,Object> payCheckConfirm = userService.payCheckConfirm(emailAndProductNo);
        HashMap<String,Object> reviewCheckConfirm = userService.reviewCheckConfirm(emailAndProductNo);

        model.addAttribute("payCheckConfirm",payCheckConfirm);
        model.addAttribute("reviewCheckConfirm",reviewCheckConfirm);
        model.addAttribute("email",loggedInEmail);



        return "review";
    }

    @PostMapping("review_ok")
    public String review_ok(Model model, @RequestParam HashMap<String,Object> reviewData, HttpSession session) {

        System.out.println("리뷰데이터확인:"+reviewData);

        //유저 리뷰수정
        if("edit".equals(reviewData.get("review_type"))) {
            userService.updateReview(reviewData);
        }

        //유저 리뷰작성
        else if (!"admin".equals(reviewData.get("user_email"))) {
            userService.insertReview(reviewData);
        } else if("admin".equals(reviewData.get("user_email"))){ //어드민 유저 답글
            adminService.updateReviewReply(reviewData);
        }









        return "redirect:/detail/review/"+reviewData.get("product_id");
    }

    @ResponseBody
    @PostMapping("/detail_like")
    public HashMap<String,Object> like(@RequestBody HashMap<String, Object> requestData, HttpSession session) {
        System.out.println("찜확인"+requestData);

        String loggedInEmail = (String) session.getAttribute("email");

        requestData.put("email",loggedInEmail);

        String likeCheck = adminService.likeCheck(requestData);




        if (likeCheck != null) {
            adminService.likeDelete(requestData);

            requestData.put("like", 1);
            return requestData;
        } else {
            adminService.likeAdd(requestData);

            requestData.put("like", 2);
            return requestData;
        }


    }


    @PostMapping("/detail_cart")
    public String cart(@RequestBody HashMap<String, Object> requestData, HttpSession session) {


        String loggedInEmail = (String) session.getAttribute("email");

        requestData.put("email",loggedInEmail);

        String cartCheck = adminService.cartCheck(requestData);


        if (cartCheck != null) {


            return "modal/cart_not_ok";
        } else {
            adminService.cartAdd(requestData);


            return "modal/cart_ok";
        }


    }


    @GetMapping("/cart_list")
    public String cart(HttpSession session, Model model) {

        String loggedInEmail = (String) session.getAttribute("email");

        List<HashMap<String,Object>> cart_list = userService.selectCartList(loggedInEmail);
        HashMap<String,Object> carted_count = userService.countCarted(loggedInEmail);

        model.addAttribute("cart_list",cart_list);
        model.addAttribute("carted_count",carted_count);

        return "cart";
    }

    @ResponseBody
    @PostMapping("/delete_cart")
    public String delete_cart(HttpSession session, Model model,@RequestBody HashMap<String, Object> requestData) {

        String loggedInEmail = (String) session.getAttribute("email");

        requestData.put("email",loggedInEmail);

        adminService.cartDelete(requestData);

        return "장바구니삭제";
    }


    @RequestMapping("/payment")
    public String payment(Model model, HttpSession session,
                          @RequestParam List<String> checkbox, @RequestParam int selectedCount) {

        System.out.println("체크박스확인"+checkbox);
        System.out.println("카운트"+selectedCount);
        String loggedInEmail = (String) session.getAttribute("email");

        int countDelivery = userService.countDelivery(loggedInEmail);

        model.addAttribute("countDelivery", countDelivery);

        if (!checkbox.isEmpty()) {


            String firstProductId = checkbox.get(0);

            // 이제 firstProductId를 이용하여 원하는 조회 작업 수행
            // 예를 들어, 상품 조회 로직을 여기에 추가할 수 있습니다.

            System.out.println("첫 번째 상품 번호: " + firstProductId);

            HashMap<String,Object> firstCartName = userService.selectFirstCart(firstProductId);
            int cartCount = selectedCount - 1;

            HashMap<String,Object> selectedCart = new HashMap<>();
            selectedCart.put("checkbox",checkbox);
            selectedCart.put("email",loggedInEmail);

            List<HashMap<String,Object>> CheckboxCartList = userService.selectCheckboxCartList(selectedCart);


            HashMap<String,Object>  BasicDeliver = userService.selectBasicDeliver(loggedInEmail);
            List<HashMap<String,Object>> DeliverList = userService.selectDeliverList(loggedInEmail);


            System.out.println("첫번째 상품이름:"+firstCartName);
            System.out.println("상품외갯수:"+cartCount);
            System.out.println("체크박스된장바구니:"+CheckboxCartList);



            model.addAttribute("DeliverList", DeliverList);
            model.addAttribute("BasicDeliver",BasicDeliver);


            model.addAttribute("firstCartName",firstCartName);
            model.addAttribute("cartCountAll",selectedCount);
            model.addAttribute("cartCount",cartCount);
            model.addAttribute("CheckboxCartList",CheckboxCartList);
        }



        return "payment";
    }



    @ResponseBody
    @PostMapping("/delivery_ok")
    public String delivery_ok(HttpSession session, Model model,@RequestBody HashMap<String, String> formData
    ) {

        String loggedInEmail = (String) session.getAttribute("email");

        formData.put("email",loggedInEmail);

        System.out.println("배송지확인:"+formData);

        int countDelivery = userService.countDelivery(loggedInEmail);

        if(countDelivery > 3) {
            userService.insertDelivery(formData);

            return "배송지추가";
        } else {
            return "배송지초과";
        }


    }

    @ResponseBody
    @PostMapping("/delivery_edit_ok")
    public String delivery_edit_ok(HttpSession session, Model model,@RequestBody HashMap<String, String> formData
    ) {

        String loggedInEmail = (String) session.getAttribute("email");

        formData.put("email",loggedInEmail);

        System.out.println("배송지수정확인:"+formData);

        userService.updateDelivery(formData);

        return "배송지수정";



    }

    @ResponseBody
    @PostMapping("/delivery_delete_ok")
    public String delivery_delete_ok(HttpSession session, Model model,@RequestBody HashMap<String, String> formData
    ) {

        String loggedInEmail = (String) session.getAttribute("email");

        formData.put("email",loggedInEmail);

        System.out.println("배송지삭제확인:"+formData);

        userService.deleteDelivery(formData);

        return "배송지삭제";



    }


    @ResponseBody
    @PostMapping("/delivery_change")
    public HashMap<String,Object> delivery_change(HttpSession session, Model model,@RequestBody HashMap<String, String> selectedValue
    ) {

        String loggedInEmail = (String) session.getAttribute("email");

        selectedValue.put("email",loggedInEmail);

        System.out.println("배송지확인:"+selectedValue);

        HashMap<String,Object>  changeDeliver = userService.selectDelivery(selectedValue);

        return changeDeliver;

    }
    @Transactional
    @RequestMapping("/final")
    public String payment_final(HttpSession session, Model model, RedirectAttributes redirectAttributes,
                                @RequestParam HashMap<String ,Object> requestData,
                                @RequestParam List<String> status,
                                @RequestParam List<Integer> productIds,
                                @RequestParam List<Integer> productCount,
                                @RequestParam List<Integer> productPrice,
                                @RequestParam List<String> productName,
                                @RequestParam List<String> productImg
    ) {

        String loggedInEmail = (String) session.getAttribute("email");

        System.out.println("결제테스트"+ requestData);
        System.out.println("결제테스트2"+ status);
        System.out.println("결제테스트3"+ productIds);
        System.out.println("결제테스트4"+ productCount);
        System.out.println("결제테스트5"+ productPrice);
        System.out.println("결제테스트6"+ productName);

        userService.insertOrder(requestData);

        requestData.put("term", status);
        requestData.put("productIds", productIds); // 수정
        requestData.put("productCounts", productCount); // 수정
        requestData.put("productPrices", productPrice); // 수정
        requestData.put("productNames", productName); // 수정
        requestData.put("productImgs", productImg); // 수정

        int orderId = userService.selectOrderId(loggedInEmail);

        requestData.put("order_id",orderId);

        System.out.println("결제최종확인"+requestData);

        userService.insertOrder_Product(requestData);



        System.out.println("성공확인1:"+requestData.get("pay"));
        System.out.println("성공확인2:"+requestData.get("cardType"));
        System.out.println("성공확인3:"+requestData.get("howlong"));
        System.out.println("성공확인4:"+requestData.get("order_id"));

        redirectAttributes.addAttribute("pay",requestData.get("pay"));
        redirectAttributes.addAttribute("cardType",requestData.get("cardType"));
        redirectAttributes.addAttribute("howlong",requestData.get("howlong"));
        redirectAttributes.addAttribute("order_id",requestData.get("order_id"));

        return "redirect:/paymentSuccess";
    }



    @GetMapping("/mypage")
    public String mypage(HttpSession session, Model model,
                         @RequestParam(defaultValue = "1") int pageNum,
                         @RequestParam(defaultValue = "1") int pageSize
                         ) {
        LocalDate currentDate = LocalDate.now();

        // yyyy-MM-dd 형식의 문자열로 변환
        String formattedDate = currentDate.toString();

        // 모델에 날짜 문자열 추가
        model.addAttribute("date1", formattedDate);
        model.addAttribute("date2", formattedDate);


        PageHelper.startPage(pageNum, pageSize);

        String loggedInEmail = (String) session.getAttribute("email");

        List<HashMap<String,Object>> payment_list = userService.selectPaymentList(loggedInEmail);

        PageInfo<HashMap<String, Object>> pageInfo = new PageInfo<>(payment_list);

        System.out.println("마이페이지확인:"+payment_list);

        model.addAttribute("payment_list",payment_list);
        model.addAttribute("pageInfo", pageInfo);

        return "mypage";
    }

    @PostMapping("/mypage_serach")
    public String mypage_serach(HttpSession session, Model model,
                                @RequestParam("date1") String date1,
                                @RequestParam("date2") String date2,
                                @RequestParam("products") String searchType,
                                @RequestParam("searchInput") String searchInput,
                                @RequestParam(defaultValue = "1") int pageNum,
                                @RequestParam(defaultValue = "1") int pageSize
                                ) {

        PageHelper.startPage(pageNum, pageSize);


        model.addAttribute("date1",date1);
        model.addAttribute("date2",date2);
        model.addAttribute("searchType",searchType);

        // 날짜에 '00:00:00'와 '23:59:59' 추가
        date1 += " 00:00:00";
        date2 += " 23:59:59";

        System.out.println("date1="+date1);
        System.out.println("date2="+date2);
        System.out.println("searchType="+searchType);
        System.out.println("searchInput="+searchInput);

        HashMap<String,Object> mypage_search = new HashMap<>();

        mypage_search.put("date1",date1);
        mypage_search.put("date2",date2);
        mypage_search.put("searchType",searchType);
        mypage_search.put("searchInput",searchInput);

        List<HashMap<String,Object>> payment_list = userService.selectSearchPaymentList(mypage_search);

        PageInfo<HashMap<String, Object>> pageInfo = new PageInfo<>(payment_list);

        System.out.println("마이페이지검색테스트:"+pageInfo);


        model.addAttribute("pageInfo", pageInfo);


        return "mypage_search";
    }


    @GetMapping("/mypage_delivery")
    public String mypage_delivery(HttpSession session, Model model) {


        String loggedInEmail = (String) session.getAttribute("email");

        int countDelivery = userService.countDelivery(loggedInEmail);

        List<HashMap<String,Object>> delivery_all_list = userService.selectDeliverAllList(loggedInEmail);

        model.addAttribute("delivery_all_list", delivery_all_list);
        model.addAttribute("countDelivery", countDelivery);

        return "mypage_delivery";
    }


    @GetMapping("/mypageEdit")
    public String mypage_edit(HttpSession session, Model model) {


        String loggedInEmail = (String) session.getAttribute("email");

        if(loggedInEmail != null) {

            return "mypageEdit";
        } else {
            return "login";
        }
    }

    @PostMapping("/mypageEdit_ok")
    public String mypage_edit_ok(HttpSession session, Model model,@RequestParam HashMap<String, Object> requestData) {


        String loggedInEmail = (String) session.getAttribute("email");
        requestData.put("email",loggedInEmail);

        HashMap<String, Object> result = userService.loginChk(requestData);

        String loginResult = (String) result.get("result");

        if (loginResult.equals("success")) {
            model.addAttribute("email", requestData.get("email"));
            model.addAttribute("nickname", requestData.get("nickname"));
            model.addAttribute("email", requestData.get("email"));

            return "/mypageEdit02"; // 로그인 성공 시 이동할 뷰 페이지

        } else if (loginResult.equals("fail")) {
            model.addAttribute("failMessage", "비밀번호가 일치하지 않습니다.");
            return "mypageEdit";
        }

        return loginResult;
    }


}
