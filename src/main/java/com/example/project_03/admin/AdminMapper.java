package com.example.project_03.admin;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface AdminMapper {


    void insertProduct(HashMap<String, Object> requestData);

    void insertPhoto(HashMap<String, Object> requestData);

    List<HashMap<String, Object>> selectProductList(HashMap<String, Object> requestData);

    void saleStopList(HashMap<String, Object> requestData);

    void productDelete(HashMap<String, Object> requestData);

    void saleStop(HashMap<String, Object> requestData);


    HashMap<String,Object> countStatus();

    List<HashMap<String, Object>> selectProductListAll(HashMap<String,Object> test);

    HashMap<String, Object> getProductInfo(String id);

    int countProducts(HashMap<String, Object> requestData);

    HashMap<String, Object> countProductAll();

    List<HashMap<String, Object>> selectProductListAll_excel();

    String likeCheck(HashMap<String, Object> requestData);

    void likeDelete(HashMap<String, Object> requestData);

    void likeAdd(HashMap<String, Object> requestData);

    String cartCheck(HashMap<String, Object> requestData);

    void cartDelete(HashMap<String, Object> requestData);

    void cartAdd(HashMap<String, Object> requestData);

    void cartUpdate(HashMap<String, Object> requestData);

    String countCartList(String loggedInEmail);

    String countLikeList(String loggedInEmail);

    void updateReviewReply(HashMap<String, Object> reviewData);


    List<HashMap<String, Object>> selectReviewListAll();

    List<HashMap<String, Object>> selectSearchReviewList(HashMap<String, Object> reviewSearch);

    HashMap<String, Object> selectOneReview(HashMap<String, Object> reviewId);
}
