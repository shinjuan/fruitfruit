package com.example.project_03.user;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface UserMapper {


    void insertMemberTerm(HashMap<String, Object> requestData);

    void insertMember(HashMap<String, Object> requestData);

    String emailChk(HashMap<String, Object> requestData);

    String nicknameChk(HashMap<String, Object> requestData);
}
