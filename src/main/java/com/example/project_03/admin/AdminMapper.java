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

    List<HashMap<String, Object>> selectProductListAll();

    HashMap<String, Object> getProductInfo(String id);
}
