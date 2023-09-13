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

        HashMap<String, Object> test = new HashMap<>();

        test.put("pageNum", pageNum);
        test.put("pageSize", pageSize);

        List<HashMap<String, Object>> list = adminService.selectProductListAll(test);

        PageInfo<HashMap<String, Object>> pageInfo = new PageInfo<>(list);

        model.addAttribute("pageInfo", pageInfo);


        // 세션에서 로그인 정보를 가져옵니다.
        String loggedInEmail = (String) session.getAttribute("email");

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
    public HashMap<String, Object> product(@RequestBody HashMap<String, Object> requestData) {

        System.out.println("메인테스트" + requestData);

        int pageNum = (int) requestData.get("currentPage");
        int pageSize = (int) requestData.get("selectedTab");

        PageHelper.startPage(pageNum, pageSize);

        List<HashMap<String, Object>> data = adminService.selectProductList(requestData);
        PageInfo<HashMap<String, Object>> pageInfo = new PageInfo<>(data);


        HashMap<String, Object> data_count = new HashMap<>();

        data_count.put("data", data);
        data_count.put("pageInfo", pageInfo);

        log.info("메인페이지인포" + pageInfo);

        return data_count;
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
