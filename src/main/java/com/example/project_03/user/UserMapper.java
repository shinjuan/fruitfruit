package com.example.project_03.user;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface UserMapper {


    void insertMemberTerm(HashMap<String, Object> requestData);

    void insertMember(HashMap<String, Object> requestData);

    HashMap<String,Object> emailChk(HashMap<String, Object> requestData);

    String nicknameChk(HashMap<String, Object> requestData);

    HashMap<String, Object> loginChk(HashMap<String, Object> requestData);

    void changePwd(HashMap<String, Object> requestData);

    HashMap<String, Object> selectProductDetail(HashMap<String,Object> detail);

    List<HashMap<String, Object>> selectCartList(String loggedInEmail);

    HashMap<String, Object> countCarted(String email);

    HashMap<String,Object> selectFirstCart(String firstProductId);

    List<HashMap<String,Object>> selectCheckboxCartList(HashMap<String,Object> selectedCart);
}
