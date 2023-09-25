package com.example.project_03;

import com.example.project_03.admin.AdminService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
public class MainController {

    private final AdminService adminService;

    @GetMapping("")
    public String hello(Model model, HttpSession session, HttpServletRequest request,
                        @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "9") int pageSize) {

        // 세션에서 로그인 정보를 가져옵니다.
        String loggedInEmail = (String) session.getAttribute("email");

        HashMap<String, Object> test = new HashMap<>();

        test.put("email",loggedInEmail);
        test.put("pageNum", pageNum);
        test.put("pageSize", pageSize);

        List<HashMap<String, Object>> list = adminService.selectProductListAll(test);

        PageInfo<HashMap<String, Object>> pageInfo = new PageInfo<>(list);


        if(loggedInEmail != null) {

            String cart_total = adminService.countCartList(loggedInEmail);
            String like_total = adminService.countLikeList(loggedInEmail);

            System.out.println("카트토탈:"+cart_total);
            System.out.println("라이크토탈:"+like_total);

            model.addAttribute("cart_total", cart_total);
            model.addAttribute("like_total", like_total);
        }



        System.out.println("메인페이지리스트:"+list);
        System.out.println("메인페이지인포:"+pageInfo);

        model.addAttribute("pageInfo", pageInfo);





        // 쿠키에서 이메일 정보를 가져옵니다.
        Cookie[] cookies = request.getCookies();
        String cookieEmail = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("email")) {
                    cookieEmail = cookie.getValue();
                    break;
                }
            }
        }

        if (loggedInEmail != null || cookieEmail != null) {
            // 세션 또는 쿠키에 이메일 정보가 있는 경우 (로그인한 경우)
            String userEmail = loggedInEmail != null ? loggedInEmail : cookieEmail;
            model.addAttribute("loggedIn", true);
            model.addAttribute("loggedInEmail", userEmail);
        } else {
            // 로그인하지 않은 경우
            model.addAttribute("loggedIn", false);
        }

        Cookie[] cookies2 = request.getCookies();
        HashMap<String, String> recentData = new HashMap<>();

        if (cookies2 != null) {
            for (Cookie cookie : cookies2) {
                if (cookie.getName().startsWith("product_")) {
                    String productId = cookie.getValue();
                    String cookieName = cookie.getName();
                    String imageUrl = "";

                    // 이미지 쿠키를 찾습니다.
                    for (Cookie imageCookie : cookies2) {
                        if (imageCookie.getName().equals("image_" + cookieName.substring(8))) {
                            imageUrl = imageCookie.getValue();
                            break;
                        }
                    }

                    // 상품 ID를 키로, 이미지 URL을 값으로 맵에 저장합니다.
                    recentData.put(productId, imageUrl);
                }

                if (recentData.size() >= 3) {
                    break; // 최대 3개까지만 가져오도록 설정
                }
            }
        }

        model.addAttribute("recentData", recentData);



        return "index";
    }

    @ResponseBody
    @PostMapping("/main")
    public HashMap<String, Object> product(@RequestBody HashMap<String, Object> requestData, HttpSession session) {


        String loggedInEmail = (String) session.getAttribute("email");



        requestData.put("email",loggedInEmail);



        int pageNum = (int) requestData.get("currentPage");
        int pageSize = (int) requestData.get("selectedTab");

        PageHelper.startPage(pageNum, pageSize);

        List<HashMap<String, Object>> data = adminService.selectProductList(requestData);
        PageInfo<HashMap<String, Object>> pageInfo = new PageInfo<>(data);

        HashMap<String, Object> data_count = new HashMap<>();

        data_count.put("data", data);
        data_count.put("pageInfo", pageInfo);

        if(loggedInEmail != null) {

            String cart_total = adminService.countCartList(loggedInEmail);
            String like_total = adminService.countLikeList(loggedInEmail);

            System.out.println("카트토탈:"+cart_total);
            System.out.println("라이크토탈:"+like_total);

            data_count.put("cart_total", cart_total);
            data_count.put("like_total", like_total);

        }




        log.info("메인페이지인포" + pageInfo);

        return data_count;
    }


    @ResponseBody
    @PostMapping("/like")
    public HashMap<String,Object> like(@RequestBody HashMap<String, Object> requestData, HttpSession session) {
        System.out.println("찜확인"+requestData);

        String loggedInEmail = (String) session.getAttribute("email");

        requestData.put("email",loggedInEmail);

        String likeCheck = adminService.likeCheck(requestData);










            if (likeCheck != null) {
                adminService.likeDelete(requestData);
                String cart_total = adminService.countCartList(loggedInEmail);
                String like_total = adminService.countLikeList(loggedInEmail);
                requestData.put("cart_total",cart_total);
                requestData.put("like_total",like_total);
                requestData.put("like", 1);
                return requestData;
            } else {
                adminService.likeAdd(requestData);
                String cart_total = adminService.countCartList(loggedInEmail);
                String like_total = adminService.countLikeList(loggedInEmail);
                requestData.put("cart_total",cart_total);
                requestData.put("like_total",like_total);
                requestData.put("like", 2);
                return requestData;
            }





    }


    @ResponseBody
    @PostMapping("/cart")
    public HashMap<String,Object> cart(@RequestBody HashMap<String, Object> requestData, HttpSession session) {

        System.out.println("장바구니확인"+requestData);

        String loggedInEmail = (String) session.getAttribute("email");

        requestData.put("email",loggedInEmail);

        String cartCheck = adminService.cartCheck(requestData);



        if(cartCheck != null) {
            adminService.cartDelete(requestData);
            String cart_total = adminService.countCartList(loggedInEmail);
            String like_total = adminService.countLikeList(loggedInEmail);
            requestData.put("cart_total",cart_total);
            requestData.put("like_total",like_total);
            requestData.put("cart",1);
            return requestData;
        } else {
            adminService.cartAdd(requestData);
            String cart_total = adminService.countCartList(loggedInEmail);
            String like_total = adminService.countLikeList(loggedInEmail);
            requestData.put("cart_total",cart_total);
            requestData.put("like_total",like_total);
            requestData.put("cart",2);
            return requestData;
        }




    }


    @ResponseBody
    @PostMapping("/cart_update")
    public HashMap<String,Object> cart_update(@RequestBody HashMap<String, Object> requestData, HttpSession session) {

        System.out.println("장바구니확인"+requestData);

        String loggedInEmail = (String) session.getAttribute("email");

        requestData.put("email",loggedInEmail);

        String cartCheck = adminService.cartCheck(requestData);



        if(cartCheck != null) {
            adminService.cartUpdate(requestData);
            requestData.put("cart",1);
            return requestData;
        }

        return null;




    }



    @PostMapping("/save-product-info")
    @ResponseBody
    public String saveProductInfo(@RequestBody HashMap<String, Object> requestData, HttpServletRequest request, HttpServletResponse response) {
        // 받은 정보를 이용하여 쿠키에 저장

        String product_id = (String) requestData.get("product_id");
        String imageUrl = (String) requestData.get("imageUrl");

        // 쿠키 이름을 유니크하게 만듭니다.
        String productIdCookieName = "product_" + product_id;
        String imageUrlCookieName = "image_" + product_id;


        // 현재 쿠키 수를 확인합니다.
        int cookieCount = 0;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().startsWith("product_")) {
                    cookieCount++;
                    // 이미 만들어진 쿠키와 동일한 값이 들어오면 무시합니다.
                    if (cookie.getName().equals(productIdCookieName)) {
                        return "이미 만들어진 쿠키와 동일한 값입니다.";
                    }
                }
            }
        }

        // 최대 유지할 쿠키 개수를 정의
        int maxCookieCount = 3;


        // 현재 쿠키 수가 최대 개수보다 많을 경우 가장 오래된 쿠키 삭제
        if (cookieCount >= maxCookieCount) {
            // 가장 오래된 쿠키 삭제
            for (Cookie cookie : cookies) {
                if (cookie.getName().startsWith("product_")) {
                    // 쿠키 삭제를 위해 유효기간을 만료로 설정
                    cookie.setMaxAge(0); // 쿠키 만료
                    response.addCookie(cookie);
                    String imageCookieName = "image_" + cookie.getName().substring(8);
                    Cookie imageCookie = new Cookie(imageCookieName, null);
                    imageCookie.setMaxAge(0); // 이미지 쿠키 만료
                    response.addCookie(imageCookie);
                    break;
                }
            }
        }



        // 상품 정보를 쿠키에 저장합니다.
        response.addCookie(new Cookie(productIdCookieName, product_id));
        response.addCookie(new Cookie(imageUrlCookieName, imageUrl));





        return "상품 정보가 성공적으로 저장되었습니다.";
    }



    /* admin page */
    @GetMapping("/admin")
    public String adminIndex() {


        return "admin/index";
    }





}
