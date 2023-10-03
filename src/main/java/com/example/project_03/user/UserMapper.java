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

    void insertDelivery(HashMap<String, String> formData);

    HashMap<String, Object> selectBasicDeliver(String email);

    List<HashMap<String, Object>> selectDeliverList(String email);

    HashMap<String, Object> selectDelivery(HashMap<String, String> selectedValue);

    void insertOrder(HashMap<String, Object> requestData);

    int selectOrderId(String email);

    void insertOrder_Product(HashMap<String, Object> requestData);

    List<HashMap<String, Object>> selectPaymentList(String email);

    List<HashMap<String, Object>> selectSearchPaymentList(HashMap<String, Object> mypageSearch);

    List<HashMap<String, Object>> selectDeliverAllList(String email);

    int countDelivery(String email);

    void updateDelivery(HashMap<String, String> formData);

    void deleteDelivery(HashMap<String, String> formData);
}
