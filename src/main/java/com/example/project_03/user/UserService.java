package com.example.project_03.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class UserService implements UserMapper {

    private final UserMapper userMapper;




   //회원가입 + 약관동의
   @Transactional
    public void insertMemberTermAll(HashMap<String, Object> requestData) {
        userMapper.insertMember(requestData);
        userMapper.insertMemberTerm(requestData);
    }

    //약관동의
    @Override
    public void insertMemberTerm(HashMap<String, Object> requestData) {}
    //회원가입
    @Override
    public void insertMember(HashMap<String, Object> requestData) {}
    
    //이메일 중복체크
    @Override
    public HashMap<String,Object> emailChk(HashMap<String, Object> requestData) {
        return userMapper.emailChk(requestData);
    }
    
    //닉네임 중복체크
    @Override
    public String nicknameChk(HashMap<String, Object> requestData) {
        return userMapper.nicknameChk(requestData);
    }

    @Override
    public HashMap<String, Object> loginChk(HashMap<String, Object> requestData) {

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        HashMap<String, Object> user = userMapper.emailChk(requestData);
        HashMap<String, Object> result = new HashMap<>();

        String encodedPassword = (String) user.get("password");
        String newPassword = (String) requestData.get("password");


        System.out.println("서비스쪽 user"+user);

        if (user != null) {
            // 아이디가 존재하면 비밀번호 확인

            if (bCryptPasswordEncoder.matches(newPassword, encodedPassword)) {

                // 비밀번호가 일치하면 로그인 성공
                result.put("result", "success");
            } else {
                // 비밀번호가 일치하지 않으면 로그인 실패
                result.put("result", "fail");
            }
        } else {
            // 아이디가 존재하지 않으면 로그인 실패
            result.put("result", "fail2");
        }

        return result;
    }

    @Override
    public void changePwd(HashMap<String, Object> requestData) {
        userMapper.changePwd(requestData);
    }


}
