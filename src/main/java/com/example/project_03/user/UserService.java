package com.example.project_03.user;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class UserService implements UserMapper {

    private final UserMapper userMapper;




   //회원가입 + 약관동의
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

    @Override
    public String emailChk(HashMap<String, Object> requestData) {
        return userMapper.emailChk(requestData);
    }

    @Override
    public String nicknameChk(HashMap<String, Object> requestData) {
        return userMapper.nicknameChk(requestData);
    }


}
