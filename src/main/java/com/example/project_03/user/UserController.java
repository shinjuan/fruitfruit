package com.example.project_03.user;

import com.example.project_03.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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




        if (!checkbox.isEmpty()) {
            String loggedInEmail = (String) session.getAttribute("email");

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

            System.out.println("첫번째 상품이름:"+firstCartName);
            System.out.println("상품외갯수:"+cartCount);
            System.out.println("체크박스된장바구니:"+CheckboxCartList);

            model.addAttribute("firstCartName",firstCartName);
            model.addAttribute("cartCountAll",selectedCount);
            model.addAttribute("cartCount",cartCount);
            model.addAttribute("CheckboxCartList",CheckboxCartList);
        }



        return "payment";
    }



}
