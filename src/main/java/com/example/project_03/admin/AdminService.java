package com.example.project_03.admin;

import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService implements AdminMapper {

    private final AdminMapper adminMapper;


    @Override
    public void insertProduct(HashMap<String, Object> requestData) {}

    @Override
    public void insertPhoto(HashMap<String, Object> requestData) {}

    @Override
    public List<HashMap<String, Object>> selectProductList(HashMap<String, Object> requestData) {
        System.out.println("서비스쪽:"+requestData);
        return adminMapper.selectProductList(requestData);
    }

    @Override
    public void saleStopList(HashMap<String, Object> requestData) {
        adminMapper.saleStopList(requestData);
    }

    @Override
    public void productDelete(HashMap<String, Object> requestData) {
        adminMapper.productDelete(requestData);
    }

    @Override
    public void saleStop(HashMap<String, Object> requestData) {
        adminMapper.saleStop(requestData);
    }

    @Override
    public HashMap<String,Object> countStatus() {

        return adminMapper.countStatus();
    }

    @Override
    public List<HashMap<String, Object>> selectProductListAll(HashMap<String,Object> test) {

        int pageNum = Integer.parseInt(test.get("pageNum").toString());
        int pageSize = Integer.parseInt(test.get("pageSize").toString());

        System.out.println("서비스쪽email테스트"+test);

        PageHelper.startPage(pageNum, pageSize);

        return adminMapper.selectProductListAll(test);
    }

    @Override
    public HashMap<String, Object> getProductInfo(String id) {
        return adminMapper.getProductInfo(id);
    }

    @Override
    public int countProducts(HashMap<String, Object> requestData) {
        return adminMapper.countProducts(requestData);
    }

    @Override
    public HashMap<String, Object> countProductAll() {
        return adminMapper.countProductAll();
    }

    @Transactional
    public void insertProductAll(HashMap<String, Object> requestData) {
        adminMapper.insertProduct(requestData);
        adminMapper.insertPhoto(requestData);
    }

    public List<HashMap<String, Object>> selectProductListAll_excel() {
        return adminMapper.selectProductListAll_excel();
    }

    public String likeCheck(HashMap<String, Object> requestData) {

        return adminMapper.likeCheck(requestData);
    }

    public void likeDelete(HashMap<String, Object> requestData) {
        adminMapper.likeDelete(requestData);
    }

    public void likeAdd(HashMap<String, Object> requestData) {
        adminMapper.likeAdd(requestData);
    }

    public String cartCheck(HashMap<String, Object> requestData) {
        return adminMapper.cartCheck(requestData);
    }

    public void cartDelete(HashMap<String, Object> requestData) {
        adminMapper.cartDelete(requestData);
    }

    public void cartAdd(HashMap<String, Object> requestData) {
        adminMapper.cartAdd(requestData);
    }

    public void cartUpdate(HashMap<String, Object> requestData) {
        adminMapper.cartUpdate(requestData);
    }

    public String countCartList(String loggedInEmail) {
        return adminMapper.countCartList(loggedInEmail);
    }

    public String countLikeList(String loggedInEmail) {
        return adminMapper.countLikeList(loggedInEmail);
    }

    public void updateReviewReply(HashMap<String, Object> reviewData) {
        adminMapper.updateReviewReply(reviewData);
    }

    public List<HashMap<String, Object>> selectReviewListAll() {
        return adminMapper.selectReviewListAll();
    }

    public List<HashMap<String, Object>> selectSearchReviewList(HashMap<String, Object> reviewSearch) {
        return adminMapper.selectSearchReviewList(reviewSearch);
    }

    public HashMap<String, Object> selectOneReview(HashMap<String, Object> reviewId) {
        return adminMapper.selectOneReview(reviewId);
    }

    public List<HashMap<String, Object>> selectMemberListAll() {
        return adminMapper.selectMemberListAll();
    }

    public List<HashMap<String, Object>> selectSearchMemberList(HashMap<String, Object> memberSearch) {
        return adminMapper.selectSearchMemberList(memberSearch);
    }
}
