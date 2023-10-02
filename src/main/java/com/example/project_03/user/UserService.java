package com.example.project_03.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

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


    public HashMap<String, Object> selectProductDetail(HashMap<String,Object> detail) {
       return userMapper.selectProductDetail(detail);
    }

    public List<HashMap<String, Object>> selectCartList(String email) {
       return userMapper.selectCartList(email);
    }

    public HashMap<String, Object> countCarted(String email) {
       return userMapper.countCarted(email);
    }

    public HashMap<String,Object> selectFirstCart(String firstProductId) {
       return userMapper.selectFirstCart(firstProductId);
    }

    public List<HashMap<String,Object>> selectCheckboxCartList(HashMap<String,Object> selectedCart) {
       return userMapper.selectCheckboxCartList(selectedCart);
    }

    public void insertDelivery(HashMap<String, String> formData) {
       userMapper.insertDelivery(formData);
    }

    public HashMap<String, Object> selectBasicDeliver(String email) {
       return userMapper.selectBasicDeliver(email);
    }

    public List<HashMap<String, Object>> selectDeliverList(String email) {
       return userMapper.selectDeliverList(email);
    }

    public  HashMap<String,Object> selectDelivery(HashMap<String, String> selectedValue) {
       return userMapper.selectDelivery(selectedValue);
    }

    public void insertOrder(HashMap<String, Object> requestData) {
       userMapper.insertOrder(requestData);
    }

    public int selectOrderId(String email) {
       return userMapper.selectOrderId(email);
    }

    public void insertOrder_Product(HashMap<String, Object> requestData) {
       userMapper.insertOrder_Product(requestData);
    }

    public List<HashMap<String, Object>> selectPaymentList(String email) {
       return userMapper.selectPaymentList(email);
    }

    public List<HashMap<String, Object>> selectSearchPaymentList(HashMap<String, Object> mypageSearch) {

        System.out.println("서비스쪽검색확인:"+mypageSearch);

       return userMapper.selectSearchPaymentList(mypageSearch);
    }
}
