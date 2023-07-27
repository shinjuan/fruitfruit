package com.example.project_03.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;



    @GetMapping("{pageName}")
    public String login(@PathVariable String pageName) {

        return  pageName;
    }
//    @Transactional
//    @PostMapping("join_ok")
//    public String join_ok(@RequestBody HashMap<String, Object> requestData, Model model)  {
//
//        userService.insertMemberTermAll(requestData);
//
//        model.addAttribute("user_email", requestData.get("email"));
//        System.out.println("모델:"+model.getAttribute("user_email"));
//
//        return "joinConfirm";
//    }


    @PostMapping("join_ok")
    public String join_ok(@RequestParam HashMap<String, Object> requestData,
                          @RequestParam List<String> status
            , Model model)  {

        System.out.println("동기data"+requestData);
        System.out.println("체크박스data"+status);

        status.remove("1");

        requestData.put("status",status);

        System.out.println("최종requestData"+requestData);

        userService.insertMemberTermAll(requestData);

        model.addAttribute("user_email", requestData.get("email"));
        System.out.println("모델:"+model.getAttribute("user_email"));

        return "joinConfirm";
    }

    @ResponseBody
    @PostMapping("/emailchk")
    public String emailChk(@RequestBody HashMap<String, Object> requestData) {

        String emailChk = userService.emailChk(requestData);

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
