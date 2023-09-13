package com.example.project_03.user;

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

}
